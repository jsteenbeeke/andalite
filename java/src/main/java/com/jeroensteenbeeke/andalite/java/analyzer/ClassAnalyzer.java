/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.java.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ClassValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.*;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.*;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.*;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ConstructorInvocationStatement.InvocationType;
import com.jeroensteenbeeke.andalite.java.analyzer.types.ArrayType;
import com.jeroensteenbeeke.andalite.java.analyzer.types.*;
import com.jeroensteenbeeke.andalite.java.util.Locations;
import com.jeroensteenbeeke.lux.TypedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassAnalyzer {
	private static final Logger log = LoggerFactory
		.getLogger(ClassAnalyzer.class);

	private static final Set<Modifier.Keyword> VISIBILITY_KEYWORDS = ImmutableSet. <Modifier.Keyword> builderWithExpectedSize(4)
		.add(Modifier.Keyword.PUBLIC)
		.add(Modifier.Keyword.DEFAULT)
		.add(Modifier.Keyword.PRIVATE)
		.add(Modifier.Keyword.PROTECTED)
		.build();

	private final File targetFile;

	private final Locations.FileMap indexes;

	private final Locations.CharacterMap characterMap;

	public ClassAnalyzer(@NotNull File targetFile) {
		super();
		this.targetFile = targetFile;
		this.indexes = Locations
			.indexFile(targetFile)
			.throwIfNotOk(e -> new IllegalStateException("Could not index line numbers and columns: " + e));
		this.characterMap = Locations.mapCharacters(targetFile)
									 .throwIfNotOk(e -> new IllegalStateException("Could not index character positions: " + e));

	}

	@NotNull
	public TypedResult<AnalyzedSourceFile> analyze() {
		log.debug("Starting analysis of {}", targetFile.getAbsolutePath());

		JavaParser parser = new JavaParser(new ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17));

		return TypedResult.attempt(() -> parser.parse(targetFile))
						  .filter(ParseResult::isSuccessful, pr -> pr
							  .getProblems()
							  .stream()
							  .map(this::problemToString)
							  .collect(Collectors.joining(", ")))
						  .map(ParseResult::getResult)
						  .filter(Optional::isPresent, "Parse marked as successful but no result present")
						  .map(Optional::get)
						  .flatMap(compilationUnit -> {

							  compilationUnit.accept(new DebugVisitor(), log);

							  Optional<PackageDeclaration> packageDefinition = compilationUnit
								  .getPackageDeclaration();

							  final String packageName = packageDefinition
								  .map(this::determinePackageName)
								  .orElse("");

							  AnalyzerContext context = new AnalyzerContext(packageName, null);

							  AnalyzedSourceFile sourceFile = new AnalyzedSourceFile(
								  Locations.from(compilationUnit, indexes), targetFile, packageName,
								  packageDefinition
									  .map(p -> Locations.from(p, indexes))
									  .orElseThrow(IllegalStateException::new));

							  for (ImportDeclaration importDeclaration : compilationUnit
								  .getImports()) {
								  AnalyzedImport imp = new AnalyzedImport(
									  Locations.from(importDeclaration, indexes),
									  importDeclaration.getName().toString(),
									  importDeclaration.isStatic(),
									  importDeclaration.isAsterisk());

								  sourceFile.addImport(imp);
							  }

							  for (TypeDeclaration<?> typeDeclaration : compilationUnit.getTypes()) {
								  analyzeTypeDeclaration(sourceFile, context, typeDeclaration);
							  }

							  return TypedResult.ok(sourceFile);
						  });
	}

	private String problemToString(Problem problem) {
		return problem
			.getLocation()
			.map(loc -> String.format("%s at location %s - %s", problem.getMessage(), loc.getBegin().asString(), loc
				.getEnd()
				.asString()))
			.orElseGet(problem::getMessage);

	}

	private Denomination<?, ?> analyzeTypeDeclaration(@NotNull AnalyzedSourceFile sourceFile,
													  @NotNull AnalyzerContext context,
													  @NotNull TypeDeclaration<?> typeDeclaration) {
		if (typeDeclaration instanceof ClassOrInterfaceDeclaration decl) {

			if (decl.isInterface()) {

				AnalyzedInterface element = new AnalyzedInterface(sourceFile,
																  Locations.from(decl, indexes), keywords(Modifier.Keyword.DEFAULT, decl.getModifiers()),
																  context.getScope(), locate(decl.getName()));

				processInterfaceDeclaration(decl,
											context.innerDeclaration(decl.getName()), element);

				sourceFile.addInterface(element);

				return setJavadoc(decl, element);
			} else {
				AnalyzedClass element = new AnalyzedClass(sourceFile, Locations.from(decl, indexes),
														  keywords(Modifier.Keyword.DEFAULT, decl.getModifiers()), context
															  .getScope(),
														  locate(decl.getName())
				);

				processClassDeclaration(decl,
										context.innerDeclaration(decl.getName()), element);

				sourceFile.addClass(element);

				return setJavadoc(decl, element);
			}
		} else if (typeDeclaration instanceof EnumDeclaration decl) {

			AnalyzedEnum element = new AnalyzedEnum(sourceFile, Locations.from(decl, indexes),
													keywords(Modifier.Keyword.DEFAULT, decl.getModifiers()), context.getScope(), locate(decl.getName())
			);

			processEnumDeclaration(decl,
								   context.innerDeclaration(decl.getName()), element);

			sourceFile.addEnum(element);

			return setJavadoc(decl, element);
		} else if (typeDeclaration instanceof AnnotationDeclaration decl) {

			AnalyzedAnnotationType element = new AnalyzedAnnotationType(sourceFile,
																		Locations.from(decl, indexes), keywords(Modifier.Keyword.DEFAULT, decl.getModifiers()),
																		context.getScope(), locate(decl.getName()));

			processAnnotationDeclaration(decl, element);

			sourceFile.addAnnotation(element);

			return setJavadoc(decl, element);
		}

		return null;
	}

	private void processAnnotationDeclaration(AnnotationDeclaration decl,
											  AnalyzedAnnotationType element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
													new AnalyzerContext(element.getAnnotationName(), null));
			element.addAnnotation(annotation);
		}

		determineBodyLocation(element, true);
	}

	private void processEnumDeclaration(EnumDeclaration decl,
										AnalyzerContext analyzerContext, AnalyzedEnum element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
													analyzerContext);
			if (annotation != null) {
				element.addAnnotation(annotation);
			}
		}

		findEnumSeparator(decl).ifPresent(element::setSeparatorLocation);


		Optional<Integer> openCurlyBracket =
			findEnumSeparator(decl).map(Location::getEnd).map(end -> end + 1).or(() ->
																					 first(characterMap.findBetweenInclusive('{', element
																						 .getNameLocation(), element.getLocation()))
																						 .map(l -> l.getStart() + 1));
		Optional<Integer> closeCurlyBracket = last(characterMap.findBetweenInclusive('}', element.getNameLocation(), element
			.getLocation()))
			.map(Location::getEnd)
			.map(e -> e - 1);
		merge(openCurlyBracket, closeCurlyBracket, Location::new).ifPresentOrElse(element::setBodyLocation,
																				  () -> {
																					  throw new IllegalStateException("Enum declaration without body");
																				  }
		);

		List<ClassOrInterfaceType> implementedInterfaces = decl.getImplementedTypes();
		if (implementedInterfaces != null) {
			for (ClassOrInterfaceType type : implementedInterfaces) {
				getTypeNameWithGenerics(element, type).ifPresent(element::addInterface);
				element.setLastImplementsLocation(Locations.from(type, indexes));
			}
		}

		analyzeBody(decl, analyzerContext, element);
		List<EnumConstantDeclaration> entries = decl.getEntries();
		if (entries != null) {
			for (EnumConstantDeclaration constDecl : entries) {
				if (constDecl != null) {
					analyzeEnumConstant(element, analyzerContext, constDecl);
				}
			}
		}

	}

	private Optional<Location> findEnumSeparator(EnumDeclaration decl) {
		NodeList<EnumConstantDeclaration> entries = decl.getEntries();
		NodeList<BodyDeclaration<?>> members = decl.getMembers();
		if (entries.isEmpty() && members.isEmpty()) {
			// Semicolon is optional in an empty enum
			SortedSet<Location> semicolons = characterMap.findIn(';', Locations.from(decl, indexes));

			if (semicolons.isEmpty()) {
				return Optional.empty();
			} else {
				return Optional.of(semicolons.first());
			}

		} else {
			if (entries.isEmpty()) {
				// Semicolon MUST be present for the enum to compile
				SortedSet<Location> semicolons = characterMap.findBetweenExclusive(';', Locations.from(decl, indexes), Locations
					.from(members
							  .get(0), indexes));

				if (semicolons.isEmpty()) {
					return Optional.empty();
				} else {
					return Optional.of(semicolons.last());
				}

			} else if (members.isEmpty()) {
				// Semicolon optional, but possible present after last constant
				SortedSet<Location> semicolons = characterMap.findBetweenInclusive(';', Locations.from(entries.get(entries
																													   .size() - 1), indexes), Locations
																					   .from(decl, indexes));

				if (semicolons.isEmpty()) {
					return Optional.empty();
				} else {
					return Optional.of(semicolons.last());
				}
			} else {
				// Semicolon MUST be present between last entry and before first member
				SortedSet<Location> semicolons = characterMap.findBetweenExclusive(';', Locations.from(entries.get(entries
																													   .size() - 1), indexes), Locations
																					   .from(members.get(0), indexes));

				if (semicolons.isEmpty()) {
					return Optional.empty();
				} else {
					return Optional.of(semicolons.last());
				}
			}
		}


	}

	private void processEnumConstantDeclaration(EnumConstantDeclaration decl,
												AnalyzerContext analyzerContext, AnalyzedEnumConstant element) {
		List<AnnotationExpr> annotations = decl.getAnnotations();
		if (annotations != null) {
			for (AnnotationExpr expr : annotations) {
				AnalyzedAnnotation annotation = analyze(expr, element,
														analyzerContext);
				if (annotation != null) {
					element.addAnnotation(annotation);
				}
			}
		}


		List<BodyDeclaration<?>> members = decl.getClassBody();

		if (members != null) {
			for (BodyDeclaration<?> member : members) {
				analyzeBodyElement(null, element, analyzerContext, member);
			}
		}

		determineBodyLocation(element, false);
	}

	private void determineBodyLocation(ContainingDenomination<?, ?> element, boolean required) {
		Optional<Integer> openCurlyBracket = first(characterMap.findBetweenInclusive('{', element.getNameLocation(), element
			.getLocation()))
			.map(Location::getStart)
			.map(s -> s + 1);
		Optional<Integer> closeCurlyBracket = last(characterMap.findBetweenInclusive('}', element.getNameLocation(), element
			.getLocation()))
			.map(Location::getEnd)
			.map(e -> e - 1);
		merge(openCurlyBracket, closeCurlyBracket, Location::new).ifPresentOrElse(element::setBodyLocation, () -> {
			if (required) {
				throw new IllegalStateException("Missing body declaration for denomination " + element.getDenominationName());
			}
		});
	}

	public <T, U> Optional<U> merge(Optional<T> left, Optional<T> right, BiFunction<T, T, U> conversion) {
		if (left.isPresent() && right.isPresent()) {
			return Optional.of(conversion.apply(left.get(), right.get()));
		}

		return Optional.empty();
	}

	private void analyzeBodyElement(String typeName,
									ContainingDenomination<?, ?> element, AnalyzerContext analyzerContext,
									BodyDeclaration<?> member) {
		if (member instanceof FieldDeclaration) {
			List<AnalyzedField> fields = analyzeField((FieldDeclaration) member,
													  element, analyzerContext);
			for (AnalyzedField field : fields) {
				element.addField(field);
			}
		} else if (member instanceof MethodDeclaration) {
			Modifier.Keyword defaultKeyword = element instanceof AnalyzedInterface
				? Modifier.Keyword.PUBLIC : Modifier.Keyword.DEFAULT;

			AnalyzedMethod method = analyzeMethod((MethodDeclaration) member, defaultKeyword, element,
												  analyzerContext);

			if (method != null) {
				element.addMethod(method);
			}
		} else if (member instanceof EnumConstantDeclaration) {
			if (element instanceof AnalyzedEnum) {
				EnumConstantDeclaration enumDecl = (EnumConstantDeclaration) member;

				analyzeEnumConstant(element, analyzerContext, enumDecl);
			}

		} else if (member instanceof ConstructorDeclaration) {
			AnalyzedConstructor constr = analyzeConstructor(typeName,
															(ConstructorDeclaration) member, element, analyzerContext);
			addConstructor(element, constr);
		} else if (member instanceof ClassOrInterfaceDeclaration innerClassDecl) {

			if (innerClassDecl.isInterface()) {
				AnalyzedInterface innerInterface = new AnalyzedInterface(element.getSourceFile(),
																		 Locations.from(innerClassDecl, indexes),
																		 keywords(Modifier.Keyword.PUBLIC, innerClassDecl
																			 .getModifiers()),
																		 String.format("%s.%s", element.getPackageName(),
																					   element.getDenominationName()),
																		 locate(innerClassDecl.getName()));

				processInterfaceDeclaration(innerClassDecl, analyzerContext
												.innerDeclaration(innerClassDecl.getName()),
											innerInterface);

				element.addInnerDenomination(innerInterface);

			} else {
				AnalyzedClass innerClass = new AnalyzedClass(element.getSourceFile(),
															 Locations.from(innerClassDecl, indexes),
															 keywords(Modifier.Keyword.DEFAULT, innerClassDecl.getModifiers()),
															 String.format("%s.%s", element.getPackageName(),
																		   element.getDenominationName()),
															 locate(innerClassDecl.getName()));

				processClassDeclaration(innerClassDecl, analyzerContext
											.innerDeclaration(innerClassDecl.getName()),
										innerClass);

				element.addInnerDenomination(innerClass);
			}

		} else if (member instanceof EnumDeclaration decl) {

			AnalyzedEnum elem = new AnalyzedEnum(element.getSourceFile(), Locations.from(decl, indexes),
												 keywords(Modifier.Keyword.DEFAULT, decl.getModifiers()), analyzerContext
													 .getScope(),
												 locate(decl.getName()));

			processEnumDeclaration(decl,
								   analyzerContext.innerDeclaration(decl.getName()), elem);

			element.addInnerDenomination(elem);
		} else if (member instanceof AnnotationDeclaration decl) {

			AnalyzedAnnotationType elem = new AnalyzedAnnotationType(element.getSourceFile(),
																	 Locations.from(decl, indexes), keywords(Modifier.Keyword.PUBLIC, decl
				.getModifiers()),
																	 analyzerContext.getScope(), locate(decl.getName()));

			processAnnotationDeclaration(decl, elem);

			element.addInnerDenomination(elem);
		}
	}

	private List<Modifier.Keyword> keywords(@NotNull Modifier.Keyword defaultKeyword, @NotNull NodeList<Modifier> modifiers) {
		List<Modifier.Keyword> keywords = modifiers.stream().map(Modifier::getKeyword).collect(Collectors.toList());
		if (keywords.stream().noneMatch(VISIBILITY_KEYWORDS::contains)) {
			keywords.add(defaultKeyword);
		}

		return keywords;
	}

	private void analyzeEnumConstant(ContainingDenomination<?, ?> element,
									 AnalyzerContext analyzerContext, EnumConstantDeclaration enumDecl) {
		List<Expression> args = enumDecl.getArguments();
		List<AnalyzedExpression> paramExpressions = Lists.newLinkedList();
		if (args != null) {
			for (Expression expression : args) {
				paramExpressions.add(analyzeExpression(expression, element,
													   analyzerContext));
			}

		}

		AnalyzedEnumConstant constant = new AnalyzedEnumConstant(element.getSourceFile(),
																 Locations.from(enumDecl, indexes), ImmutableList.of(Modifier.Keyword.PUBLIC),
																 String.format("%s.%s", element.getPackageName(),
																			   element.getDenominationName()),
																 locate(enumDecl.getName()), paramExpressions);

		processEnumConstantDeclaration(enumDecl, analyzerContext, constant);

		((AnalyzedEnum) element).addConstant(constant);
	}

	private void addConstructor(ContainingDenomination<?, ?> element,
								AnalyzedConstructor constr) {
		if (element instanceof ConstructableDenomination<?, ?> constructable) {

			constructable.addConstructor(constr);
		}
	}

	private void processClassDeclaration(ClassOrInterfaceDeclaration decl,
										 AnalyzerContext analyzerContext, AnalyzedClass element) {
		determineBodyLocation(element, true);

		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
													analyzerContext);
			element.addAnnotation(annotation);
		}

		List<ClassOrInterfaceType> extendedClasses = decl.getExtendedTypes();
		if (extendedClasses != null) {
			for (ClassOrInterfaceType type : extendedClasses) {
				getTypeNameWithGenerics(element, type).ifPresent(element::setSuperClass);

				break; // Classes only have single inheritance
			}
		}

		List<ClassOrInterfaceType> implementedInterfaces = decl.getImplementedTypes();
		if (implementedInterfaces != null) {

			for (ClassOrInterfaceType type : implementedInterfaces) {
				getTypeNameWithGenerics(element, type).ifPresent(element::addInterface);
				element.setLastImplementsLocation(Locations.from(type, indexes));
			}
		}

		analyzeBody(decl, analyzerContext, element);

	}

	private void analyzeBody(TypeDeclaration<?> decl, AnalyzerContext analyzerContext, ContainingDenomination<?, ?> element) {
		List<BodyDeclaration<?>> members = decl.getMembers();
		if (members != null) {
			for (BodyDeclaration<?> member : members) {
				analyzeBodyElement(decl.getNameAsString(), element,
								   analyzerContext, member);
			}
		}
	}

	private Optional<GenerifiedName> getTypeNameWithGenerics(ContainingDenomination<?, ?> containingElement,
															 @Nullable ClassOrInterfaceType type) {
		if (type == null) {
			return Optional.empty();
		}
		List<Type> typeArgs = type.getTypeArguments().orElseGet(NodeList::new);

		LocatedName<SimpleName> locatedTypedName = locate(type.getName());
		String typeName = locatedTypedName.getName();

		String prefix = type
			.getScope()
			.flatMap(s -> getTypeNameWithGenerics(containingElement, s))
			.map(GenerifiedName::getName)
			.map(s -> s.concat("."))
			.orElse("");
		String generifiedName = prefix.concat(typeName);

		Location extent = locatedTypedName.getLocation();

		if (!typeArgs.isEmpty()) {
			generifiedName = prefix.concat(
				typeName.concat(typeArgs.stream().map(Node::toString)
										.collect(Collectors.joining(", ", "<", ">"))));


			extent = containingElement.getBodyLocation().orElse(extent);

			extent = last(characterMap.findBetweenInclusive('>', locatedTypedName.getLocation(), extent)).orElseThrow(() -> new IllegalStateException("Generified class without angle brackets"));
		}

		Location nameLocation = new Location(locatedTypedName.getLocation().getStart(), extent.getEnd());

		return Optional.of(new GenerifiedName(generifiedName, nameLocation));
	}

	private void processInterfaceDeclaration(ClassOrInterfaceDeclaration decl,
											 AnalyzerContext analyzerContext, AnalyzedInterface element) {
		determineBodyLocation(element, true);

		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
													analyzerContext);
			element.addAnnotation(annotation);
		}

		List<ClassOrInterfaceType> extendedClasses = decl.getExtendedTypes();
		if (extendedClasses != null) {
			for (ClassOrInterfaceType type : extendedClasses) {
				getTypeNameWithGenerics(element, type).ifPresent(element::addInterface);

				element.setLastImplementsLocation(Locations.from(type, indexes));
			}
		}

		for (BodyDeclaration<?> member : decl.getMembers()) {
			analyzeBodyElement(decl.getNameAsString(), element, analyzerContext,
							   member);
		}

	}

	private AnalyzedConstructor analyzeConstructor(String className,
												   ConstructorDeclaration member,
												   @NotNull ContainingDenomination<?, ?> containingDenomination,
												   @NotNull AnalyzerContext analyzerContext) {

		AnalyzedConstructor constructor = new AnalyzedConstructor(
			Locations.from(member, indexes), className, keywords(Modifier.Keyword.DEFAULT, member.getModifiers()),
			determineRightParenthesisLocation(member));

		constructor.addAnnotations(determineAnnotations(member,
														containingDenomination, analyzerContext));

		analyzeAndAddParameters(member, containingDenomination, analyzerContext, constructor);

		BlockStmt blockStatement = member.getBody();
		if (blockStatement != null) {
			BlockStatement statement = analyzeBlockStatement(blockStatement,
															 containingDenomination, analyzerContext);
			if (statement != null) {
				List<AnalyzedStatement<?, ?>> body = statement.getStatements();
				body.forEach(constructor::addStatement);
			}

			constructor.setBodyLocation(Locations.from(blockStatement, indexes));
		}


		return constructor;
	}

	private void analyzeAndAddParameters(CallableDeclaration<?> member, @NotNull ContainingDenomination<?, ?> containingDenomination, @NotNull AnalyzerContext analyzerContext, IParameterized parameterized) {
		List<Parameter> parameters = member.getParameters();
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				AnalyzedParameter analyzedParameter = analyzeParameter(parameter,
																	   containingDenomination, analyzerContext);
				parameterized.addParameter(analyzedParameter);
			}
		}
	}

	private Location determineRightParenthesisLocation(CallableDeclaration<?> member) {
		Location nameLocation = Locations.from(member.getName(), indexes);

		NodeList<Parameter> parameters = member.getParameters();
		SortedSet<Location> locations;

		if (parameters.isEmpty()) {
			locations = characterMap.findBetweenInclusive(')', nameLocation, Locations.from(member, indexes));
		} else {
			Parameter last = parameters.get(parameters.size() - 1);

			locations = characterMap.findBetweenInclusive(')', Locations.from(last, indexes), Locations.from(member, indexes));
		}

		if (locations.isEmpty()) {
			throw new IllegalStateException("Could not determine right parenthesis location");
		}

		return locations.last();
	}

	private AnalyzedMethod analyzeMethod(MethodDeclaration member,
										 @NotNull Modifier.Keyword defaultKeyword,
										 @NotNull ContainingDenomination<?, ?> containingDenomination,
										 @NotNull AnalyzerContext analyzerContext) {
		String nameAsString = member.getNameAsString();
		final AnalyzedMethod method = new AnalyzedMethod(Locations.from(member, indexes),
														 analyzeType(member.getType()), keywords(defaultKeyword, member.getModifiers()),
														 nameAsString);

		method.addAnnotations(determineAnnotations(member,
												   containingDenomination, analyzerContext));

		analyzeAndAddParameters(member, containingDenomination, analyzerContext, method);

		List<ReferenceType> thrown = member.getThrownExceptions();
		if (thrown != null) {
			thrown
				.stream()
				.filter(Objects::nonNull)
				.forEach(nameEx -> method.addThrownException(new AnalyzedThrownException(
					Locations.from(nameEx, indexes), nameEx.asString())));
		}

		member.getBody().ifPresent(body -> {
			analyzeBodyDeclaration(body, method::addStatement, containingDenomination, analyzerContext);
			method.setBodyLocation(Locations.from(body, indexes));
		});

		findParameterRightParenthesis(member).ifPresent(method::setRightParenthesisLocation);


		return addComments(member, setJavadoc(member, method));
	}

	private Optional<Location> findParameterRightParenthesis(CallableDeclaration<?> member) {
		Location nameLocation = Locations.from(member.getName(), indexes);
		Location memberLocation = Locations.from(member, indexes);

		NodeList<ReferenceType> thrownExceptions = member.getThrownExceptions();

		if (thrownExceptions.isNonEmpty()) {
			Location firstThrownException = Locations.from(thrownExceptions.get(0), indexes);

			return last(characterMap.findBetweenExclusive(')', nameLocation, firstThrownException));
		} else if (member instanceof NodeWithBlockStmt<?> nodeWithBlockStmt) {

			return last(characterMap.findBetweenExclusive(')', nameLocation, Locations.from(nodeWithBlockStmt.getBody(), indexes)));
		} else if (member instanceof NodeWithOptionalBlockStmt<?> nodeWithOptionalBlockStmt) {
			Optional<BlockStmt> body = nodeWithOptionalBlockStmt.getBody();
			if (body.isPresent()) {
				return last(characterMap.findBetweenExclusive(')', nameLocation, Locations.from(body.get(), indexes)));
			} else {
				NodeList<Parameter> parameters = member.getParameters();

				if (parameters.isNonEmpty()) {
					Location lastParameterLocation = Locations.from(parameters.get(parameters.size() - 1), indexes);

					Location searchArea = new Location(lastParameterLocation.getStart(), memberLocation
						.getEnd());

					return last(characterMap.findIn(')', searchArea));
				} else {
					// Abstract method without parameters and without exceptions, consider everything between name
					// and end of declaration, and pick last paren
					Location searchArea = new Location(nameLocation.getEnd(), memberLocation
						.getEnd());

					return last(characterMap.findIn(')', searchArea));
				}
			}
		} else {
			// No idea what sort of declaration this is supposed to be, search between name and declaration end
			// Abstract method without parameters and without exceptions, consider everything between name
			// and end of declaration, and pick last paren
			Location searchArea = new Location(nameLocation.getEnd(), memberLocation
				.getEnd());

			return last(characterMap.findIn(')', searchArea));
		}


	}

	private Optional<Location> first(SortedSet<Location> potentialLocations) {
		return fromSet(potentialLocations, SortedSet::first);
	}

	private Optional<Location> last(SortedSet<Location> potentialLocations) {
		return fromSet(potentialLocations, SortedSet::last);
	}

	private Optional<Location> fromSet(SortedSet<Location> potentialLocations, Function<SortedSet<Location>, Location> selector) {
		if (potentialLocations.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(selector.apply(potentialLocations));
	}

	private AnalyzedParameter analyzeParameter(Parameter parameter,
											   @NotNull ContainingDenomination<?, ?> containingDenomination,
											   @NotNull AnalyzerContext analyzerContext) {
		AnalyzedParameter param = new AnalyzedParameter(
			Locations.from(parameter, indexes), parameter.getType().asString(),
			parameter.getNameAsString());

		for (AnnotationExpr annotationExpr : parameter.getAnnotations()) {
			AnalyzedAnnotation annot = analyze(annotationExpr,
											   containingDenomination, analyzerContext);
			if (annot != null) {
				param.addAnnotation(annot);
			}
		}

		return param;
	}

	private List<AnalyzedField> analyzeField(FieldDeclaration member,
											 @NotNull ContainingDenomination<?, ?> containingDenomination,
											 @NotNull AnalyzerContext analyzerContext) {
		final AnalyzedType type = analyzeType(member.getCommonType());
		final List<Modifier.Keyword> modifiers = keywords(Modifier.Keyword.DEFAULT, member.getModifiers());

		Builder<AnalyzedField> builder = ImmutableList.builder();
		List<AnalyzedAnnotation> annotations = determineAnnotations(member,
																	containingDenomination, analyzerContext);

		for (VariableDeclarator var : member.getVariables()) {
			String name = var.getNameAsString();

			AnalyzedField analyzedField = new AnalyzedField(
				Locations.from(member, indexes), modifiers, name, type);

			analyzedField.setSpecificDeclarationLocation(Locations.from(var, indexes));
			analyzedField.addAnnotations(annotations);

			var.getInitializer().ifPresent(init ->
											   analyzedField.setInitializationExpression(analyzeExpression(
												   init, containingDenomination, analyzerContext)));

			builder.add(analyzedField);

		}

		return builder.build();
	}

	private List<AnalyzedAnnotation> determineAnnotations(
		BodyDeclaration<?> member,
		@NotNull ContainingDenomination<?, ?> containingDenomination,
		@NotNull AnalyzerContext analyzerContext) {
		Builder<AnalyzedAnnotation> annot = ImmutableList.builder();

		for (AnnotationExpr annotExpr : member.getAnnotations()) {
			AnalyzedAnnotation analyzedAnnotation = analyze(annotExpr,
															containingDenomination, analyzerContext);
			if (analyzedAnnotation != null) {
				annot.add(analyzedAnnotation);
			}
		}

		return annot.build();
	}

	private AnalyzedAnnotation analyze(AnnotationExpr expr,
									   ContainingDenomination<?, ?> containingDenomination,
									   AnalyzerContext analyzerContext) {
		AnalyzedAnnotation annotation = new AnalyzedAnnotation(
			Locations.from(expr, indexes), expr.getName().toString());

		if (expr instanceof SingleMemberAnnotationExpr annot) {

			Expression memberValue = annot.getMemberValue();

			assignValue(annotation, "value", memberValue,
						containingDenomination, analyzerContext);

			annotation.setParametersLocation(Locations.from(memberValue, indexes));
		} else if (expr instanceof NormalAnnotationExpr annot) {
			List<MemberValuePair> pairs = annot.getPairs();

			Location annotLocation = Locations.from(annot, indexes);

			int start = annotLocation.getEnd();
			int end = annotLocation.getEnd();

			if (pairs != null) {
				boolean first = true;

				for (MemberValuePair mvp : pairs) {
					Location mvpLocation = Locations.from(mvp, indexes);

					if (first) {
						first = false;

						start = mvpLocation.getStart();
					}

					String name = mvp.getNameAsString();
					Expression memberValue = mvp.getValue();

					assignValue(annotation, name, memberValue,
								containingDenomination, analyzerContext);

					end = mvpLocation.getEnd();
				}
			} else {
				boolean hasParentheses = expr.toString().length() - 1 > expr
					.getName().toString().length();

				if (hasParentheses) {
					start = end - 1;
				}

				annotation.setHasParentheses(hasParentheses);
			}

			annotation.setParametersLocation(new Location(start, end));

		} else if (expr instanceof MarkerAnnotationExpr) {
			annotation.setHasParentheses(false);
		} else {
			System.err.println();
		}

		return annotation;
	}

	private void assignValue(AnalyzedAnnotation annotation, String name,
							 Expression expr, ContainingDenomination<?, ?> containingDenomination,
							 AnalyzerContext analyzerContext) {
		BaseValue<?, ?, ?> value = translateExpression(name, expr,
													   containingDenomination, analyzerContext);

		if (value != null) {
			annotation.addAnnotation(value);
		}

	}

	private BaseValue<?, ?, ?> translateExpression(String name, Expression expr,
												   ContainingDenomination<?, ?> containingDenomination,
												   AnalyzerContext analyzerContext) {
		if (expr instanceof AnnotationExpr annot) {
			AnalyzedAnnotation sub = analyze(annot, containingDenomination,
											 analyzerContext);

			return new AnnotationValue(Locations.from(expr, indexes), name, sub);
		} else if (expr instanceof BooleanLiteralExpr bool) {

			return new BooleanValue(Locations.from(expr, indexes), name, bool.getValue());
		} else if (expr instanceof CharLiteralExpr charLit) {

			return new CharValue(Locations.from(expr, indexes), name,
								 charLit.getValue().charAt(0));

		} else if (expr instanceof DoubleLiteralExpr doubLit) {

			return new DoubleValue(Locations.from(expr, indexes), name,
								   Double.parseDouble(doubLit.getValue()));
		} else if (expr instanceof IntegerLiteralExpr intLit) {

			return new IntegerValue(Locations.from(expr, indexes), name,
									Integer.parseInt(intLit.getValue()));
		} else if (expr instanceof LongLiteralExpr longLit) {

			return new LongValue(Locations.from(expr, indexes), name,
								 Long.parseLong(longLit.getValue()));
		} else if (expr instanceof StringLiteralExpr str) {

			return new StringValue(Locations.from(expr, indexes), name, str.getValue());
		} else if (expr instanceof ClassExpr classExpr) {

			return new ClassValue(Locations.from(expr, indexes), name,
								  classExpr.getType().toString());

		} else if (expr instanceof ArrayInitializerExpr array) {

			Builder<BaseValue<?, ?, ?>> builder = ImmutableList.builder();
			for (Expression expression : array.getValues()) {
				BaseValue<?, ?, ?> val = translateExpression(null, expression,
															 containingDenomination, analyzerContext);
				if (val != null) {
					builder.add(val);
				}
			}

			return new ArrayValue(Locations.from(expr, indexes), name, builder.build());
		} else if (expr instanceof FieldAccessExpr fieldAccess) {
			// Enum field reference or similar, just turn it into a big string,
			// ignore type params (shouldn't be there in enums)

			final String scope = analyzeExpression(fieldAccess.getScope(),
												   containingDenomination, analyzerContext).toJavaString();
			final String field = fieldAccess.getNameAsString();

			return new FieldAccessValue(Locations.from(expr, indexes), name, scope,
										field);

		}

		return null;
	}

	private String determinePackageName(PackageDeclaration pkg) {

		return pkg != null ? pkg.getName().asString() : "";
	}

	private void analyzeBodyDeclaration(@NotNull final BlockStmt body,
										@NotNull final IStatementAssigner assigner,
										ContainingDenomination<?, ?> containingDenomination,
										AnalyzerContext analyzerContext) {
		List<Statement> statements = body.getStatements();
		if (statements != null) {
			for (Statement statement : statements) {
				assigner.assignStatement(analyzeStatement(statement,
														  containingDenomination, analyzerContext));
			}
		}

	}

	@NotNull
	private AnalyzedStatement<?, ?> analyzeStatement(@NotNull Statement statement,
													 @NotNull ContainingDenomination<?, ?> containingDenomination,
													 @NotNull AnalyzerContext analyzerContext) {
		Location location = Locations.from(statement, indexes);
		if (statement instanceof ReturnStmt returnStatement) {


			AnalyzedExpression returnExpression = returnStatement
				.getExpression()
				.map(expr -> analyzeExpression(expr,
											   containingDenomination, analyzerContext))
				.orElse(null);
			return addComments(statement,
							   new ReturnStatement(location, returnExpression));

		} else if (statement instanceof IfStmt ifStmt) {

			AnalyzedExpression condition = analyzeExpression(
				ifStmt.getCondition(), containingDenomination,
				analyzerContext);
			AnalyzedStatement<?, ?> thenStatement = analyzeStatement(
				ifStmt.getThenStmt(), containingDenomination,
				analyzerContext);

			IfStatement ifStatement = new IfStatement(location, condition,
													  thenStatement);

			ifStmt.getElseStmt().ifPresent(elseStatement -> ifStatement.setElseStatement(analyzeStatement(elseStatement,
																										  containingDenomination, analyzerContext)));

			return addComments(statement, ifStatement);

		} else if (statement instanceof AssertStmt assertStmt) {

			return addComments(statement,
							   new AssertStatement(Locations.from(assertStmt, indexes),
												   analyzeExpression(assertStmt.getCheck(),
																	 containingDenomination, analyzerContext)));
		} else if (statement instanceof BlockStmt blockStmt) {

			return addComments(statement, analyzeBlockStatement(blockStmt,
																containingDenomination, analyzerContext));

		} else if (statement instanceof BreakStmt breakStatement) {

			return addComments(statement, new BreakStatement(
				Locations.from(breakStatement, indexes), breakStatement
				.getLabel()
				.map(SimpleName::asString)
				.orElse(null)));

		} else if (statement instanceof ContinueStmt continueStatement) {

			return addComments(statement, new ContinueStatement(
				Locations.from(continueStatement, indexes), continueStatement.getLabelAsString().orElse(null)));
		} else if (statement instanceof DoStmt doStatement) {

			AnalyzedExpression condition = analyzeExpression(
				doStatement.getCondition(), containingDenomination,
				analyzerContext);
			AnalyzedStatement<?, ?> body = analyzeStatement(doStatement.getBody(),
															containingDenomination, analyzerContext);

			return addComments(statement,
							   new DoStatement(Locations.from(doStatement, indexes), condition, body));
		} else if (statement instanceof EmptyStmt) {
			return new EmptyStatement(Locations.from(statement, indexes));
		} else if (statement instanceof ExplicitConstructorInvocationStmt explicitConstructorInvocationStatement) {

			InvocationType invocationType = explicitConstructorInvocationStatement
				.isThis() ? InvocationType.THIS : InvocationType.SUPER;

			ConstructorInvocationStatement invocation = new ConstructorInvocationStatement(
				Locations.from(explicitConstructorInvocationStatement, indexes),
				invocationType);

			explicitConstructorInvocationStatement.getExpression().ifPresent(expr -> {
				AnalyzedExpression scopeExpression = analyzeExpression(expr,
																	   containingDenomination, analyzerContext);
				invocation.setScope(scopeExpression);
			});

			explicitConstructorInvocationStatement
				.getTypeArguments().ifPresent(typeArgs -> {
				for (Type type : typeArgs) {
					if (type != null) {
						AnalyzedType analyzedType = analyzeType(type);
						if (analyzedType != null) {
							invocation.addTypeArgument(analyzedType);
						}
					}
				}
			});

			List<Expression> args = explicitConstructorInvocationStatement.getArguments();
			if (args != null) {
				for (Expression expression : args) {
					if (expression != null) {
						AnalyzedExpression analyzedExpression = analyzeExpression(
							expression, containingDenomination,
							analyzerContext);
						invocation.addArgument(analyzedExpression);
					}
				}
			}

			return addComments(statement, invocation);
		} else if (statement instanceof ExpressionStmt expr) {
			AnalyzedExpression e = analyzeExpression(expr.getExpression(),
													 containingDenomination, analyzerContext);

			return addComments(statement,
							   new ExpressionStatement(Locations.from(expr, indexes), e));

		} else if (statement instanceof ForEachStmt foreachStatement) {

			AnalyzedStatement<?, ?> body = analyzeStatement(foreachStatement.getBody(),
															containingDenomination, analyzerContext);
			VariableDeclarationExpression declareExpression = analyzeVariableDeclarationExpression(
				foreachStatement.getVariable(), containingDenomination,
				analyzerContext);
			AnalyzedExpression iterable = analyzeExpression(
				foreachStatement.getIterable(), containingDenomination,
				analyzerContext);

			return addComments(statement,
							   new ForEachStatement(Locations.from(foreachStatement, indexes),
													declareExpression, iterable, body));
		} else if (statement instanceof ForStmt forStmt) {

			AnalyzedExpression compare = forStmt
				.getCompare()
				.map(e -> analyzeExpression(e, containingDenomination, analyzerContext))
				.orElse(null);

			AnalyzedStatement<?, ?> body = analyzeStatement(forStmt.getBody(),
															containingDenomination, analyzerContext);

			ForStatement forStatement = new ForStatement(Locations.from(forStmt, indexes),
														 body, compare);

			forStmt.getInitialization().forEach(expression -> {
				AnalyzedExpression e = analyzeExpression(expression,
														 containingDenomination, analyzerContext);
				forStatement.addInitializerExpression(e);
			});

			List<Expression> update = forStmt.getUpdate();
			if (update != null) {
				for (Expression expression : update) {
					AnalyzedExpression e = analyzeExpression(expression,
															 containingDenomination, analyzerContext);
					forStatement.addUpdateExpression(e);
				}
			}

			return addComments(statement, forStatement);
		} else if (statement instanceof LabeledStmt labeledStatement) {

			String label = labeledStatement.getLabel().asString();
			AnalyzedStatement<?, ?> analyzedStatement = analyzeStatement(
				labeledStatement.getStatement(), containingDenomination,
				analyzerContext);

			return new LabeledStatement(Locations.from(labeledStatement, indexes), label,
										analyzedStatement);
		} else if (statement instanceof SwitchStmt switchStmt) {

			SwitchStatement switchStatement = new SwitchStatement(
				Locations.from(switchStmt, indexes),
				analyzeExpression(switchStmt.getSelector(),
								  containingDenomination, analyzerContext));

			NodeList<SwitchEntry> entries = switchStmt.getEntries();
			if (entries != null) {
				for (SwitchEntry entryStatement : entries) {
					analyzeSwitchEntry(entryStatement,
									   containingDenomination, analyzerContext).forEach(switchStatement::addStatement);
				}
			}

			return addComments(statement, switchStatement);
		} else if (statement instanceof SynchronizedStmt synchronizedStatement) {

			AnalyzedStatement<?, ?> block = analyzeStatement(
				synchronizedStatement.getBody(), containingDenomination,
				analyzerContext);
			AnalyzedExpression syncExpression = analyzeExpression(
				synchronizedStatement.getExpression(), containingDenomination,
				analyzerContext);

			return addComments(statement, new SynchronizedStatement(
				Locations.from(synchronizedStatement, indexes), syncExpression, block));
		} else if (statement instanceof ThrowStmt throwStatement) {

			return addComments(statement,
							   new ThrowStatement(Locations.from(throwStatement, indexes),
												  analyzeExpression(throwStatement.getExpression(),
																	containingDenomination, analyzerContext)));
		} else if (statement instanceof TryStmt tryStmt) {

			BlockStatement finallyBlock = tryStmt
				.getFinallyBlock()
				.map(stmt -> analyzeBlockStatement(stmt, containingDenomination, analyzerContext))
				.orElse(null);

			BlockStatement tryBlock = analyzeBlockStatement(
				tryStmt.getTryBlock(), containingDenomination,
				analyzerContext);

			TryStatement tryStatement = new TryStatement(Locations.from(tryStmt, indexes),
														 tryBlock, finallyBlock);


			List<Expression> resources = tryStmt.getResources();
			for (Expression resource : resources) {
				if (resource instanceof VariableDeclarationExpr expr) {

					AnalyzedType type = analyzeType(expr.getElementType());

					expr.getVariables().forEach(decl -> {
						String resourceId = decl.getNameAsString();
						ResourceStatement resourceStatement = new ResourceStatement(
							Locations.from(resource, indexes), type, resourceId,
							keywords(Modifier.Keyword.DEFAULT, expr.getModifiers()).contains(Modifier.Keyword.FINAL));

						decl.getInitializer().ifPresent(initExpr -> {


							AnalyzedExpression initializer = analyzeExpression(
								initExpr, containingDenomination,
								analyzerContext);
							resourceStatement.setInitializer(initializer);

						});

						List<AnnotationExpr> annotations = expr
							.getAnnotations();
						if (annotations != null) {
							processAnnotations(containingDenomination, analyzerContext, resourceStatement, annotations);
						}

						tryStatement.addResourceStatement(resourceStatement);

					});
				}

				// TODO: FieldAccessExpr and NameExpr are also valid since Java 9

			}
			List<CatchClause> catchs = tryStmt.getCatchClauses();
			if (catchs != null) {
				for (CatchClause catchClause : catchs) {

					BlockStatement blockStatement = analyzeBlockStatement(
						catchClause.getBody(), containingDenomination,
						analyzerContext);
					Parameter except = catchClause.getParameter();

					CatchStatement catchStatement = new CatchStatement(
						Locations.from(catchClause, indexes), blockStatement,
						except.getNameAsString());

					List<AnnotationExpr> annotations = except.getAnnotations();
					if (annotations != null) {
						catchStatement = processAnnotations(containingDenomination, analyzerContext, catchStatement, annotations);
					}

					Type type = except.getType();
					AnalyzedType analyzedType = analyzeType(type);

					if (analyzedType != null) {
						catchStatement.addExceptionType(analyzedType);
					}

					tryStatement.addCatchClause(catchStatement);

				}
			}

			return addComments(statement, tryStatement);
		} else if (statement instanceof LocalClassDeclarationStmt typeDeclarationStatement) {

			return addComments(statement,
							   new TypeDeclarationStatement(
								   Locations.from(typeDeclarationStatement, indexes),
								   analyzeTypeDeclaration(containingDenomination.getSourceFile(), analyzerContext.anonymousInnerClass(),
														  typeDeclarationStatement.getClassDeclaration())));
		} else if (statement instanceof WhileStmt whileStatement) {

			AnalyzedExpression condition = analyzeExpression(
				whileStatement.getCondition(), containingDenomination,
				analyzerContext);
			AnalyzedStatement<?, ?> body = analyzeStatement(whileStatement.getBody(),
															containingDenomination, analyzerContext);

			return addComments(statement, new WhileStatement(
				Locations.from(whileStatement, indexes), condition, body));
		}

		throw new IllegalStateException(
			"Unhandled statement type! Go slap the andalite developers!");
	}

	private <T extends IAnnotationAddable<T>> T processAnnotations(@NotNull ContainingDenomination<?, ?> containingDenomination, @NotNull AnalyzerContext analyzerContext, T addable, List<AnnotationExpr> annotations) {
		for (AnnotationExpr annotationExpr : annotations) {
			AnalyzedAnnotation annotation = analyze(
				annotationExpr, containingDenomination,
				analyzerContext);
			addable = addable.addAnnotation(annotation);
		}

		return addable;
	}

	private <T extends Commentable> T addComments(Node node, T commentable) {
		node.getComment().ifPresent(comment -> {
				checkJavadoc(commentable, comment);
				commentable.addComment(comment.getContent());
		});
		return commentable;
	}

	private <T extends Commentable> void checkJavadoc(T commentable,
													  Comment comment) {
		if (comment instanceof JavadocComment jdc
			&& commentable instanceof Javadocable jda) {

			if (!jda.getJavadoc().isPresent()) {
				String content = sanitizeJavadoc(jdc.getContent());
				if (content.startsWith(" * ")) {
					content = content.substring(3);
				}

				jda.setJavadoc(content);
			}
		}
	}

	private <T extends Javadocable> T setJavadoc(BodyDeclaration<?> node,
												 T javadocable) {
		if (node != null) {
			JavadocComment comment = (JavadocComment) node
				.getComment()
				.filter(c -> c instanceof JavadocComment).orElse(null);
			if (comment != null && javadocable != null) {
				String content = sanitizeJavadoc(comment.getContent());

				javadocable.setJavadoc(content);
			}
		}

		return javadocable;
	}

	public String sanitizeJavadoc(String input) {
		return Arrays.stream(input.split("\n"))
					 .map(s -> {
						 if (s.startsWith(" * ")) {
							 return s.substring(3);
						 }

						 return s;
					 }).collect(Collectors.joining("\n"));
	}

	private BlockStatement analyzeBlockStatement(BlockStmt blockStatement,
												 ContainingDenomination<?, ?> containingDenomination,
												 AnalyzerContext analyzerContext) {
		BlockStatement block = new BlockStatement(Locations.from(blockStatement, indexes));

		List<Statement> Statements = blockStatement.getStatements();
		if (Statements != null) {
			for (Statement s : Statements) {
				AnalyzedStatement<?, ?> analyzedStatement = analyzeStatement(s,
																			 containingDenomination, analyzerContext);
				block.addStatement(analyzedStatement);
			}
		}

		return block;
	}

	private List<SwitchEntryStatement> analyzeSwitchEntry(
		SwitchEntry switchEntryStatement,
		ContainingDenomination<?, ?> containingDenomination,
		AnalyzerContext analyzerContext) {
		List<Expression> labels = switchEntryStatement.getLabels();

		return labels.stream()
					 .map(label -> {
						 AnalyzedExpression value = analyzeExpression(label,
																	  containingDenomination, analyzerContext);

						 SwitchEntryStatement switchEntry = new SwitchEntryStatement(
							 Locations.from(switchEntryStatement, indexes), value);

						 List<Statement> Statements = switchEntryStatement.getStatements();
						 if (Statements != null) {
							 for (Statement Statement : Statements) {
								 AnalyzedStatement<?, ?> analyzed = analyzeStatement(Statement,
																					 containingDenomination, analyzerContext);

								 switchEntry.addStatement(analyzed);
							 }
						 }

						 return switchEntry;

					 }).collect(Collectors.toList());

	}

	@NotNull
	private AnalyzedExpression analyzeExpression(Expression expr,
												 ContainingDenomination<?, ?> containingDenomination,
												 AnalyzerContext analyzerContext) {
		final Location location = Locations.from(expr, indexes);

		if (expr instanceof NullLiteralExpr) {
			return new NullExpression(location);

		}
		if (expr instanceof BooleanLiteralExpr bool) {

			return new BooleanLiteralExpression(location, bool.getValue());
		}
		if (expr instanceof CharLiteralExpr charLit) {

			return new CharLiteralExpression(location,
											 charLit.getValue().charAt(0));
		}
		if (expr instanceof DoubleLiteralExpr doubLit) {

			return new DoubleLiteralExpression(location, doubLit.getValue());
		}
		if (expr instanceof IntegerLiteralExpr intLit) {

			return new IntegerLiteralExpression(location,
												Integer.parseInt(intLit.getValue()));
		}
		if (expr instanceof LongLiteralExpr longLit) {

			return new LongLiteralExpression(location, longLit.getValue());
		}
		if (expr instanceof StringLiteralExpr lit) {

			return new StringLiteralExpression(location, lit.getValue());
		}
		if (expr instanceof NameExpr name) {

			return new NameReferenceExpression(location,
											   name.getNameAsString());
		}
		if (expr instanceof MethodCallExpr methodCall) {

			List<AnalyzedType> analyzedTypeArguments = Lists.newArrayList();
			methodCall.getTypeArguments().ifPresent(typeArgs -> {
				for (Type type : typeArgs) {
					AnalyzedType at = analyzeType(type);

					analyzedTypeArguments.add(at);
				}
			});


			List<AnalyzedExpression> analyzedArguments = Lists.newArrayList();
			List<Expression> args = methodCall.getArguments();
			if (args != null) {
				for (Expression expression : args) {
					AnalyzedExpression ae = analyzeExpression(expression,
															  containingDenomination, analyzerContext);
					analyzedArguments.add(ae);
				}
			}

			MethodCallExpression expression = new MethodCallExpression(location,
																	   methodCall.getNameAsString(), analyzedTypeArguments,
																	   analyzedArguments);

			methodCall.getScope().ifPresent(scopeExpr -> expression.setScope(analyzeExpression(scopeExpr,
																							   containingDenomination, analyzerContext)));

			return expression;

		}
		if (expr instanceof MarkerAnnotationExpr annotationExpr) {


			return new MarkerAnnotationExpression(location,
												  annotationExpr.getNameAsString());
		}
		if (expr instanceof NormalAnnotationExpr annotationExpr) {

			NormalAnnotationExpression normalAnnotationExpression = new NormalAnnotationExpression(Locations.from(annotationExpr, indexes), annotationExpr
				.getNameAsString());


			for (MemberValuePair pair : annotationExpr.getPairs()) {
				normalAnnotationExpression = normalAnnotationExpression.addValue(
					pair.getNameAsString(),
					analyzeExpression(pair.getValue(), containingDenomination, analyzerContext)
				);
			}


			return normalAnnotationExpression;
		}
		if (expr instanceof SingleMemberAnnotationExpr singleMemberAnnotationExpr) {

			AnalyzedExpression member = analyzeExpression(singleMemberAnnotationExpr.getMemberValue(), containingDenomination, analyzerContext);

			return new SingleMemberAnnotationExpression(Locations.from(singleMemberAnnotationExpr, indexes), singleMemberAnnotationExpr
				.getNameAsString(), member);
		}

		if (expr instanceof ArrayAccessExpr arrayAccessExpr) {

			AnalyzedExpression index = analyzeExpression(
				arrayAccessExpr.getIndex(), containingDenomination,
				analyzerContext);
			AnalyzedExpression name = analyzeExpression(
				arrayAccessExpr.getName(), containingDenomination,
				analyzerContext);

			return new ArrayAccessExpression(location, name, index);
		}
		if (expr instanceof ArrayCreationExpr arrayCreationExpr) {

			ArrayInitializerExpression initializer = arrayCreationExpr.getInitializer().map(init -> parseInitializer(
				init, containingDenomination,
				analyzerContext)).orElse(null);


			AnalyzedType type = analyzeType(arrayCreationExpr.getElementType());

			ArrayCreationExpression creationExpression = new ArrayCreationExpression(
				location, type, initializer);


			List<ArrayCreationLevel> dimensions = arrayCreationExpr.getLevels();
			if (dimensions != null) {
				for (ArrayCreationLevel level : dimensions) {
					level.getDimension().ifPresent(dimension -> creationExpression
						.addDimension(analyzeExpression(dimension,
														containingDenomination, analyzerContext)));
				}
			}

			return creationExpression;
		}
		if (expr instanceof ArrayInitializerExpr arrayInitializerExpr) {

			return parseInitializer(arrayInitializerExpr,
									containingDenomination, analyzerContext);
		}
		if (expr instanceof AssignExpr assignExpr) {

			AnalyzedExpression value = analyzeExpression(assignExpr.getValue(),
														 containingDenomination, analyzerContext);
			AnalyzedExpression target = analyzeExpression(
				assignExpr.getTarget(), containingDenomination,
				analyzerContext);
			AssignExpression.Operator operator = AssignExpression.Operator
				.values()[assignExpr.getOperator().ordinal()];

			return new AssignExpression(location, target, operator, value);
		}
		if (expr instanceof BinaryExpr binaryExpr) {

			AnalyzedExpression left = analyzeExpression(binaryExpr.getLeft(),
														containingDenomination, analyzerContext);
			AnalyzedExpression right = analyzeExpression(binaryExpr.getRight(),
														 containingDenomination, analyzerContext);
			BinaryExpression.Operator operator = BinaryExpression.Operator
				.values()[binaryExpr.getOperator().ordinal()];

			return new BinaryExpression(location, left, operator, right);
		}
		if (expr instanceof CastExpr castExpr) {

			AnalyzedExpression expression = analyzeExpression(
				castExpr.getExpression(), containingDenomination,
				analyzerContext);
			AnalyzedType type = analyzeType(castExpr.getType());

			return new CastExpression(location, type, expression);

		}
		if (expr instanceof ClassExpr classExpr) {

			return new ClassExpression(location,
									   analyzeType(classExpr.getType()));
		}
		if (expr instanceof ConditionalExpr conditionalExpr) {

			AnalyzedExpression condition = analyzeExpression(
				conditionalExpr.getCondition(), containingDenomination,
				analyzerContext);
			AnalyzedExpression primary = analyzeExpression(
				conditionalExpr.getThenExpr(), containingDenomination,
				analyzerContext);
			AnalyzedExpression alternate = analyzeExpression(
				conditionalExpr.getElseExpr(), containingDenomination,
				analyzerContext);

			return new ConditionalExpression(Locations.from(conditionalExpr, indexes),
											 condition, primary, alternate);
		}
		if (expr instanceof EnclosedExpr enclosedExpr) {

			AnalyzedExpression inner = analyzeExpression(
				enclosedExpr.getInner(), containingDenomination,
				analyzerContext);

			return new EnclosedExpression(Locations.from(enclosedExpr, indexes), inner);
		}
		if (expr instanceof FieldAccessExpr fieldAccessExpr) {

			String fieldName = fieldAccessExpr.getNameAsString();
			Expression scopeExpr = fieldAccessExpr.getScope();
			AnalyzedExpression scope = scopeExpr != null ? analyzeExpression(
				scopeExpr, containingDenomination, analyzerContext) : null;

			FieldAccessExpression fieldAccessExpression = new FieldAccessExpression(
				Locations.from(fieldAccessExpr, indexes), scope, fieldName);


			fieldAccessExpr.getTypeArguments().ifPresent(typeArgs -> {
				for (Type type : typeArgs) {
					AnalyzedType at = analyzeType(type);
					if (at != null) {
						fieldAccessExpression.addTypeArgument(at);
					}
				}
			});

			return fieldAccessExpression;
		}
		if (expr instanceof InstanceOfExpr instanceOfExpr) {

			AnalyzedType target = analyzeType(instanceOfExpr.getType());
			AnalyzedExpression expression = analyzeExpression(
				instanceOfExpr.getExpression(), containingDenomination,
				analyzerContext);

			return new InstanceOfExpression(Locations.from(instanceOfExpr, indexes),
											expression, target);

		}
		if (expr instanceof LambdaExpr lambdaExpr) {

			return new LambdaExpression(Locations.from(lambdaExpr, indexes), analyzeStatement(lambdaExpr.getBody(), containingDenomination, analyzerContext),
										lambdaExpr
											.getParameters()
											.stream()
											.map(p -> analyzeParameter(p, containingDenomination, analyzerContext))
											.collect(Collectors.toList())
			);

		}
		if (expr instanceof MethodReferenceExpr methodReferenceExpr) {

			return new MethodReferenceExpression(
				Locations.from(methodReferenceExpr, indexes), methodReferenceExpr.getIdentifier(), analyzeExpression(methodReferenceExpr
																														 .getScope(), containingDenomination, analyzerContext));
		}
		if (expr instanceof ObjectCreationExpr objectCreationExpr) {

			ClassOrInterfaceType objectType = objectCreationExpr.getType();
			return analyzeClassOrInterface(
				objectType).map(type -> {

				ObjectCreationExpression creationExpression = new ObjectCreationExpression(
					Locations.from(objectCreationExpr, indexes), type);
				objectCreationExpr
					.getScope()
					.ifPresent(scopeExpr -> creationExpression.setScope(analyzeExpression(scopeExpr,
																						  containingDenomination, analyzerContext)));
				List<Expression> args = objectCreationExpr.getArguments();
				if (args != null) {
					for (Expression expression : args) {
						creationExpression.addArgument(analyzeExpression(
							expression, containingDenomination,
							analyzerContext));
					}
				}

				creationExpression.setDiamond(objectType.isUsingDiamondOperator());

				objectType.getTypeArguments().ifPresent(typeArgs -> {
					for (Type t : typeArgs) {
						creationExpression.addTypeArgument(analyzeType(t));
					}
				});
				objectCreationExpr
					.getAnonymousClassBody().ifPresent(anonymousClassBody -> {
					AnalyzerContext innerClassContext = analyzerContext
						.anonymousInnerClass();

					AnalyzedClass innerClass = new AnalyzedClass(containingDenomination.getSourceFile(),
																 Locations.from(objectCreationExpr, indexes), ImmutableList
																	 .of(),
																 analyzerContext.getScope(),
																 locate(objectCreationExpr.getType().getName()));
					containingDenomination.addInnerDenomination(innerClass);

					for (BodyDeclaration<?> bodyDeclaration : anonymousClassBody) {
						analyzeBodyElement(
							Integer.toString(
								innerClassContext.anonymousInnerClassCounter
									- 1),
							innerClass, innerClassContext, bodyDeclaration);
					}
					creationExpression.setDeclaredAnonymousClass(innerClass);
				});

				return creationExpression;
			}).orElseThrow(IllegalStateException::new);
		}
		if (expr instanceof SuperExpr superExpr) {

			LocatedName<Name> classExpression = superExpr
				.getTypeName()
				.map(e -> new LocatedName<>(e, Locations.from(e, indexes)))
				.orElse(null);

			return new SuperExpression(Locations.from(superExpr, indexes),
									   classExpression);
		}
		if (expr instanceof ThisExpr thisExpr) {

			LocatedName<Name> classExpression = thisExpr
				.getTypeName()
				.map(e -> new LocatedName<>(e, Locations.from(e, indexes)))
				.orElse(null);

			return new ThisExpression(Locations.from(thisExpr, indexes), classExpression);
		}
		if (expr instanceof UnaryExpr unaryExpr) {

			AnalyzedExpression expression = analyzeExpression(
				unaryExpr.getExpression(), containingDenomination,
				analyzerContext);
			UnaryExpression.Operator operator = UnaryExpression.Operator
				.values()[unaryExpr.getOperator().ordinal()];

			return new UnaryExpression(Locations.from(unaryExpr, indexes), operator,
									   expression);

		}
		if (expr instanceof VariableDeclarationExpr) {
			return analyzeVariableDeclarationExpression(
				(VariableDeclarationExpr) expr, containingDenomination,
				analyzerContext);
		}
		if (expr instanceof SwitchExpr switchExpr) {

			SwitchExpression switchExpression = new SwitchExpression(Locations.from(switchExpr, indexes), analyzeExpression(switchExpr
																																.getSelector(), containingDenomination, analyzerContext));

			for (SwitchEntry entry : switchExpr.getEntries()) {
				for (SwitchEntryStatement entryStatement : analyzeSwitchEntry(entry, containingDenomination, analyzerContext)) {
					switchExpression = switchExpression.addStatement(entryStatement);
				}
			}

			return switchExpression;
		}

		return new NullExpression(location);
	}

	private VariableDeclarationExpression analyzeVariableDeclarationExpression(
		VariableDeclarationExpr variable,
		ContainingDenomination<?, ?> containingDenomination,
		AnalyzerContext analyzerContext) {

		final List<Modifier.Keyword> modifiers = keywords(Modifier.Keyword.DEFAULT, variable.getModifiers());
		final AnalyzedType type = analyzeType(variable.getElementType());

		VariableDeclarationExpression expression = new VariableDeclarationExpression(
			Locations.from(variable, indexes), modifiers, type);

		List<AnnotationExpr> annotations = variable.getAnnotations();
		if (annotations != null) {
			for (AnnotationExpr annotationExpr : annotations) {
				AnalyzedAnnotation annotation = analyze(annotationExpr,
														containingDenomination, analyzerContext);

				expression.addAnnotation(annotation);
			}
		}

		List<VariableDeclarator> vars = variable.getVariables();
		if (vars != null) {
			for (VariableDeclarator var : vars) {
				AnalyzedExpression init = var
					.getInitializer()
					.map(e -> analyzeExpression(e, containingDenomination, analyzerContext))
					.orElse(null);

				expression.addDeclareVariable(new DeclareVariableExpression(
					Locations.from(var, indexes), var.getNameAsString(), init));
			}
		}

		return expression;
	}

	@NotNull
	private ArrayInitializerExpression parseInitializer(
		@NotNull ArrayInitializerExpr initializer,
		ContainingDenomination<?, ?> containingDenomination,
		AnalyzerContext analyzerContext) {
		ArrayInitializerExpression expression = new ArrayInitializerExpression(
			Locations.from(initializer, indexes));

		for (Expression expr : initializer.getValues()) {
			expression.addValue(analyzeExpression(expr,
												  containingDenomination, analyzerContext));
		}

		return expression;
	}

	private AnalyzedType analyzeType(Type type) {
		final Location location = Locations.from(type, indexes);

		if (type.isArrayType()) {
			return new ArrayType(location, analyzeType(type.asArrayType().getComponentType()));
		}

		return analyzeBaseType(type, location);
	}

	private AnalyzedType analyzeBaseType(Type type, Location location) {
		if (type instanceof ClassOrInterfaceType classOrInterface) {

			return analyzeClassOrInterface(classOrInterface).orElseThrow(IllegalStateException::new);
		}

		if (type instanceof PrimitiveType prim) {

			return new Primitive(location,
								 Primitive.PrimitiveType.fromParserType(prim.getType()));
		}
		if (type instanceof ReferenceType ref) {

			Reference rt = processReferenceType(location, ref);
			if (rt != null) {
				return rt;
			}
		}
		if (type instanceof WildcardType wildcard) {

			Optional<ReferenceType> superType = wildcard.getSuperType();
			Reference superRef = superType.map(rt -> processReferenceType(Locations.from(rt, indexes), rt))
										  .orElse(null);

			Reference extendsRef = wildcard
				.getExtendedType()
				.map(et -> processReferenceType(Locations.from(et, indexes), et))
				.orElse(null);

			return new Wildcard(location, superRef, extendsRef);
		}

		return new TypeVoid(location);
	}

	private Reference processReferenceType(final Location location,
										   ReferenceType ref) {
		AnalyzedType refType = analyzeType(ref.getElementType());

		if (refType != null) {
			return new Reference(location, refType, ref.getArrayLevel());
		}

		return null;
	}

	private Optional<ClassOrInterface> analyzeClassOrInterface(
		@Nullable ClassOrInterfaceType classOrInterface) {
		if (classOrInterface == null)
			return Optional.empty();

		return
			classOrInterface.getScope().flatMap(this::analyzeClassOrInterface).map(scope -> {

				List<AnalyzedType> analyzedTypeArguments = Lists.newArrayList();
				classOrInterface.getTypeArguments().ifPresent(typeArgs -> {
					for (Type subtype : typeArgs) {
						AnalyzedType at = analyzeType(subtype);

						analyzedTypeArguments.add(at);
					}
				});

				return new ClassOrInterface(Locations.from(classOrInterface, indexes),
											locate(classOrInterface.getName()), scope, analyzedTypeArguments);
			}).or(() -> {
				List<AnalyzedType> analyzedTypeArguments = Lists.newArrayList();
				classOrInterface.getTypeArguments().ifPresent(typeArgs -> {
					for (Type subtype : typeArgs) {
						AnalyzedType at = analyzeType(subtype);

						analyzedTypeArguments.add(at);
					}
				});

				return Optional.of(new ClassOrInterface(Locations.from(classOrInterface, indexes),
														locate(classOrInterface.getName()), null, analyzedTypeArguments));
			});
	}

	private LocatedName<SimpleName> locate(SimpleName name) {
		return new LocatedName<>(name, Locations.from(name, indexes));
	}

	public interface IStatementAssigner {
		void assignStatement(@NotNull AnalyzedStatement<?, ?> statement);
	}

	private static class AnalyzerContext {
		private int anonymousInnerClassCounter = 0;

		private final String currentScope;

		private final AnalyzerContext parent;

		private AnalyzerContext(String currentScope, AnalyzerContext parent) {
			this.currentScope = currentScope;
			this.parent = parent;
		}

		public String getScope() {
			if (parent == null) {
				return currentScope;
			}

			return String.format("%s.%s", parent.getScope(), currentScope);
		}

		public AnalyzerContext anonymousInnerClass() {
			return new AnalyzerContext(
				String.format("$%d", ++anonymousInnerClassCounter), this);
		}

		public AnalyzerContext innerDeclaration(Node name) {
			return new AnalyzerContext(name.toString(), this);
		}

	}
}
