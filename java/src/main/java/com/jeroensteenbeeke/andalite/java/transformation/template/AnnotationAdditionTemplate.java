package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public abstract class AnnotationAdditionTemplate<T extends AnnotationAdditionTemplate<T>> {

	protected final ImmutableList<AnnotationValueTemplate<?>> values;

	protected AnnotationAdditionTemplate(@Nonnull ImmutableList<AnnotationValueTemplate<?>> values) {
		this.values = values;
	}

	@Nonnull
	public T withValues(@Nonnull AnnotationValueTemplate<?>... templates) {
		return newInstance(ImmutableList.<AnnotationValueTemplate<?>>builder()
							   .addAll(values)
							   .addAll(ImmutableList.copyOf(templates))
							   .build());
	}

	@Nonnull
	protected abstract T newInstance(@Nonnull ImmutableList<AnnotationValueTemplate<?>> templates);

	protected final void applyValues(JavaRecipeBuilder builder, IAnnotationOperationBuilder<?, ?> annotationOperationBuilder) {
		values.forEach(v -> v.apply(builder, annotationOperationBuilder));
	}
}
