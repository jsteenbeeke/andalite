/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.ParseException;
import com.github.antlrjavaparser.ParserConfigurator;
import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.ImportDeclaration;
import com.github.antlrjavaparser.api.Node;
import com.github.antlrjavaparser.api.PackageDeclaration;
import com.github.antlrjavaparser.api.body.AnnotationDeclaration;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
import com.github.antlrjavaparser.api.body.ConstructorDeclaration;
import com.github.antlrjavaparser.api.body.EnumConstantDeclaration;
import com.github.antlrjavaparser.api.body.EnumDeclaration;
import com.github.antlrjavaparser.api.body.FieldDeclaration;
import com.github.antlrjavaparser.api.body.JavadocComment;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.ModifierSet;
import com.github.antlrjavaparser.api.body.Parameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.body.TypeDeclaration;
import com.github.antlrjavaparser.api.body.VariableDeclarator;
import com.github.antlrjavaparser.api.expr.AnnotationExpr;
import com.github.antlrjavaparser.api.expr.ArrayAccessExpr;
import com.github.antlrjavaparser.api.expr.ArrayCreationExpr;
import com.github.antlrjavaparser.api.expr.ArrayInitializerExpr;
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.BinaryExpr;
import com.github.antlrjavaparser.api.expr.BooleanLiteralExpr;
import com.github.antlrjavaparser.api.expr.CastExpr;
import com.github.antlrjavaparser.api.expr.CharLiteralExpr;
import com.github.antlrjavaparser.api.expr.ClassExpr;
import com.github.antlrjavaparser.api.expr.ConditionalExpr;
import com.github.antlrjavaparser.api.expr.DoubleLiteralExpr;
import com.github.antlrjavaparser.api.expr.EnclosedExpr;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.FieldAccessExpr;
import com.github.antlrjavaparser.api.expr.InstanceOfExpr;
import com.github.antlrjavaparser.api.expr.IntegerLiteralExpr;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.LongLiteralExpr;
import com.github.antlrjavaparser.api.expr.MarkerAnnotationExpr;
import com.github.antlrjavaparser.api.expr.MemberValuePair;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.expr.NameExpr;
import com.github.antlrjavaparser.api.expr.NormalAnnotationExpr;
import com.github.antlrjavaparser.api.expr.NullLiteralExpr;
import com.github.antlrjavaparser.api.expr.ObjectCreationExpr;
import com.github.antlrjavaparser.api.expr.SingleMemberAnnotationExpr;
import com.github.antlrjavaparser.api.expr.StringLiteralExpr;
import com.github.antlrjavaparser.api.expr.SuperExpr;
import com.github.antlrjavaparser.api.expr.ThisExpr;
import com.github.antlrjavaparser.api.expr.UnaryExpr;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
import com.github.antlrjavaparser.api.stmt.AssertStmt;
import com.github.antlrjavaparser.api.stmt.BlockStmt;
import com.github.antlrjavaparser.api.stmt.BreakStmt;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.stmt.ContinueStmt;
import com.github.antlrjavaparser.api.stmt.DoStmt;
import com.github.antlrjavaparser.api.stmt.EmptyStmt;
import com.github.antlrjavaparser.api.stmt.ExplicitConstructorInvocationStmt;
import com.github.antlrjavaparser.api.stmt.ExpressionStmt;
import com.github.antlrjavaparser.api.stmt.ForStmt;
import com.github.antlrjavaparser.api.stmt.ForeachStmt;
import com.github.antlrjavaparser.api.stmt.IfStmt;
import com.github.antlrjavaparser.api.stmt.LabeledStmt;
import com.github.antlrjavaparser.api.stmt.ReturnStmt;
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.SwitchEntryStmt;
import com.github.antlrjavaparser.api.stmt.SwitchStmt;
import com.github.antlrjavaparser.api.stmt.SynchronizedStmt;
import com.github.antlrjavaparser.api.stmt.ThrowStmt;
import com.github.antlrjavaparser.api.stmt.TryStmt;
import com.github.antlrjavaparser.api.stmt.TypeDeclarationStmt;
import com.github.antlrjavaparser.api.stmt.WhileStmt;
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;
import com.github.antlrjavaparser.api.type.PrimitiveType;
import com.github.antlrjavaparser.api.type.ReferenceType;
import com.github.antlrjavaparser.api.type.Type;
import com.github.antlrjavaparser.api.type.WildcardType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BooleanValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.CharValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ClassValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.DoubleValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.FieldAccessValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.IntegerValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.LongValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.AnnotationExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ArrayAccessExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ArrayCreationExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ArrayInitializerExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.AssignExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.BinaryExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.BooleanLiteralExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.CastExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.CharLiteralExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ClassExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ConditionalExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.DeclareVariableExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.DoubleLiteralExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.EnclosedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.FieldAccessExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.InstanceOfExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.IntegerLiteralExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.LambdaExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.LongLiteralExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.MethodCallExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.MethodReferenceExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.NameReferenceExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.NullExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ObjectCreationExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.StringLiteralExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.SuperExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.ThisExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.UnaryExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.VariableDeclarationExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.AssertStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BreakStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.CatchStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ConstructorInvocationStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ConstructorInvocationStatement.InvocationType;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ContinueStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.DoStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.EmptyStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ExpressionStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ForEachStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ForStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.LabeledStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ResourceStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.SwitchEntryStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.SwitchStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.SynchronizedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ThrowStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.TryStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.TypeDeclarationStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.WhileStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.types.ClassOrInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.types.Primitive;
import com.jeroensteenbeeke.andalite.java.analyzer.types.Reference;
import com.jeroensteenbeeke.andalite.java.analyzer.types.TypeVoid;
import com.jeroensteenbeeke.andalite.java.analyzer.types.Wildcard;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

