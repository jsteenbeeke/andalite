package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import org.jetbrains.annotations.NotNull;

public class Templates {
	static final String JAVA_LANG = "java.lang";

	@NotNull
	public static ClassTemplate aPublicClass() {
		return new ClassTemplate(JavaRecipeBuilder::ensurePublicClass, JavaRecipeBuilder::inPublicClass);
	}

	@NotNull
	public static ClassTemplate aPackageClass(String name) {
		return new ClassTemplate(b -> b.ensurePackageClass(name), b -> b.inPackageClass(name));
	}

	@NotNull
	public static InterfaceTemplate aPublicInterface() {
		return new InterfaceTemplate(ImmutableList.of(), ImmutableList.of());
	}

	public static EnumTemplate aPublicEnum() {
		return new EnumTemplate(ImmutableList.of(), ImmutableList.of());
	}

	public static EnumConstantTemplate enumConstant(@NotNull String name) {
		return new EnumConstantTemplate(name);
	}

	@NotNull
	public static ClassAnnotation classAnnotation(@NotNull String type) {
		return new ClassAnnotation(TypeReference.of(type), ImmutableList.of());
	}

	@NotNull
	public static EnsuredImport ensuredImport(@NotNull String fqdn) {
		return new EnsuredImport(TypeReference.of(fqdn));
	}

	@NotNull
	public static FieldOfType field(@NotNull String name) {
		return type -> new FieldTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface FieldOfType {
		FieldTemplate ofType(@NotNull String type);
	}

	@NotNull
	public static PropertyOfType property(@NotNull String name) {
		return type -> new PropertyTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface PropertyOfType {
		@NotNull
		PropertyTemplate ofType(@NotNull String type);
	}

	public static Initializer initializer(@NotNull String expression) {
		return new Initializer(expression);
	}

	public static ConstructorTemplate constructor() {
		return new ConstructorTemplate();
	}

	public static MethodOfType method(@NotNull String name) {
		return type -> new MethodTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface MethodOfType {
		@NotNull
		MethodTemplate ofType(@NotNull String type);
	}

	public static ParameterOfType parameter(@NotNull String name) {
		return type -> new ParameterTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface ParameterOfType {
		@NotNull
		ParameterTemplate ofType(@NotNull String type);
	}

	public static ParameterAnnotation parameterAnnotation(@NotNull String type) {
		return new ParameterAnnotation(TypeReference.of(type));
	}

	public static MethodAnnotation methodAnnotation(@NotNull String type) {
		return new MethodAnnotation(TypeReference.of(type));
	}

	public static Returns returns(@NotNull String expression) {
		return new Returns(expression);
	}

	public static StatementTemplate statement(@NotNull String statement) {
		return new StatementTemplate(statement);
	}

	public static FieldAnnotation fieldAnnotation(@NotNull String type) {
		return new FieldAnnotation(TypeReference.of(type));
	}

	public static GetterAnnotation getterAnnotation(@NotNull String type) {
		return new GetterAnnotation(TypeReference.of(type));
	}

	public static OptionalGetterAnnotation optionalGetterAnnotation(@NotNull String type) {
		return new OptionalGetterAnnotation(TypeReference.of(type));
	}

	public static FluentSetterAnnotation fluentSetterAnnotation(@NotNull String type) {
		return new FluentSetterAnnotation(TypeReference.of(type));
	}

	public static SetterParameterAnnotation setterParameterAnnotation(@NotNull String type) {
		return new SetterParameterAnnotation(TypeReference.of(type));
	}

	public static AnnotationValueBuilder<String> stringField(@NotNull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureStringValue);
	}

	public static AnnotationValueBuilder<Integer> intField(@NotNull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureIntegerValue);
	}

	public static AnnotationValueBuilder<Boolean> booleanField(@NotNull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureBooleanValue);
	}

	public static AnnotationValueBuilder<String> fieldAccessField(@NotNull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureFieldAccessValue);
	}

	public static AnnotationValueBuilder<String> classField(@NotNull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureClassValue);
	}

	@FunctionalInterface
	public interface AnnotationValueBuilder<T> {
		AnnotationValueTemplate<T> withValue(T value);
	}
}
