package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public class Templates {
	static final String JAVA_LANG = "java.lang";

	@Nonnull
	public static ClassTemplate aPublicClass() {
		return new ClassTemplate(JavaRecipeBuilder::ensurePublicClass, JavaRecipeBuilder::inPublicClass);
	}

	@Nonnull
	public static ClassTemplate aPackageClass(String name) {
		return new ClassTemplate(b -> b.ensurePackageClass(name), b -> b.inPackageClass(name));
	}

	@Nonnull
	public static InterfaceTemplate aPublicInterface() {
		return new InterfaceTemplate(ImmutableList.of(), ImmutableList.of());
	}

	public static EnumTemplate aPublicEnum() {
		return new EnumTemplate(ImmutableList.of(), ImmutableList.of());
	}

	public static EnumConstantTemplate enumConstant(@Nonnull String name) {
		return new EnumConstantTemplate(name);
	}

	@Nonnull
	public static ClassAnnotation classAnnotation(@Nonnull String type) {
		return new ClassAnnotation(TypeReference.of(type), ImmutableList.of());
	}

	@Nonnull
	public static EnsuredImport ensuredImport(@Nonnull String fqdn) {
		return new EnsuredImport(TypeReference.of(fqdn));
	}

	@Nonnull
	public static FieldOfType field(@Nonnull String name) {
		return type -> new FieldTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface FieldOfType {
		FieldTemplate ofType(@Nonnull String type);
	}

	@Nonnull
	public static PropertyOfType property(@Nonnull String name) {
		return type -> new PropertyTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface PropertyOfType {
		@Nonnull
		PropertyTemplate ofType(@Nonnull String type);
	}

	public static Initializer initializer(@Nonnull String expression) {
		return new Initializer(expression);
	}

	public static ConstructorTemplate constructor() {
		return new ConstructorTemplate();
	}

	public static MethodOfType method(@Nonnull String name) {
		return type -> new MethodTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface MethodOfType {
		@Nonnull
		MethodTemplate ofType(@Nonnull String type);
	}

	public static ParameterOfType parameter(@Nonnull String name) {
		return type -> new ParameterTemplate(TypeReference.of(type), name);
	}

	@FunctionalInterface
	public interface ParameterOfType {
		@Nonnull
		ParameterTemplate ofType(@Nonnull String type);
	}

	public static ParameterAnnotation parameterAnnotation(@Nonnull String type) {
		return new ParameterAnnotation(TypeReference.of(type));
	}

	public static MethodAnnotation methodAnnotation(@Nonnull String type) {
		return new MethodAnnotation(TypeReference.of(type));
	}

	public static Returns returns(@Nonnull String expression) {
		return new Returns(expression);
	}

	public static StatementTemplate statement(@Nonnull String statement) {
		return new StatementTemplate(statement);
	}

	public static FieldAnnotation fieldAnnotation(@Nonnull String type) {
		return new FieldAnnotation(TypeReference.of(type));
	}

	public static GetterAnnotation getterAnnotation(@Nonnull String type) {
		return new GetterAnnotation(TypeReference.of(type));
	}

	public static OptionalGetterAnnotation optionalGetterAnnotation(@Nonnull String type) {
		return new OptionalGetterAnnotation(TypeReference.of(type));
	}

	public static SetterParameterAnnotation setterParameterAnnotation(@Nonnull String type) {
		return new SetterParameterAnnotation(TypeReference.of(type));
	}

	public static AnnotationValueBuilder<String> stringField(@Nonnull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureStringValue);
	}

	public static AnnotationValueBuilder<Integer> intField(@Nonnull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureIntegerValue);
	}

	public static AnnotationValueBuilder<Boolean> booleanField(@Nonnull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureBooleanValue);
	}

	public static AnnotationValueBuilder<String> fieldAccessField(@Nonnull String name) {
		return value -> new AnnotationValueTemplate<>(name, value, IAnnotationOperationBuilder::ensureFieldAccessValue);
	}

	@FunctionalInterface
	public interface AnnotationValueBuilder<T> {
		AnnotationValueTemplate<T> withValue(T value);
	}
}
