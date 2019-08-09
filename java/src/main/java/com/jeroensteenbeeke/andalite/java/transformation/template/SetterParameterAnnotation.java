package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public class SetterParameterAnnotation extends AnnotationAdditionTemplate<SetterParameterAnnotation> implements PropertyElementTemplate {
	private final TypeReference type;

	SetterParameterAnnotation(@Nonnull TypeReference type) {
		this(type, ImmutableList.of());
	}

	private SetterParameterAnnotation(@Nonnull TypeReference type, @Nonnull ImmutableList<AnnotationValueTemplate<?>> values) {
		super(values);
		this.type = type;
	}

	@Nonnull
	@Override
	protected SetterParameterAnnotation newInstance(@Nonnull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new SetterParameterAnnotation(type, templates);
	}

	@Override
	public void onSetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		methodBuilder.forParameterAtIndex(0).ensureAnnotation(type.name());

		applyValues(builder, methodBuilder.forParameterAtIndex(0).forAnnotation(type.name()));
	}
}
