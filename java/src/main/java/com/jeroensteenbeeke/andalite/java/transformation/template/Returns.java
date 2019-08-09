package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public class Returns implements MethodElementTemplate{
	private final String expression;

	Returns(String expression) {
		this.expression = expression;
	}

	@Override
	public void onMethod(@Nonnull JavaRecipeBuilder builder, @Nonnull MethodOperationBuilder methodBuilder) {
		methodBuilder.inBody().ensureReturnAsLastStatement(expression);
	}
}
