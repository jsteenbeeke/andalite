package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.FieldOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class Initializer implements PropertyElementTemplate {
	private final String expression;

	Initializer(String expression) {
		this.expression = expression;
	}

	@Override
	public void onField(JavaRecipeBuilder builder, FieldOperationBuilder fieldBuilder) {
		fieldBuilder.ensureInitialization(expression);
	}
}
