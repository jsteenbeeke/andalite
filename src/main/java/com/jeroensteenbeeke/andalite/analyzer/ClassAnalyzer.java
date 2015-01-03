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

package com.jeroensteenbeeke.andalite.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.ParseException;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.ImportDeclaration;
import com.github.antlrjavaparser.api.PackageDeclaration;
import com.github.antlrjavaparser.api.body.*;
import com.github.antlrjavaparser.api.expr.*;
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;
import com.github.antlrjavaparser.api.type.Type;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.TypedActionResult;
import com.jeroensteenbeeke.andalite.analyzer.annotation.*;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ClassValue;

public class ClassAnalyzer {
	private final File targetFile;

	public ClassAnalyzer(@Nonnull File targetFile) {
		super();
		this.targetFile = targetFile;
	}

	@Nonnull
	public TypedActionResult<AnalyzedSourceFile> analyze() {
		try {
			CompilationUnit compilationUnit = JavaParser.parse(targetFile);

			PackageDeclaration packageDefinition = compilationUnit.getPackage();

			final String packageName = determinePackageName(packageDefinition);

			AnalyzedSourceFile sourceFile = new AnalyzedSourceFile(
					Location.from(compilationUnit), targetFile, packageName,
					Location.from(packageDefinition));

			for (ImportDeclaration importDeclaration : compilationUnit
					.getImports()) {
				AnalyzedImport imp = new AnalyzedImport(
						Location.from(importDeclaration), importDeclaration
								.getName().toString(),
						importDeclaration.isStatic(),
						importDeclaration.isAsterisk());

				sourceFile.addImport(imp);
			}

			for (TypeDeclaration typeDeclaration : compilationUnit.getTypes()) {
				analyze(sourceFile, packageName, typeDeclaration);
			}

			return TypedActionResult.ok(sourceFile);
		} catch (ParseException | IOException e) {
			return TypedActionResult.fail(e.getMessage());
		}
	}

