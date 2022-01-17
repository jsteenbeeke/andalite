package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import org.jetbrains.annotations.NotNull;

public class Returns implements MethodElementTemplate{
	private final String expression;

	Returns(String expression) {
		this.expression = expression;
	}

	@Override
	public void onMethod(@NotNull JavaRecipeBuilder builder, @NotNull MethodOperationBuilder methodBuilder) {
		methodBuilder.inBody().ensureReturnAsLastStatement(expression);
	}
}
