package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public class MethodAnnotation extends AnnotationAdditionTemplate<MethodAnnotation> implements MethodElementTemplate {
	private final TypeReference type;

	MethodAnnotation(TypeReference type) {
		this(type, ImmutableList.of());
	}

	private MethodAnnotation(TypeReference type, ImmutableList<AnnotationValueTemplate<?>> templates) {
		super(templates);
		this.type = type;
	}

	@Nonnull
	@Override
	protected MethodAnnotation newInstance(@Nonnull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new MethodAnnotation(type, templates);
	}

	@Override
	public void onMethod(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		methodBuilder.ensureAnnotation(type.name());

		applyValues(builder, methodBuilder.forAnnotation(type.name()));
	}
}
