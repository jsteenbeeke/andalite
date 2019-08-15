package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.IAnnotationOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class AnnotationValueTemplate<T> {
	private final String name;

	private final Invocation<T> invocation;

	private final T value;

	public AnnotationValueTemplate(String name, T value, Invocation<T> invocation) {
		this.name = name;
		this.value = value;
		this.invocation = invocation;
	}

	public void onBeforeApply(JavaRecipeBuilder builder, IAnnotationOperationBuilder<?, ?> operationBuilder) {

	}

	public final void apply(JavaRecipeBuilder builder, IAnnotationOperationBuilder<?, ?> operationBuilder) {
		onBeforeApply(builder, operationBuilder);
		invocation.invoke(operationBuilder, name, value);
	}

	@FunctionalInterface
	public interface Invocation<T> {
		void invoke(IAnnotationOperationBuilder<?,?> builder, String name, T value);
	}
}