/**
 * Java class analyzer. Parses a file and then interprets the abstract syntax
 * tree, transforming it into
 * {@code Locatable} elements.
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class ClassAnalyzer {
	private static final Logger log = LoggerFactory
			.getLogger(ClassAnalyzer.class);

	private final File targetFile;

	public ClassAnalyzer(@Nonnull File targetFile) {
		super();
		this.targetFile = targetFile;
	}

	@Nonnull
	public TypedActionResult<AnalyzedSourceFile> analyze() {
		log.debug("Starting analysis of {}", targetFile.getAbsolutePath());

		RecordingBailErrorStrategy errorStrategy = new RecordingBailErrorStrategy(
				targetFile);

		try {
			CompilationUnit compilationUnit = JavaParser.parse(
					new FileInputStream(targetFile), new ParserConfigurator() {
						public void configure(Java8Parser parser) {
							parser.setErrorHandler(errorStrategy);
						}
					});

			compilationUnit.accept(new DebugVisitor(), log);

			PackageDeclaration packageDefinition = compilationUnit.getPackage();

			final String packageName = determinePackageName(packageDefinition);

			AnalyzerContext context = new AnalyzerContext(packageName, null);

			AnalyzedSourceFile sourceFile = new AnalyzedSourceFile(
					Location.from(compilationUnit), targetFile, packageName,
					Location.from(packageDefinition));

			for (ImportDeclaration importDeclaration : compilationUnit
					.getImports()) {
				AnalyzedImport imp = new AnalyzedImport(
						Location.from(importDeclaration),
						importDeclaration.getName().toString(),
						importDeclaration.isStatic(),
						importDeclaration.isAsterisk());

				sourceFile.addImport(imp);
			}

			for (TypeDeclaration typeDeclaration : compilationUnit.getTypes()) {
				analyze(sourceFile, context, typeDeclaration);
			}

			return TypedActionResult.ok(sourceFile);
		} catch (ParseCancellationException e) {
			log.error(e.getMessage(), e);
			String exceptionMessage = errorStrategy.getExceptionMessage();
			if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
				return TypedActionResult.fail(exceptionMessage);
			}
			return TypedActionResult.fail("Could not parse %s",
					targetFile.getAbsolutePath());
		} catch (ParseException | IOException e) {
			String exceptionMessage = errorStrategy.getExceptionMessage();
			if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
				return TypedActionResult.fail(exceptionMessage);
			}
			return TypedActionResult.fail(e.getMessage());
		}
	}

	private Denomination analyze(@CheckForNull AnalyzedSourceFile sourceFile,
			@Nonnull AnalyzerContext context,
			@Nonnull TypeDeclaration typeDeclaration) {
		if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration decl = (ClassOrInterfaceDeclaration) typeDeclaration;

			if (decl.isInterface()) {
				AnalyzedInterface element = new AnalyzedInterface(
						Location.from(decl), decl.getModifiers(),
						context.getScope(), decl.getName());
				element.setSourceFile(sourceFile);

				processInterfaceDeclaration(decl,
						context.innerDeclaration(decl.getName()), element);

				if (sourceFile != null) {
					sourceFile.addInterface(element);
				}

				return setJavadoc(decl, element);
			} else {
				AnalyzedClass element = new AnalyzedClass(Location.from(decl),
						decl.getModifiers(), context.getScope(),
						decl.getName());
				element.setSourceFile(sourceFile);

				processClassDeclaration(decl,
						context.innerDeclaration(decl.getName()), element);

				if (sourceFile != null) {
					sourceFile.addClass(element);
				}

				return setJavadoc(decl, element);
			}
		} else if (typeDeclaration instanceof EnumDeclaration) {
			EnumDeclaration decl = (EnumDeclaration) typeDeclaration;

			AnalyzedEnum element = new AnalyzedEnum(Location.from(decl),
					decl.getModifiers(), context.getScope(), decl.getName());
			element.setSourceFile(sourceFile);

			processEnumDeclaration(decl,
					context.innerDeclaration(decl.getName()), element);

			if (sourceFile != null) {
				sourceFile.addEnum(element);
			}

			return setJavadoc(decl, element);
		} else if (typeDeclaration instanceof AnnotationDeclaration) {
			AnnotationDeclaration decl = (AnnotationDeclaration) typeDeclaration;

			AnalyzedAnnotationType element = new AnalyzedAnnotationType(
					Location.from(decl), decl.getModifiers(),
					context.getScope(), decl.getName());
			element.setSourceFile(sourceFile);

			processAnnotationDeclaration(decl, element);

			if (sourceFile != null) {
				sourceFile.addAnnotation(element);
			}

			return setJavadoc(decl, element);
		}

		return null;
	}

	private void processAnnotationDeclaration(AnnotationDeclaration decl,
			AnalyzedAnnotationType element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
					new AnalyzerContext(element.getAnnotationName(), null));
			if (annotation != null) {
				element.addAnnotation(annotation);
			}
		}

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

		TerminalNode separator = decl.getConstantAndBodySeparator();

		if (separator != null) {
			element.setSeparatorLocation(Location.from(separator));
		}

		List<ClassOrInterfaceType> implementedInterfaces = decl.getImplements();
		if (implementedInterfaces != null) {
			for (ClassOrInterfaceType type : implementedInterfaces) {
				element.addInterface(type.getNameAsString());
				element.setLastImplementsLocation(Location.from(type));
			}
		}

		final int classEndMinusOne = decl.getEndIndex() - 1;
		int start = classEndMinusOne;
		final int end = classEndMinusOne;

		List<BodyDeclaration> members = decl.getMembers();

		if (members != null) {
			for (BodyDeclaration member : members) {
				if (start == classEndMinusOne) {
					start = member.getBeginIndex();
				}
				analyzeBodyElement(decl.getName().getText(), element,
						analyzerContext, member);
			}
		}

		List<EnumConstantDeclaration> entries = decl.getEntries();
		if (entries != null) {
			for (EnumConstantDeclaration constDecl : entries) {
				if (constDecl != null) {
					analyzeEnumConstant(element, analyzerContext, constDecl);
				}
			}
		}

		element.setBodyLocation(new Location(start, end));
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

		final int classEndMinusOne = decl.getEndIndex() - 1;
		int start = classEndMinusOne;
		final int end = classEndMinusOne;

		List<BodyDeclaration> members = decl.getClassBody();

		if (members != null) {
			for (BodyDeclaration member : members) {
				if (start == classEndMinusOne) {
					start = member.getBeginIndex();
				}
				analyzeBodyElement(null, element, analyzerContext, member);
			}
		}

		element.setBodyLocation(new Location(start, end));
	}

	private void analyzeBodyElement(String typeName,
			ContainingDenomination element, AnalyzerContext analyzerContext,
			BodyDeclaration member) {
		if (member instanceof FieldDeclaration) {
			List<AnalyzedField> fields = analyze((FieldDeclaration) member,
					element, analyzerContext);
			for (AnalyzedField field : fields) {
				element.addField(field);
			}
		} else if (member instanceof MethodDeclaration) {
			AnalyzedMethod method = analyze((MethodDeclaration) member, element,
					analyzerContext);

			if (method != null) {
				if (element.isAutoAbstractMethods()) {
					method.setAbstract(true);
				}

				element.addMethod(method);
			}
		} else if (member instanceof EnumConstantDeclaration) {
			if (element instanceof AnalyzedEnum) {
				EnumConstantDeclaration enumDecl = (EnumConstantDeclaration) member;

				analyzeEnumConstant(element, analyzerContext, enumDecl);
			}

		} else if (member instanceof ConstructorDeclaration) {
			AnalyzedConstructor constr = analyze(typeName,
					(ConstructorDeclaration) member, element, analyzerContext);
			if (constr != null) {
				addConstructor(element, constr);
			}
		} else if (member instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration innerClassDecl = (ClassOrInterfaceDeclaration) member;

			if (innerClassDecl.isInterface()) {
				AnalyzedInterface innerInterface = new AnalyzedInterface(
						Location.from(innerClassDecl),
						innerClassDecl.getModifiers(),
						String.format("%s.%s", element.getPackageName(),
								element.getDenominationName()),
						innerClassDecl.getName());
				innerInterface.setSourceFile(element.getSourceFile());

				processInterfaceDeclaration(innerClassDecl, analyzerContext
						.innerDeclaration(innerClassDecl.getName()),
						innerInterface);

				element.addInnerDenomination(innerInterface);

			} else {
				AnalyzedClass innerClass = new AnalyzedClass(
						Location.from(innerClassDecl),
						innerClassDecl.getModifiers(),
						String.format("%s.%s", element.getPackageName(),
								element.getDenominationName()),
						innerClassDecl.getName());
				innerClass.setSourceFile(element.getSourceFile());

				processClassDeclaration(innerClassDecl, analyzerContext
						.innerDeclaration(innerClassDecl.getName()),
						innerClass);

				element.addInnerDenomination(innerClass);
			}

		} else if (member instanceof EnumDeclaration) {
			EnumDeclaration decl = (EnumDeclaration) member;

			AnalyzedEnum elem = new AnalyzedEnum(Location.from(decl),
					decl.getModifiers(), analyzerContext.getScope(),
					decl.getName());

			processEnumDeclaration(decl,
					analyzerContext.innerDeclaration(decl.getName()), elem);

			element.addInnerDenomination(elem);
		} else if (member instanceof AnnotationDeclaration) {
			AnnotationDeclaration decl = (AnnotationDeclaration) member;

			AnalyzedAnnotationType elem = new AnalyzedAnnotationType(
					Location.from(decl), decl.getModifiers(),
					analyzerContext.getScope(), decl.getName());

			processAnnotationDeclaration(decl, elem);

			element.addInnerDenomination(elem);
		}
	}

	private void analyzeEnumConstant(ContainingDenomination element,
			AnalyzerContext analyzerContext, EnumConstantDeclaration enumDecl) {
		List<Expression> args = enumDecl.getArgs();
		List<AnalyzedExpression> paramExpressions = Lists.newLinkedList();
		if (args != null) {
			for (Expression expression : args) {
				paramExpressions.add(analyzeExpression(expression, element,
						analyzerContext));
			}

		}

		AnalyzedEnumConstant constant = new AnalyzedEnumConstant(
				Location.from(enumDecl), ModifierSet.PUBLIC,
				String.format("%s.%s", element.getPackageName(),
						element.getDenominationName()),
				enumDecl.getName(), paramExpressions);

		processEnumConstantDeclaration(enumDecl, analyzerContext, constant);

		((AnalyzedEnum) element).addConstant(constant);
	}

	private void addConstructor(ContainingDenomination element,
			AnalyzedConstructor constr) {
		if (element instanceof ConstructableDenomination) {
			ConstructableDenomination constructable = (ConstructableDenomination) element;

			constructable.addConstructor(constr);
		}
	}

	private void processClassDeclaration(ClassOrInterfaceDeclaration decl,
			AnalyzerContext analyzerContext, AnalyzedClass element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
					analyzerContext);
			if (annotation != null) {
				element.addAnnotation(annotation);
			}
		}

		final int classEndMinusOne = decl.getEndIndex() - 1;
		int start = classEndMinusOne;
		final int end = classEndMinusOne;

		List<ClassOrInterfaceType> extendedClasses = decl.getExtends();
		if (extendedClasses != null) {
			for (ClassOrInterfaceType type : extendedClasses) {
				element.setSuperClass(getTypeNameWithGenerics(type));
				element.setExtendsLocation(Location.from(type));

				break; // Classes only have single inheritance
			}
		}

		List<ClassOrInterfaceType> implementedInterfaces = decl.getImplements();
		if (implementedInterfaces != null) {

			for (ClassOrInterfaceType type : implementedInterfaces) {
				element.addInterface(getTypeNameWithGenerics(type));
				element.setLastImplementsLocation(Location.from(type));
			}
		}

		List<BodyDeclaration> members = decl.getMembers();
		if (members != null) {
			for (BodyDeclaration member : members) {
				if (start == classEndMinusOne) {
					start = member.getBeginIndex();
				}
				analyzeBodyElement(decl.getNameAsString(), element,
						analyzerContext, member);
			}
		}

		element.setBodyLocation(new Location(start, end));
	}

	private String getTypeNameWithGenerics(
			@Nullable ClassOrInterfaceType type) {
		if (type == null) {
			return null;
		}
		List<Type> typeArgs = type.getTypeArgs();
		String typeName = type.getNameAsString();
		ClassOrInterfaceType scope = type.getScope();
		String prefix = "";
		if (scope != null) {
			prefix = getTypeNameWithGenerics(scope).concat(".");
		}

		if (typeArgs != null && !typeArgs.isEmpty()) {
			return prefix.concat(
					typeName.concat(typeArgs.stream().map(t -> t.toString())
							.collect(Collectors.joining(", ", "<", ">"))));
		}

		return prefix.concat(typeName);
	}

	private void processInterfaceDeclaration(ClassOrInterfaceDeclaration decl,
			AnalyzerContext analyzerContext, AnalyzedInterface element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr, element,
					analyzerContext);
			if (annotation != null) {
				element.addAnnotation(annotation);
			}
		}

		final int classEndMinusOne = decl.getEndIndex() - 1;
		int start = classEndMinusOne;
		final int end = classEndMinusOne;

		List<ClassOrInterfaceType> extendedClasses = decl.getExtends();
		if (extendedClasses != null) {
			for (ClassOrInterfaceType type : extendedClasses) {
				element.addInterface(getTypeNameWithGenerics(type));
				element.setLastImplementsLocation(Location.from(type));
			}
		}

		for (BodyDeclaration member : decl.getMembers()) {
			if (start == classEndMinusOne) {
				start = member.getBeginIndex();
			}
			analyzeBodyElement(decl.getNameAsString(), element, analyzerContext,
					member);
		}

		element.setBodyLocation(new Location(start, end));
	}

	private AnalyzedConstructor analyze(String className,
			ConstructorDeclaration member,
			@Nonnull ContainingDenomination containingDenomination,
			@Nonnull AnalyzerContext analyzerContext) {

		AnalyzedConstructor constructor = new AnalyzedConstructor(
				Location.from(member), className, member.getModifiers(),
				Location.from(member.getParametersStart()));

		constructor.addAnnotations(determineAnnotations(member,
				containingDenomination, analyzerContext));

		List<Parameter> parameters = member.getParameters();
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				AnalyzedParameter analyzedParameter = analyze(parameter,
						containingDenomination, analyzerContext);
				if (analyzedParameter != null) {
					constructor.addParameter(analyzedParameter);
				}
			}
		}

		BlockStmt blockStmt = member.getBlock();
		if (blockStmt != null) {
			BlockStatement statement = analyzeBlockStatement(blockStmt,
					containingDenomination, analyzerContext);
			if (statement != null) {
				List<AnalyzedStatement> body = statement.getStatements();
				if (body != null) {
					body.forEach(constructor::addStatement);
				}
			}
		}

		return constructor;
	}

	private AnalyzedMethod analyze(MethodDeclaration member,
			@Nonnull ContainingDenomination containingDenomination,
			@Nonnull AnalyzerContext analyzerContext) {
		final AnalyzedMethod method = new AnalyzedMethod(Location.from(member),
				analyzeType(member.getType()), member.getModifiers(),
				member.getName());

		method.addAnnotations(determineAnnotations(member,
				containingDenomination, analyzerContext));

		List<Parameter> parameters = member.getParameters();
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				AnalyzedParameter analyzedParameter = analyze(parameter,
						containingDenomination, analyzerContext);
				if (analyzedParameter != null) {
					method.addParameter(analyzedParameter);
				}
			}
		}

		List<NameExpr> thrown = member.getThrows();
		if (thrown != null) {
			thrown.stream().filter(Objects::nonNull).forEach(nameEx -> {
				method.addThrownException(new AnalyzedThrownException(
						Location.from(nameEx), nameEx.getName()));

			});
		}

		BlockStmt body = member.getBody();
		if (body != null) {
			analyzeBodyDeclaration(body, new IStatementAssigner() {
				@Override
				public void assignStatement(AnalyzedStatement statement) {
					method.addStatement(statement);
				}
			}, containingDenomination, analyzerContext);
		}

		TerminalNode rparen = member.getRightParenthesis();
		if (rparen != null) {
			method.setRightParenthesisLocation(Location.from(rparen));
		}

		return addComments(member, setJavadoc(member, method));
	}

	private AnalyzedParameter analyze(Parameter parameter,
			@Nonnull ContainingDenomination containingDenomination,
			@Nonnull AnalyzerContext analyzerContext) {
		AnalyzedParameter param = new AnalyzedParameter(
				Location.from(parameter), parameter.getType().toString(),
				parameter.getId().getName());

		for (AnnotationExpr annotationExpr : parameter.getAnnotations()) {
			AnalyzedAnnotation annot = analyze(annotationExpr,
					containingDenomination, analyzerContext);
			if (annot != null) {
				param.addAnnotation(annot);
			}
		}

		return param;
	}

	private List<AnalyzedField> analyze(FieldDeclaration member,
			@Nonnull ContainingDenomination containingDenomination,
			@Nonnull AnalyzerContext analyzerContext) {
		final AnalyzedType type = analyzeType(member.getType());
		final int modifiers = member.getModifiers();

		Builder<AnalyzedField> builder = ImmutableList.builder();
		List<AnalyzedAnnotation> annotations = determineAnnotations(member,
				containingDenomination, analyzerContext);

		for (VariableDeclarator var : member.getVariables()) {
			String name = var.getId().getName();

			AnalyzedField analyzedField = new AnalyzedField(
					Location.from(member), modifiers, name, type);
			analyzedField.setSpecificDeclarationLocation(Location.from(var));
			analyzedField.addAnnotations(annotations);

			Expression init = var.getInit();
			if (init != null) {
				analyzedField.setInitializationExpression(analyzeExpression(
						init, containingDenomination, analyzerContext));
			}

			builder.add(analyzedField);

		}

		return builder.build();
	}

	private List<AnalyzedAnnotation> determineAnnotations(
			BodyDeclaration member,
			@Nonnull ContainingDenomination containingDenomination,
			@Nonnull AnalyzerContext analyzerContext) {
		Builder<AnalyzedAnnotation> annot = ImmutableList.builder();

		for (AnnotationExpr annotExpr : member.getAnnotations()) {
			AnalyzedAnnotation analyzedAnnotation = analyze(annotExpr,
					containingDenomination, analyzerContext);
			if (analyzedAnnotation != null) {
				annot.add(analyzedAnnotation);
			}
		}

		List<AnalyzedAnnotation> annotations = annot.build();
		return annotations;
	}

	private AnalyzedAnnotation analyze(AnnotationExpr expr,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		AnalyzedAnnotation annotation = new AnalyzedAnnotation(
				Location.from(expr), expr.getName().toString());

		if (expr instanceof SingleMemberAnnotationExpr) {
			SingleMemberAnnotationExpr annot = (SingleMemberAnnotationExpr) expr;

			Expression memberValue = annot.getMemberValue();

			assignValue(annotation, "value", memberValue,
					containingDenomination, analyzerContext);

			annotation.setParametersLocation(Location.from(memberValue));
		} else if (expr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr annot = (NormalAnnotationExpr) expr;
			List<MemberValuePair> pairs = annot.getPairs();

			int start = annot.getEndIndex() + 1;
			int end = annot.getEndIndex() + 1;

			if (pairs != null) {
				boolean first = true;

				for (MemberValuePair mvp : pairs) {
					if (first) {
						first = false;
						start = mvp.getBeginIndex();
					}

					String name = mvp.getName();
					Expression memberValue = mvp.getValue();

					assignValue(annotation, name, memberValue,
							containingDenomination, analyzerContext);

					end = mvp.getEndIndex();
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
			Expression expr, ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		BaseValue<?> value = translateExpression(name, expr,
				containingDenomination, analyzerContext);

		if (value != null) {
			annotation.addAnnotation(value);
		}

	}

	private BaseValue<?> translateExpression(String name, Expression expr,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		if (expr instanceof AnnotationExpr) {
			AnnotationExpr annot = (AnnotationExpr) expr;
			AnalyzedAnnotation sub = analyze(annot, containingDenomination,
					analyzerContext);

			return new AnnotationValue(Location.from(expr), name, sub);
		} else if (expr instanceof BooleanLiteralExpr) {
			BooleanLiteralExpr bool = (BooleanLiteralExpr) expr;

			return new BooleanValue(Location.from(expr), name, bool.getValue());
		} else if (expr instanceof CharLiteralExpr) {
			CharLiteralExpr charLit = (CharLiteralExpr) expr;

			return new CharValue(Location.from(expr), name,
					charLit.getValue().charAt(0));

		} else if (expr instanceof DoubleLiteralExpr) {
			DoubleLiteralExpr doubLit = (DoubleLiteralExpr) expr;

			return new DoubleValue(Location.from(expr), name,
					Double.parseDouble(doubLit.getValue()));
		} else if (expr instanceof IntegerLiteralExpr) {
			IntegerLiteralExpr intLit = (IntegerLiteralExpr) expr;

			return new IntegerValue(Location.from(expr), name,
					Integer.parseInt(intLit.getValue()));
		} else if (expr instanceof LongLiteralExpr) {
			LongLiteralExpr longLit = (LongLiteralExpr) expr;

			return new LongValue(Location.from(expr), name,
					Long.parseLong(longLit.getValue()));
		} else if (expr instanceof StringLiteralExpr) {
			StringLiteralExpr str = (StringLiteralExpr) expr;

			return new StringValue(Location.from(expr), name, str.getValue());
		} else if (expr instanceof ClassExpr) {
			ClassExpr classExpr = (ClassExpr) expr;

			return new ClassValue(Location.from(expr), name,
					classExpr.getType().toString());

		} else if (expr instanceof ArrayInitializerExpr) {
			ArrayInitializerExpr array = (ArrayInitializerExpr) expr;

			Builder<BaseValue<?>> builder = ImmutableList.builder();
			for (Expression expression : array.getValues()) {
				BaseValue<?> val = translateExpression(null, expression,
						containingDenomination, analyzerContext);
				if (val != null) {
					builder.add(val);
				}
			}

			return new ArrayValue(Location.from(expr), name, builder.build());
		} else if (expr instanceof FieldAccessExpr) {
			// Enum field reference or similar, just turn it into a big string,
			// ignore type params (shouldn't be there in enums)
			FieldAccessExpr fieldAccess = (FieldAccessExpr) expr;

			final String scope = analyzeExpression(fieldAccess.getScope(),
					containingDenomination, analyzerContext).toJavaString();
			final String field = fieldAccess.getField();

			return new FieldAccessValue(Location.from(expr), name, scope,
					field);

		}

		return null;
	}

	private String determinePackageName(PackageDeclaration pkg) {
		return pkg != null ? AnalyzeUtil.getQualifiedName(pkg.getName()) : "";
	}

	private void analyzeBodyDeclaration(@Nonnull final BlockStmt body,
			@Nonnull final IStatementAssigner assigner,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		List<Statement> statements = body.getStmts();
		if (statements != null) {
			for (Statement statement : statements) {
				assigner.assignStatement(analyzeStatement(statement,
						containingDenomination, analyzerContext));
			}
		}

	}

	@CheckForNull
	private AnalyzedStatement analyzeStatement(Statement statement,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		if (statement == null) {
			return null;
		}

		Location location = Location.from(statement);
		if (statement instanceof ReturnStmt) {
			ReturnStmt returnStmt = (ReturnStmt) statement;

			Expression expr = returnStmt.getExpr();
			AnalyzedExpression returnExpression = null;
			if (expr != null) {
				returnExpression = analyzeExpression(expr,
						containingDenomination, analyzerContext);
			}

			return addComments(statement,
					new ReturnStatement(location, returnExpression));

		} else if (statement instanceof IfStmt) {
			IfStmt ifStmt = (IfStmt) statement;

			AnalyzedExpression condition = analyzeExpression(
					ifStmt.getCondition(), containingDenomination,
					analyzerContext);
			AnalyzedStatement thenStatement = analyzeStatement(
					ifStmt.getThenStmt(), containingDenomination,
					analyzerContext);

			IfStatement ifStatement = new IfStatement(location, condition,
					thenStatement);

			ifStatement.setElseStatement(analyzeStatement(ifStmt.getElseStmt(),
					containingDenomination, analyzerContext));

			return addComments(statement, ifStatement);

		} else if (statement instanceof AssertStmt) {
			AssertStmt assertStmt = (AssertStmt) statement;

			return addComments(statement,
					new AssertStatement(Location.from(statement),
							analyzeExpression(assertStmt.getCheck(),
									containingDenomination, analyzerContext)));
		} else if (statement instanceof BlockStmt) {
			BlockStmt blockStmt = (BlockStmt) statement;

			return addComments(statement, analyzeBlockStatement(blockStmt,
					containingDenomination, analyzerContext));

		} else if (statement instanceof BreakStmt) {
			BreakStmt breakStmt = (BreakStmt) statement;

			return addComments(statement, new BreakStatement(
					Location.from(breakStmt), breakStmt.getId()));

		} else if (statement instanceof ContinueStmt) {
			ContinueStmt continueStmt = (ContinueStmt) statement;

			return addComments(statement, new ContinueStatement(
					Location.from(continueStmt), continueStmt.getId()));
		} else if (statement instanceof DoStmt) {
			DoStmt doStmt = (DoStmt) statement;

			AnalyzedExpression condition = analyzeExpression(
					doStmt.getCondition(), containingDenomination,
					analyzerContext);
			AnalyzedStatement body = analyzeStatement(doStmt.getBody(),
					containingDenomination, analyzerContext);

			return addComments(statement,
					new DoStatement(Location.from(doStmt), condition, body));
		} else if (statement instanceof EmptyStmt) {
			return new EmptyStatement(Location.from(statement));
		} else if (statement instanceof ExplicitConstructorInvocationStmt) {
			ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt = (ExplicitConstructorInvocationStmt) statement;

			InvocationType invocationType = explicitConstructorInvocationStmt
					.isThis() ? InvocationType.THIS : InvocationType.SUPER;

			ConstructorInvocationStatement invocation = new ConstructorInvocationStatement(
					Location.from(explicitConstructorInvocationStmt),
					invocationType);

			Expression expr = explicitConstructorInvocationStmt.getExpr();
			if (expr != null) {
				AnalyzedExpression scopeExpression = analyzeExpression(expr,
						containingDenomination, analyzerContext);
				invocation.setScope(scopeExpression);
			}

			List<Type> typeArgs = explicitConstructorInvocationStmt
					.getTypeArgs();
			if (typeArgs != null) {
				for (Type type : typeArgs) {
					if (type != null) {
						AnalyzedType analyzedType = analyzeType(type);
						if (analyzedType != null) {
							invocation.addTypeArgument(analyzedType);
						}
					}
				}
			}

			List<Expression> args = explicitConstructorInvocationStmt.getArgs();
			if (args != null) {
				for (Expression expression : args) {
					if (expression != null) {
						AnalyzedExpression analyzedExpression = analyzeExpression(
								expression, containingDenomination,
								analyzerContext);
						if (analyzedExpression != null) {
							invocation.addArgument(analyzedExpression);
						}
					}
				}
			}

			return addComments(statement, invocation);
		} else if (statement instanceof ExpressionStmt) {
			ExpressionStmt expr = (ExpressionStmt) statement;
			AnalyzedExpression e = analyzeExpression(expr.getExpression(),
					containingDenomination, analyzerContext);

			return addComments(statement,
					new ExpressionStatement(Location.from(expr), e));

		} else if (statement instanceof ForeachStmt) {
			ForeachStmt foreachStmt = (ForeachStmt) statement;

			AnalyzedStatement body = analyzeStatement(foreachStmt.getBody(),
					containingDenomination, analyzerContext);
			VariableDeclarationExpression declareExpression = analyzeVariableDeclarationExpression(
					foreachStmt.getVariable(), containingDenomination,
					analyzerContext);
			AnalyzedExpression iterable = analyzeExpression(
					foreachStmt.getIterable(), containingDenomination,
					analyzerContext);

			return addComments(statement,
					new ForEachStatement(Location.from(foreachStmt),
							declareExpression, iterable, body));
		} else if (statement instanceof ForStmt) {
			ForStmt forStmt = (ForStmt) statement;

			Expression compareExpr = forStmt.getCompare();
			AnalyzedExpression compare = compareExpr != null
					? analyzeExpression(compareExpr, containingDenomination,
							analyzerContext)
					: null;
			AnalyzedStatement body = analyzeStatement(forStmt.getBody(),
					containingDenomination, analyzerContext);

			ForStatement forStatement = new ForStatement(Location.from(forStmt),
					body, compare);

			List<Expression> init = forStmt.getInit();
			if (init != null) {
				for (Expression expression : init) {
					AnalyzedExpression e = analyzeExpression(expression,
							containingDenomination, analyzerContext);
					if (e != null) {
						forStatement.addInitializerExpression(e);
					}
				}
			}

			List<Expression> update = forStmt.getUpdate();
			if (update != null) {
				for (Expression expression : update) {
					AnalyzedExpression e = analyzeExpression(expression,
							containingDenomination, analyzerContext);
					if (e != null) {
						forStatement.addUpdateExpression(e);
					}
				}
			}

			return addComments(statement, forStatement);
		} else if (statement instanceof LabeledStmt) {
			LabeledStmt labeledStmt = (LabeledStmt) statement;

			String label = labeledStmt.getLabel();
			AnalyzedStatement analyzedStatement = analyzeStatement(
					labeledStmt.getStmt(), containingDenomination,
					analyzerContext);

			return new LabeledStatement(Location.from(labeledStmt), label,
					analyzedStatement);
		} else if (statement instanceof SwitchStmt) {
			SwitchStmt switchStmt = (SwitchStmt) statement;

			SwitchStatement switchStatement = new SwitchStatement(
					Location.from(switchStmt),
					analyzeExpression(switchStmt.getSelector(),
							containingDenomination, analyzerContext));

			List<SwitchEntryStmt> entries = switchStmt.getEntries();
			if (entries != null) {
				for (SwitchEntryStmt entryStmt : entries) {
					switchStatement.addStatement(analyzeSwitchEntry(entryStmt,
							containingDenomination, analyzerContext));
				}
			}

			return addComments(statement, switchStatement);
		} else if (statement instanceof SwitchEntryStmt) {
			SwitchEntryStmt switchEntryStmt = (SwitchEntryStmt) statement;

			return addComments(statement, analyzeSwitchEntry(switchEntryStmt,
					containingDenomination, analyzerContext));

		} else if (statement instanceof SynchronizedStmt) {
			SynchronizedStmt synchronizedStmt = (SynchronizedStmt) statement;

			AnalyzedStatement block = analyzeStatement(
					synchronizedStmt.getBlock(), containingDenomination,
					analyzerContext);
			AnalyzedExpression syncExpression = analyzeExpression(
					synchronizedStmt.getExpr(), containingDenomination,
					analyzerContext);

			return addComments(statement, new SynchronizedStatement(
					Location.from(synchronizedStmt), syncExpression, block));
		} else if (statement instanceof ThrowStmt) {
			ThrowStmt throwStmt = (ThrowStmt) statement;

			return addComments(statement,
					new ThrowStatement(Location.from(throwStmt),
							analyzeExpression(throwStmt.getExpr(),
									containingDenomination, analyzerContext)));
		} else if (statement instanceof TryStmt) {
			TryStmt tryStmt = (TryStmt) statement;

			BlockStmt finallyBlockStmt = tryStmt.getFinallyBlock();
			BlockStatement finallyBlock = finallyBlockStmt != null
					? analyzeBlockStatement(finallyBlockStmt,
							containingDenomination, analyzerContext)
					: null;
			BlockStatement tryBlock = analyzeBlockStatement(
					tryStmt.getTryBlock(), containingDenomination,
					analyzerContext);

			TryStatement tryStatement = new TryStatement(Location.from(tryStmt),
					tryBlock, finallyBlock);

			List<Resource> resources = tryStmt.getResources();
			if (resources != null) {
				for (Resource resource : resources) {
					AnalyzedType type = analyzeType(resource.getType());
					Expression initExpr = resource.getExpression();

					String resourceId = resource.getId().getName();
					ResourceStatement resourceStatement = new ResourceStatement(
							Location.from(resource), type, resourceId,
							ModifierSet.isFinal(resource.getModifiers()));

					if (initExpr != null) {
						AnalyzedExpression initializer = analyzeExpression(
								initExpr, containingDenomination,
								analyzerContext);
						resourceStatement.setInitializer(initializer);
					}

					List<AnnotationExpr> annotations = resource
							.getAnnotations();
					if (annotations != null) {
						for (AnnotationExpr annotationExpr : annotations) {
							AnalyzedAnnotation annotation = analyze(
									annotationExpr, containingDenomination,
									analyzerContext);
							if (annotation != null) {
								resourceStatement.addAnnotation(annotation);
							}
						}
					}

					tryStatement.addResourceStatement(resourceStatement);
				}
			}
			List<CatchClause> catchs = tryStmt.getCatchs();
			if (catchs != null) {
				for (CatchClause catchClause : catchs) {

					BlockStatement blockStatement = analyzeBlockStatement(
							catchClause.getCatchBlock(), containingDenomination,
							analyzerContext);
					CatchParameter except = catchClause.getExcept();

					CatchStatement catchStatement = new CatchStatement(
							Location.from(catchClause), blockStatement,
							except.getId().getName());

					List<AnnotationExpr> annotations = except.getAnnotations();
					if (annotations != null) {
						for (AnnotationExpr annotationExpr : annotations) {
							AnalyzedAnnotation annotation = analyze(
									annotationExpr, containingDenomination,
									analyzerContext);
							if (annotation != null) {
								catchStatement.addAnnotation(annotation);
							}
						}
					}

					List<Type> typeList = except.getTypeList();
					if (typeList != null) {
						for (Type type : typeList) {
							AnalyzedType analyzedType = analyzeType(type);

							if (analyzedType != null) {
								catchStatement.addExceptionType(analyzedType);
							}
						}
					}

					tryStatement.addCatchClause(catchStatement);

				}
			}

			return addComments(statement, tryStatement);
		} else if (statement instanceof TypeDeclarationStmt) {
			TypeDeclarationStmt typeDeclarationStmt = (TypeDeclarationStmt) statement;

			return addComments(statement,
					new TypeDeclarationStatement(
							Location.from(typeDeclarationStmt),
							analyze(null, analyzerContext.anonymousInnerClass(),
									typeDeclarationStmt.getTypeDeclaration())));
		} else if (statement instanceof WhileStmt) {
			WhileStmt whileStmt = (WhileStmt) statement;

			AnalyzedExpression condition = analyzeExpression(
					whileStmt.getCondition(), containingDenomination,
					analyzerContext);
			AnalyzedStatement body = analyzeStatement(whileStmt.getBody(),
					containingDenomination, analyzerContext);

			return addComments(statement, new WhileStatement(
					Location.from(whileStmt), condition, body));
		}

		throw new IllegalStateException(
				"Unhandled statement type! Go slap the andalite developers!");
	}

	private <T extends Commentable> T addComments(Node node, T commentable) {
		List<Comment> beginComments = node.getBeginComments();
		if (beginComments != null) {
			for (Comment comment : beginComments) {
				checkJavadoc(commentable, comment);
				commentable.addComment(comment.getContent());
			}
		}

		List<Comment> endComments = node.getEndComments();
		if (endComments != null) {
			for (Comment comment : endComments) {
				checkJavadoc(commentable, comment);
				commentable.addComment(comment.getContent());
			}
		}

		return commentable;
	}

	private <T extends Commentable> void checkJavadoc(T commentable,
			Comment comment) {
		if (comment instanceof JavadocComment
				&& commentable instanceof Javadocable) {
			JavadocComment jdc = (JavadocComment) comment;
			Javadocable jda = (Javadocable) commentable;

			if (jda.getJavadoc() == null) {
				jda.setJavadoc(jdc.getContent());
			}
		}
	}

	private <T extends Javadocable> T setJavadoc(BodyDeclaration node,
			T javadocable) {
		if (node != null) {
			JavadocComment comment = node.getJavaDoc();
			if (comment != null && javadocable != null) {
				javadocable.setJavadoc(comment.getContent());
			}
		}

		return javadocable;
	}

	private BlockStatement analyzeBlockStatement(BlockStmt blockStmt,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		BlockStatement block = new BlockStatement(Location.from(blockStmt));

		List<Statement> stmts = blockStmt.getStmts();
		if (stmts != null) {
			for (Statement s : stmts) {
				AnalyzedStatement analyzedStatement = analyzeStatement(s,
						containingDenomination, analyzerContext);
				if (analyzedStatement != null) {
					block.addStatement(analyzedStatement);
				}
			}
		}

		return block;
	}

	private SwitchEntryStatement analyzeSwitchEntry(
			SwitchEntryStmt switchEntryStmt,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		Expression label = switchEntryStmt.getLabel();
		AnalyzedExpression value = label != null ? analyzeExpression(label,
				containingDenomination, analyzerContext) : null;

		SwitchEntryStatement switchEntry = new SwitchEntryStatement(
				Location.from(switchEntryStmt), value);

		List<Statement> stmts = switchEntryStmt.getStmts();
		if (stmts != null) {
			for (Statement stmt : stmts) {
				AnalyzedStatement analyzed = analyzeStatement(stmt,
						containingDenomination, analyzerContext);

				switchEntry.addStatement(analyzed);
			}
		}

		return switchEntry;
	}

	@Nonnull
	private AnalyzedExpression analyzeExpression(Expression expr,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		final Location location = Location.from(expr);

		if (expr instanceof NullLiteralExpr) {
			return new NullExpression(location);

		}
		if (expr instanceof BooleanLiteralExpr) {
			BooleanLiteralExpr bool = (BooleanLiteralExpr) expr;

			return new BooleanLiteralExpression(location, bool.getValue());
		}
		if (expr instanceof CharLiteralExpr) {
			CharLiteralExpr charLit = (CharLiteralExpr) expr;

			return new CharLiteralExpression(location,
					charLit.getValue().charAt(0));
		}
		if (expr instanceof DoubleLiteralExpr) {
			DoubleLiteralExpr doubLit = (DoubleLiteralExpr) expr;

			return new DoubleLiteralExpression(location, doubLit.getValue());
		}
		if (expr instanceof IntegerLiteralExpr) {
			IntegerLiteralExpr intLit = (IntegerLiteralExpr) expr;

			return new IntegerLiteralExpression(location,
					Integer.parseInt(intLit.getValue()));
		}
		if (expr instanceof LongLiteralExpr) {
			LongLiteralExpr longLit = (LongLiteralExpr) expr;

			return new LongLiteralExpression(location, longLit.getValue());
		}
		if (expr instanceof StringLiteralExpr) {
			StringLiteralExpr lit = (StringLiteralExpr) expr;

			return new StringLiteralExpression(location, lit.getValue());
		}
		if (expr instanceof NameExpr) {
			NameExpr name = (NameExpr) expr;

			return new NameReferenceExpression(location,
					AnalyzeUtil.getQualifiedName(name));
		}
		if (expr instanceof MethodCallExpr) {
			MethodCallExpr methodCall = (MethodCallExpr) expr;

			List<AnalyzedType> analyzedTypeArguments = Lists.newArrayList();
			List<Type> typeArgs = methodCall.getTypeArgs();
			if (typeArgs != null) {
				for (Type type : typeArgs) {
					AnalyzedType at = analyzeType(type);

					analyzedTypeArguments.add(at);
				}
			}

			List<AnalyzedExpression> analyzedArguments = Lists.newArrayList();
			List<Expression> args = methodCall.getArgs();
			if (args != null) {
				for (Expression expression : args) {
					AnalyzedExpression ae = analyzeExpression(expression,
							containingDenomination, analyzerContext);
					if (ae != null) {
						analyzedArguments.add(ae);
					}
				}
			}

			MethodCallExpression expression = new MethodCallExpression(location,
					methodCall.getName(), analyzedTypeArguments,
					analyzedArguments);

			Expression scopeExpr = methodCall.getScope();
			if (scopeExpr != null) {
				expression.setScope(analyzeExpression(scopeExpr,
						containingDenomination, analyzerContext));
			}

			return expression;

		}
		if (expr instanceof AnnotationExpr) {
			AnnotationExpr annotationExpr = (AnnotationExpr) expr;

			return new AnnotationExpression(location,
					AnalyzeUtil.getQualifiedName(annotationExpr.getName()));
		}
		if (expr instanceof ArrayAccessExpr) {
			ArrayAccessExpr arrayAccessExpr = (ArrayAccessExpr) expr;

			AnalyzedExpression index = analyzeExpression(
					arrayAccessExpr.getIndex(), containingDenomination,
					analyzerContext);
			AnalyzedExpression name = analyzeExpression(
					arrayAccessExpr.getName(), containingDenomination,
					analyzerContext);

			return new ArrayAccessExpression(location, name, index);
		}
		if (expr instanceof ArrayCreationExpr) {
			ArrayCreationExpr arrayCreationExpr = (ArrayCreationExpr) expr;

			ArrayInitializerExpression initializer = parseInitializer(
					arrayCreationExpr.getInitializer(), containingDenomination,
					analyzerContext);
			AnalyzedType type = analyzeType(arrayCreationExpr.getType());

			ArrayCreationExpression creationExpression = new ArrayCreationExpression(
					location, type, initializer);

			List<Expression> dimensions = arrayCreationExpr.getDimensions();
			if (dimensions != null) {
				for (Expression expression : dimensions) {
					creationExpression
							.addDimension(analyzeExpression(expression,
									containingDenomination, analyzerContext));
				}
			}

			return creationExpression;
		}
		if (expr instanceof ArrayInitializerExpr) {
			ArrayInitializerExpr arrayInitializerExpr = (ArrayInitializerExpr) expr;

			return parseInitializer(arrayInitializerExpr,
					containingDenomination, analyzerContext);
		}
		if (expr instanceof AssignExpr) {
			AssignExpr assignExpr = (AssignExpr) expr;

			AnalyzedExpression value = analyzeExpression(assignExpr.getValue(),
					containingDenomination, analyzerContext);
			AnalyzedExpression target = analyzeExpression(
					assignExpr.getTarget(), containingDenomination,
					analyzerContext);
			AssignExpression.Operator operator = AssignExpression.Operator
					.values()[assignExpr.getOperator().ordinal()];

			return new AssignExpression(location, target, operator, value);
		}
		if (expr instanceof BinaryExpr) {
			BinaryExpr binaryExpr = (BinaryExpr) expr;

			AnalyzedExpression left = analyzeExpression(binaryExpr.getLeft(),
					containingDenomination, analyzerContext);
			AnalyzedExpression right = analyzeExpression(binaryExpr.getRight(),
					containingDenomination, analyzerContext);
			BinaryExpression.Operator operator = BinaryExpression.Operator
					.values()[binaryExpr.getOperator().ordinal()];

			return new BinaryExpression(location, left, operator, right);
		}
		if (expr instanceof CastExpr) {
			CastExpr castExpr = (CastExpr) expr;

			AnalyzedExpression expression = analyzeExpression(
					castExpr.getExpr(), containingDenomination,
					analyzerContext);
			AnalyzedType type = analyzeType(castExpr.getType());

			return new CastExpression(location, type, expression);

		}
		if (expr instanceof ClassExpr) {
			ClassExpr classExpr = (ClassExpr) expr;

			return new ClassExpression(location,
					analyzeType(classExpr.getType()));
		}
		if (expr instanceof ConditionalExpr) {
			ConditionalExpr conditionalExpr = (ConditionalExpr) expr;

			AnalyzedExpression condition = analyzeExpression(
					conditionalExpr.getCondition(), containingDenomination,
					analyzerContext);
			AnalyzedExpression primary = analyzeExpression(
					conditionalExpr.getThenExpr(), containingDenomination,
					analyzerContext);
			AnalyzedExpression alternate = analyzeExpression(
					conditionalExpr.getElseExpr(), containingDenomination,
					analyzerContext);

			return new ConditionalExpression(Location.from(conditionalExpr),
					condition, primary, alternate);
		}
		if (expr instanceof EnclosedExpr) {
			EnclosedExpr enclosedExpr = (EnclosedExpr) expr;

			AnalyzedExpression inner = analyzeExpression(
					enclosedExpr.getInner(), containingDenomination,
					analyzerContext);

			return new EnclosedExpression(Location.from(enclosedExpr), inner);
		}
		if (expr instanceof FieldAccessExpr) {
			FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expr;

			String fieldName = fieldAccessExpr.getField();
			Expression scopeExpr = fieldAccessExpr.getScope();
			AnalyzedExpression scope = scopeExpr != null ? analyzeExpression(
					scopeExpr, containingDenomination, analyzerContext) : null;

			FieldAccessExpression fieldAccessExpression = new FieldAccessExpression(
					Location.from(fieldAccessExpr), scope, fieldName);

			List<Type> typeArgs = fieldAccessExpr.getTypeArgs();
			if (typeArgs != null) {
				for (Type type : typeArgs) {
					AnalyzedType at = analyzeType(type);
					if (at != null) {
						fieldAccessExpression.addTypeArgument(at);
					}
				}
			}

			return fieldAccessExpression;
		}
		if (expr instanceof InstanceOfExpr) {
			InstanceOfExpr instanceOfExpr = (InstanceOfExpr) expr;

			AnalyzedType target = analyzeType(instanceOfExpr.getType());
			AnalyzedExpression expression = analyzeExpression(
					instanceOfExpr.getExpr(), containingDenomination,
					analyzerContext);

			return new InstanceOfExpression(Location.from(instanceOfExpr),
					expression, target);

		}
		if (expr instanceof LambdaExpr) {
			LambdaExpr lambdaExpr = (LambdaExpr) expr;

			// TODO: antlr-java-parser parses these correctly, but does not pass
			// them

			return new LambdaExpression(Location.from(lambdaExpr));

		}
		if (expr instanceof MethodReferenceExpr) {
			MethodReferenceExpr methodReferenceExpr = (MethodReferenceExpr) expr;

			// TODO: antlr-java-parser parses these correctly, but does not pass
			// them

			return new MethodReferenceExpression(
					Location.from(methodReferenceExpr));
		}
		if (expr instanceof ObjectCreationExpr) {
			ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) expr;

			ClassOrInterface type = analyzeClassOrInterface(
					objectCreationExpr.getType());

			if (type != null) {

				ObjectCreationExpression creationExpression = new ObjectCreationExpression(
						Location.from(objectCreationExpr), type);
				Expression scopeExpr = objectCreationExpr.getScope();
				if (scopeExpr != null) {
					creationExpression.setScope(analyzeExpression(scopeExpr,
							containingDenomination, analyzerContext));
				}
				List<Expression> args = objectCreationExpr.getArgs();
				if (args != null) {
					for (Expression expression : args) {
						creationExpression.addArgument(analyzeExpression(
								expression, containingDenomination,
								analyzerContext));
					}
				}
				List<Type> typeArgs = objectCreationExpr.getTypeArgs();
				if (typeArgs != null) {
					for (Type t : typeArgs) {
						creationExpression.addTypeArgument(analyzeType(t));
					}
				}
				List<BodyDeclaration> anonymousClassBody = objectCreationExpr
						.getAnonymousClassBody();
				if (anonymousClassBody != null) {
					AnalyzerContext innerClassContext = analyzerContext
							.anonymousInnerClass();

					AnalyzedClass innerClass = new AnalyzedClass(
							Location.from(objectCreationExpr), 0,
							analyzerContext.getScope(),
							objectCreationExpr.getType().getName());
					innerClass.setSourceFile(
							containingDenomination.getSourceFile());
					containingDenomination.addInnerDenomination(innerClass);

					for (BodyDeclaration bodyDeclaration : anonymousClassBody) {
						analyzeBodyElement(
								Integer.toString(
										innerClassContext.anonymousInnerClassCounter
												- 1),
								innerClass, innerClassContext, bodyDeclaration);
					}
					creationExpression.setDeclaredAnonymousClass(innerClass);
				}

				return creationExpression;
			}

		}
		if (expr instanceof SuperExpr) {
			SuperExpr superExpr = (SuperExpr) expr;

			Expression classExpr = superExpr.getClassExpr();
			AnalyzedExpression classExpression = classExpr != null
					? analyzeExpression(classExpr, containingDenomination,
							analyzerContext)
					: null;

			return new SuperExpression(Location.from(superExpr),
					classExpression);
		}
		if (expr instanceof ThisExpr) {
			ThisExpr thisExpr = (ThisExpr) expr;

			Expression classExpr = thisExpr.getClassExpr();
			AnalyzedExpression classExpression = classExpr != null
					? analyzeExpression(classExpr, containingDenomination,
							analyzerContext)
					: null;

			return new ThisExpression(Location.from(thisExpr), classExpression);
		}
		if (expr instanceof UnaryExpr) {
			UnaryExpr unaryExpr = (UnaryExpr) expr;

			AnalyzedExpression expression = analyzeExpression(
					unaryExpr.getExpr(), containingDenomination,
					analyzerContext);
			UnaryExpression.Operator operator = UnaryExpression.Operator
					.values()[unaryExpr.getOperator().ordinal()];

			return new UnaryExpression(Location.from(unaryExpr), operator,
					expression);

		}
		if (expr instanceof VariableDeclarationExpr) {
			return analyzeVariableDeclarationExpression(
					(VariableDeclarationExpr) expr, containingDenomination,
					analyzerContext);
		}

		return new NullExpression(location);
	}

	private VariableDeclarationExpression analyzeVariableDeclarationExpression(
			VariableDeclarationExpr variable,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {

		final int modifiers = variable.getModifiers();
		final AnalyzedType type = analyzeType(variable.getType());

		VariableDeclarationExpression expression = new VariableDeclarationExpression(
				Location.from(variable), modifiers, type);

		List<AnnotationExpr> annotations = variable.getAnnotations();
		if (annotations != null) {
			for (AnnotationExpr annotationExpr : annotations) {
				AnalyzedAnnotation annotation = analyze(annotationExpr,
						containingDenomination, analyzerContext);

				expression.addAnnotation(annotation);
			}
		}

		List<VariableDeclarator> vars = variable.getVars();
		if (vars != null) {
			for (VariableDeclarator var : vars) {
				Expression initExpr = var.getInit();
				AnalyzedExpression init = initExpr != null
						? analyzeExpression(initExpr, containingDenomination,
								analyzerContext)
						: null;

				expression.addDeclareVariable(new DeclareVariableExpression(
						Location.from(var), var.getId().getName(), init));
			}
		}

		return expression;
	}

	@CheckForNull
	private ArrayInitializerExpression parseInitializer(
			@Nullable ArrayInitializerExpr initializer,
			ContainingDenomination containingDenomination,
			AnalyzerContext analyzerContext) {
		if (initializer != null) {
			ArrayInitializerExpression expression = new ArrayInitializerExpression(
					Location.from(initializer));

			for (Expression expr : initializer.getValues()) {
				expression.addValue(analyzeExpression(expr,
						containingDenomination, analyzerContext));
			}

			return expression;
		}

		return null;
	}

	private AnalyzedType analyzeType(Type type) {
		final Location location = Location.from(type);

		if (type instanceof ClassOrInterfaceType) {
			ClassOrInterfaceType classOrInterface = (ClassOrInterfaceType) type;

			return analyzeClassOrInterface(classOrInterface);
		}

		if (type instanceof PrimitiveType) {
			PrimitiveType prim = (PrimitiveType) type;

			return new Primitive(location,
					Primitive.PrimitiveType.fromParserType(prim.getType()));
		}
		if (type instanceof ReferenceType) {
			ReferenceType ref = (ReferenceType) type;

			Reference rt = processReferenceType(location, ref);
			if (rt != null) {
				return rt;
			}
		}
		if (type instanceof WildcardType) {
			WildcardType wildcard = (WildcardType) type;

			ReferenceType superType = wildcard.getSuper();
			Reference superRef = superType != null
					? processReferenceType(Location.from(superType), superType)
					: null;
			ReferenceType extendsType = wildcard.getExtends();
			Reference extendsRef = extendsType != null ? processReferenceType(
					Location.from(extendsType), extendsType) : null;

			return new Wildcard(location, superRef, extendsRef);
		}

		return new TypeVoid(location);
	}

	private Reference processReferenceType(final Location location,
			ReferenceType ref) {
		AnalyzedType refType = analyzeType(ref.getType());

		if (refType != null) {
			return new Reference(location, refType, ref.getArrayCount());
		}

		return null;
	}

	@CheckForNull
	private ClassOrInterface analyzeClassOrInterface(
			@Nullable ClassOrInterfaceType classOrInterface) {
		if (classOrInterface == null)
			return null;

		ClassOrInterface scope = analyzeClassOrInterface(
				classOrInterface.getScope());

		List<AnalyzedType> analyzedTypeArguments = Lists.newArrayList();
		List<Type> typeArgs = classOrInterface.getTypeArgs();
		if (typeArgs != null) {
			for (Type subtype : typeArgs) {
				AnalyzedType at = analyzeType(subtype);

				analyzedTypeArguments.add(at);
			}
		}

		return new ClassOrInterface(Location.from(classOrInterface),
				classOrInterface.getName(), scope, analyzedTypeArguments);
	}

	public interface IStatementAssigner {
		void assignStatement(@Nonnull AnalyzedStatement statement);
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

		public AnalyzerContext innerDeclaration(TerminalNode name) {
			return new AnalyzerContext(name.getText(), this);
		}

	}
}