	private void analyze(@Nonnull AnalyzedSourceFile sourceFile,
			@Nonnull String packageName,
			@Nonnull TypeDeclaration typeDeclaration) {
		if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration decl = (ClassOrInterfaceDeclaration) typeDeclaration;

			if (decl.isInterface()) {
				AnalyzedInterface element = new AnalyzedInterface(
						Location.from(decl), decl.getModifiers(), packageName,
						decl.getName());

				processInterfaceDeclaration(decl, element);

				sourceFile.addInterface(element);
			} else {
				AnalyzedClass element = new AnalyzedClass(Location.from(decl),
						decl.getModifiers(), packageName, decl.getName());

				processClassDeclaration(decl, element);

				sourceFile.addClass(element);
			}
		} else if (typeDeclaration instanceof EnumDeclaration) {
			EnumDeclaration decl = (EnumDeclaration) typeDeclaration;

			AnalyzedEnum element = new AnalyzedEnum(Location.from(decl),
					decl.getModifiers(), packageName, decl.getName());

			processEnumDeclaration(decl, element);

			sourceFile.addEnum(element);
		} else if (typeDeclaration instanceof AnnotationDeclaration) {
			AnnotationDeclaration decl = (AnnotationDeclaration) typeDeclaration;

			AnalyzedAnnotationType element = new AnalyzedAnnotationType(
					Location.from(decl), decl.getModifiers(), packageName,
					decl.getName());

			processAnnotationDeclaration(decl, element);

			sourceFile.addAnnotation(element);
		}

	}

	private void processAnnotationDeclaration(AnnotationDeclaration decl,
			AnalyzedAnnotationType element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr);
			if (annotation != null) {
				element.addAnnotation(annotation);
			}
		}

	}

	private void processEnumDeclaration(EnumDeclaration decl,
			AnalyzedEnum element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr);
			if (annotation != null) {
				element.addAnnotation(annotation);
			}
		}

		List<ClassOrInterfaceType> implementedInterfaces = decl.getImplements();
		if (implementedInterfaces != null) {
			for (ClassOrInterfaceType type : implementedInterfaces) {
				element.addInterface(type.getName());
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
				analyzeBodyElement(decl.getName(), element, member);
			}
		}

		element.setBodyLocation(new Location(start, end));
	}

	private void processEnumConstantDeclaration(EnumConstantDeclaration decl,
			AnalyzedEnumConstant element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr);
			if (annotation != null) {
				element.addAnnotation(annotation);
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
				analyzeBodyElement(null, element, member);
			}
		}

		element.setBodyLocation(new Location(start, end));
	}

	private void analyzeBodyElement(String typeName,
			ContainingDenomination element, BodyDeclaration member) {
		if (member instanceof FieldDeclaration) {
			List<AnalyzedField> fields = analyze((FieldDeclaration) member);
			for (AnalyzedField field : fields) {
				element.addField(field);
			}
		} else if (member instanceof MethodDeclaration) {
			AnalyzedMethod method = analyze((MethodDeclaration) member);

			if (method != null) {
				if (element.isAutoAbstractMethods()) {
					method.setAbstract(true);
				}

				element.addMethod(method);
			}
		} else if (member instanceof EnumConstantDeclaration) {
			if (element instanceof AnalyzedEnum) {
				EnumConstantDeclaration enumDecl = (EnumConstantDeclaration) member;

				AnalyzedEnumConstant constant = new AnalyzedEnumConstant(
						Location.from(enumDecl), ModifierSet.PUBLIC,
						String.format("%s.%s", element.getPackageName(),
								element.getDenominationName()),
						enumDecl.getName());

				processEnumConstantDeclaration(enumDecl, constant);

				((AnalyzedEnum) element).addConstant(constant);
			}

		} else if (member instanceof ConstructorDeclaration) {
			AnalyzedConstructor constr = analyze(typeName,
					(ConstructorDeclaration) member);
			if (constr != null) {
				addConstructor(element, constr);
			}
		} else if (member instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration innerClassDecl = (ClassOrInterfaceDeclaration) member;

			if (innerClassDecl.isInterface()) {
				AnalyzedInterface innerInterface = new AnalyzedInterface(
						Location.from(innerClassDecl),
						innerClassDecl.getModifiers(), String.format("%s.%s",
								element.getPackageName(),
								element.getDenominationName()),
						innerClassDecl.getName());

				processInterfaceDeclaration(innerClassDecl, innerInterface);

				element.addInnerDenomination(innerInterface);

			} else {
				AnalyzedClass innerClass = new AnalyzedClass(
						Location.from(innerClassDecl),
						innerClassDecl.getModifiers(), String.format("%s.%s",
								element.getPackageName(),
								element.getDenominationName()),
						innerClassDecl.getName());

				processClassDeclaration(innerClassDecl, innerClass);

				element.addInnerDenomination(innerClass);
			}

		}
	}

	private void addConstructor(ContainingDenomination element,
			AnalyzedConstructor constr) {
		if (element instanceof ConstructableDenomination) {
			ConstructableDenomination constructable = (ConstructableDenomination) element;

			constructable.addConstructor(constr);
		}
	}

	private void processClassDeclaration(ClassOrInterfaceDeclaration decl,
			AnalyzedClass element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr);
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
				element.setSuperClass(type.getName());
				element.setExtendsLocation(Location.from(type));

				break; // Classes only have single inheritance
			}
		}

		List<ClassOrInterfaceType> implementedInterfaces = decl.getImplements();
		if (implementedInterfaces != null) {
			for (ClassOrInterfaceType type : implementedInterfaces) {
				element.addInterface(type.getName());
				element.setLastImplementsLocation(Location.from(type));
			}
		}

		List<BodyDeclaration> members = decl.getMembers();
		if (members != null) {
			for (BodyDeclaration member : members) {
				if (start == classEndMinusOne) {
					start = member.getBeginIndex();
				}
				analyzeBodyElement(decl.getName(), element, member);
			}
		}

		element.setBodyLocation(new Location(start, end));
	}

	private void processInterfaceDeclaration(ClassOrInterfaceDeclaration decl,
			AnalyzedInterface element) {
		for (AnnotationExpr expr : decl.getAnnotations()) {
			AnalyzedAnnotation annotation = analyze(expr);
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
				element.addInterface(type.getName());
				element.setLastImplementsLocation(Location.from(type));
			}
		}

		for (BodyDeclaration member : decl.getMembers()) {
			if (start == classEndMinusOne) {
				start = member.getBeginIndex();
			}
			analyzeBodyElement(decl.getName(), element, member);
		}

		element.setBodyLocation(new Location(start, end));
	}

	private AnalyzedConstructor analyze(String className,
			ConstructorDeclaration member) {
		AnalyzedConstructor constructor = new AnalyzedConstructor(
				Location.from(member), className, member.getModifiers());

		constructor.addAnnotations(determineAnnotations(member));

		List<Parameter> parameters = member.getParameters();
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				AnalyzedParameter analyzedParameter = analyze(parameter);
				if (analyzedParameter != null) {
					constructor.addParameter(analyzedParameter);
				}
			}
		}
		return constructor;
	}

	private AnalyzedMethod analyze(MethodDeclaration member) {
		AnalyzedMethod method = new AnalyzedMethod(Location.from(member),
				member.getModifiers(), member.getName());

		method.addAnnotations(determineAnnotations(member));

		List<Parameter> parameters = member.getParameters();
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				AnalyzedParameter analyzedParameter = analyze(parameter);
				if (analyzedParameter != null) {
					method.addParameter(analyzedParameter);
				}
			}
		}

		return method;
	}

	private AnalyzedParameter analyze(Parameter parameter) {
		AnalyzedParameter param = new AnalyzedParameter(
				Location.from(parameter), parameter.getType().toString(),
				parameter.getId().getName());

		for (AnnotationExpr annotationExpr : parameter.getAnnotations()) {
			AnalyzedAnnotation annot = analyze(annotationExpr);
			if (annot != null) {
				param.addAnnotation(annot);
			}
		}

		return param;
	}

	private List<AnalyzedField> analyze(FieldDeclaration member) {
		final Type type = member.getType();
		final int modifiers = member.getModifiers();

		Builder<AnalyzedField> builder = ImmutableList.builder();
		List<AnalyzedAnnotation> annotations = determineAnnotations(member);

		for (VariableDeclarator var : member.getVariables()) {
			String name = var.getId().getName();

			AnalyzedField analyzedField = new AnalyzedField(
					Location.from(member), modifiers, name, type.toString());
			analyzedField.setSpecificDeclarationLocation(Location.from(var));
			analyzedField.addAnnotations(annotations);

			builder.add(analyzedField);

		}

		return builder.build();
	}

	private List<AnalyzedAnnotation> determineAnnotations(BodyDeclaration member) {
		Builder<AnalyzedAnnotation> annot = ImmutableList.builder();

		for (AnnotationExpr annotExpr : member.getAnnotations()) {
			AnalyzedAnnotation analyzedAnnotation = analyze(annotExpr);
			if (analyzedAnnotation != null) {
				annot.add(analyzedAnnotation);
			}
		}

		List<AnalyzedAnnotation> annotations = annot.build();
		return annotations;
	}

	private AnalyzedAnnotation analyze(AnnotationExpr expr) {
		AnalyzedAnnotation annotation = new AnalyzedAnnotation(
				Location.from(expr), expr.getName().toString());

		if (expr instanceof SingleMemberAnnotationExpr) {
			SingleMemberAnnotationExpr annot = (SingleMemberAnnotationExpr) expr;

			Expression memberValue = annot.getMemberValue();

			assignValue(annotation, "value", memberValue);

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

					assignValue(annotation, name, memberValue);

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
			Expression expr) {
		BaseValue<?> value = translateExpression(name, expr);

		if (value != null) {
			annotation.addAnnotation(value);
		}

	}

	private BaseValue<?> translateExpression(String name, Expression expr) {
		if (expr instanceof AnnotationExpr) {
			AnnotationExpr annot = (AnnotationExpr) expr;
			AnalyzedAnnotation sub = analyze(annot);

			return new AnnotationValue(Location.from(expr), name, sub);
		} else if (expr instanceof BooleanLiteralExpr) {
			BooleanLiteralExpr bool = (BooleanLiteralExpr) expr;

			return new BooleanValue(Location.from(expr), name, bool.getValue());
		} else if (expr instanceof CharLiteralExpr) {
			CharLiteralExpr charLit = (CharLiteralExpr) expr;

			return new CharValue(Location.from(expr), name, charLit.getValue()
					.charAt(0));

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

			return new ClassValue(Location.from(expr), name, classExpr
					.getType().toString());

		} else if (expr instanceof ArrayInitializerExpr) {
			ArrayInitializerExpr array = (ArrayInitializerExpr) expr;

			Builder<BaseValue<?>> builder = ImmutableList.builder();
			for (Expression expression : array.getValues()) {
				BaseValue<?> val = translateExpression(null, expression);
				if (val != null) {
					builder.add(val);
				}
			}

			return new ArrayValue(Location.from(expr), name, builder.build());
		}

		return null;
	}

	private String determinePackageName(PackageDeclaration pkg) {
		NameExpr name = pkg.getName();
		return name.toString();
	}
}
