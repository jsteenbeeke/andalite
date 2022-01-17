package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import org.jetbrains.annotations.NotNull;

public abstract class AnnotationAdditionTemplate<T extends AnnotationAdditionTemplate<T>> {

	protected final ImmutableList<AnnotationValueTemplate<?>> values;

	protected AnnotationAdditionTemplate(@NotNull ImmutableList<AnnotationValueTemplate<?>> values) {
		this.values = values;
	}

	@NotNull
	public T withValues(@NotNull AnnotationValueTemplate<?>... templates) {
		return newInstance(ImmutableList.<AnnotationValueTemplate<?>>builder()
							   .addAll(values)
							   .addAll(ImmutableList.copyOf(templates))
							   .build());
	}

	@NotNull
	protected abstract T newInstance(@NotNull ImmutableList<AnnotationValueTemplate<?>> templates);

	protected final void applyValues(JavaRecipeBuilder builder, IAnnotationOperationBuilder<?, ?> annotationOperationBuilder) {
		values.forEach(v -> v.apply(builder, annotationOperationBuilder));
	}
}
