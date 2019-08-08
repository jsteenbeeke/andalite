package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableSet;
import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public abstract class AnnotationAdditionTemplate<T extends AnnotationAdditionTemplate<T>> {

	private final ImmutableSet<AnnotationValueTemplate<?>> values;

	protected AnnotationAdditionTemplate(@Nonnull ImmutableSet<AnnotationValueTemplate<?>> values) {
		this.values = values;
	}

	@Nonnull
	public T withValues(@Nonnull AnnotationValueTemplate<?>... templates) {
		return newInstance(ImmutableSet.<AnnotationValueTemplate<?>>builder()
							   .addAll(values)
							   .addAll(ImmutableSet.copyOf(templates))
							   .build());
	}

	@Nonnull
	protected abstract T newInstance(@Nonnull ImmutableSet<AnnotationValueTemplate<?>> templates);

	protected final void applyValues(JavaRecipeBuilder builder, IAnnotationOperationBuilder<?, ?> annotationOperationBuilder) {
		values.forEach(v -> v.apply(builder, annotationOperationBuilder));
	}
}
