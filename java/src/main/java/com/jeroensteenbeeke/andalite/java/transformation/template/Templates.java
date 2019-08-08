package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;

import javax.annotation.Nonnull;

public class Templates {
	static final String JAVA_LANG = "java.lang";

	@Nonnull
	public static ClassTemplate aPublicClass() {
		return new ClassTemplate();
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

	@FunctionalInterface
	public interface AnnotationValueBuilder<T> {
		AnnotationValueTemplate<T> withValue(T value);
	}
}
