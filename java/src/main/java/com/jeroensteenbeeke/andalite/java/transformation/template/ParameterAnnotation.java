package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterScopeOperationBuilder;

import org.jetbrains.annotations.NotNull;

public class ParameterAnnotation extends AnnotationAdditionTemplate<ParameterAnnotation> implements ParameterElementTemplate {
	private final TypeReference type;

	ParameterAnnotation(@NotNull TypeReference type) {
		this(type, ImmutableList.of());
	}

	private ParameterAnnotation(@NotNull TypeReference type, @NotNull ImmutableList<AnnotationValueTemplate<?>> values) {
		super(values);
		this.type = type;
	}

	@NotNull
	@Override
	protected ParameterAnnotation newInstance(@NotNull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new ParameterAnnotation(type, templates);
	}

	@Override
	public void onParameter(JavaRecipeBuilder builder, ParameterScopeOperationBuilder parameterBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		parameterBuilder.ensureAnnotation(type.name());

		applyValues(builder, parameterBuilder.forAnnotation(type.name()));
	}
}
