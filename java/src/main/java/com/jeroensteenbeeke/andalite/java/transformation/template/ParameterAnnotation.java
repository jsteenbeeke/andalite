package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterScopeOperationBuilder;

import javax.annotation.Nonnull;

public class ParameterAnnotation extends AnnotationAdditionTemplate<ParameterAnnotation> implements ParameterElementTemplate {
	private final TypeReference type;

	ParameterAnnotation(@Nonnull TypeReference type) {
		this(type, ImmutableList.of());
	}

	private ParameterAnnotation(@Nonnull TypeReference type, @Nonnull ImmutableList<AnnotationValueTemplate<?>> values) {
		super(values);
		this.type = type;
	}

	@Nonnull
	@Override
	protected ParameterAnnotation newInstance(@Nonnull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new ParameterAnnotation(type, templates);
	}

	@Override
	public void onParameter(JavaRecipeBuilder builder, ParameterScopeOperationBuilder parameterBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		parameterBuilder.ensureAnnotation(type.name());

		applyValues(builder, parameterBuilder.forAnnotation(type.name()));
	}
}
