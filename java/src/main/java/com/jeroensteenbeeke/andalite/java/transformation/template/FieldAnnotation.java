package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.FieldOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import org.jetbrains.annotations.NotNull;

public class FieldAnnotation extends AnnotationAdditionTemplate<FieldAnnotation> implements PropertyElementTemplate {
	private final TypeReference type;

	FieldAnnotation(@NotNull TypeReference type) {
		this(type, ImmutableList.of());
	}

	private FieldAnnotation(@NotNull TypeReference type, @NotNull ImmutableList<AnnotationValueTemplate<?>> values) {
		super(values);
		this.type = type;
	}

	@NotNull
	@Override
	protected FieldAnnotation newInstance(@NotNull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new FieldAnnotation(type, templates);
	}

	@Override
	public void onField(JavaRecipeBuilder builder, FieldOperationBuilder fieldBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		fieldBuilder.ensureAnnotation(type.name());

		applyValues(builder, fieldBuilder.forAnnotation(type.name()));
	}
}
