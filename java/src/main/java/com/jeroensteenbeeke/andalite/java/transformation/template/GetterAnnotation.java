package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableSet;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public class GetterAnnotation extends AnnotationAdditionTemplate<GetterAnnotation> implements PropertyElementTemplate {
	private final TypeReference type;

	GetterAnnotation(TypeReference type) {
		this(type, ImmutableSet.of());
	}

	private GetterAnnotation(TypeReference type, ImmutableSet<AnnotationValueTemplate<?>> templates) {
		super(templates);
		this.type = type;
	}

	@Nonnull
	@Override
	protected GetterAnnotation newInstance(@Nonnull ImmutableSet<AnnotationValueTemplate<?>> templates) {
		return new GetterAnnotation(type, templates);
	}

	@Override
	public void onGetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		methodBuilder.ensureAnnotation(type.name());

		applyValues(builder, methodBuilder.forAnnotation(type.name()));
	}
}
