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
import com.github.antlrjavaparser.api.type.Type;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.CodePoint;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.TypedActionResult;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BooleanValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.StringValue;

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

			AnalyzedClass element = new AnalyzedClass(Location.from(decl),
					decl.getModifiers(), packageName, decl.getName());

			processClassDeclaration(decl, element);

			sourceFile.addClass(element);
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

		CodePoint start = null;

		BodyDeclaration last = null;

		for (BodyDeclaration member : decl.getMembers()) {
			last = member;
			if (start == null) {
				start = new CodePoint(member.getBeginLine(),
						member.getBeginColumn());
			}
			if (member instanceof FieldDeclaration) {
				List<AnalyzedField> fields = analyze((FieldDeclaration) member);
				for (AnalyzedField field : fields) {
					element.addField(field);
				}
			} else if (member instanceof MethodDeclaration) {
				AnalyzedMethod method = analyze((MethodDeclaration) member);
				if (method != null) {
					element.addMethod(method);
				}
			} else if (member instanceof ConstructorDeclaration) {
				AnalyzedConstructor constr = analyze(decl.getName(),
						(ConstructorDeclaration) member);
				if (constr != null) {
					element.addConstructor(constr);
				}
			}
		}
		CodePoint end = null;

		if (last != null) {
			end = new CodePoint(last.getEndLine(), last.getEndColumn());
		} else {
			if (decl.getEndColumn() > 1) {
				end = new CodePoint(decl.getEndLine(), decl.getEndColumn() - 1);
			} else {

			}
		}
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

			AnalyzedField analyzedField = new AnalyzedField(Location.from(var),
					modifiers, name, type.toString());
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
		} else if (expr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr annot = (NormalAnnotationExpr) expr;
			for (MemberValuePair mvp : annot.getPairs()) {
				String name = mvp.getName();
				Expression memberValue = mvp.getValue();

				assignValue(annotation, name, memberValue);
			}
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

			return new AnnotationValue(name, sub);
		} else if (expr instanceof BooleanLiteralExpr) {
			BooleanLiteralExpr bool = (BooleanLiteralExpr) expr;

			return new BooleanValue(name, bool.getValue());
		} else if (expr instanceof StringLiteralExpr) {
			StringLiteralExpr str = (StringLiteralExpr) expr;

			return new StringValue(name, str.getValue());
		} else if (expr instanceof ArrayInitializerExpr) {
			ArrayInitializerExpr array = (ArrayInitializerExpr) expr;

			Builder<BaseValue<?>> builder = ImmutableList.builder();
			for (Expression expression : array.getValues()) {
				BaseValue<?> val = translateExpression(null, expression);
				if (val != null) {
					builder.add(val);
				}
			}

			return new ArrayValue(name, builder.build());
		}

		return null;
	}

	private String determinePackageName(PackageDeclaration pkg) {
		NameExpr name = pkg.getName();
		return name.toString();
	}
}
