package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableSet;
import com.jeroensteenbeeke.andalite.java.transformation.FieldOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public class FieldAnnotation extends AnnotationAdditionTemplate<FieldAnnotation> implements PropertyElementTemplate {
	private final TypeReference type;

	FieldAnnotation(@Nonnull TypeReference type) {
		this(type, ImmutableSet.of());
	}

	public FieldAnnotation(@Nonnull TypeReference type, @Nonnull ImmutableSet<AnnotationValueTemplate<?>> values) {
		super(values);
		this.type = type;
	}

	@Nonnull
	@Override
	protected FieldAnnotation newInstance(@Nonnull ImmutableSet<AnnotationValueTemplate<?>> templates) {
		return new FieldAnnotation(type, templates);
	}

	@Override
	public void onField(JavaRecipeBuilder builder, FieldOperationBuilder fieldBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		fieldBuilder.ensureAnnotation(type.name());

		applyValues(builder, fieldBuilder.forAnnotation(type.name()));
	}
}
