package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.ConstructorOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public class Statement implements ConstructorElementTemplate, MethodElementTemplate{
	private final String statement;

	Statement(String statement) {
		this.statement = statement;
	}

	@Override
	public void onConstructor(JavaRecipeBuilder builder, ConstructorOperationBuilder constructorOperationBuilder) {
		constructorOperationBuilder.inBody().ensureStatement(statement);
	}

	@Override
	public void onMethod(@Nonnull JavaRecipeBuilder builder, @Nonnull MethodOperationBuilder methodBuilder) {
		methodBuilder.inBody().ensureStatement(statement);
	}
}
