package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.ConstructorOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public interface ConstructorElementTemplate {
	void onConstructor(JavaRecipeBuilder builder, ConstructorOperationBuilder constructorOperationBuilder);
}
