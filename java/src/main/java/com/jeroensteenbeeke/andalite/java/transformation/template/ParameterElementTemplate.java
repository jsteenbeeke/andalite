package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterScopeOperationBuilder;

public interface ParameterElementTemplate {
	void onParameter(JavaRecipeBuilder builder, ParameterScopeOperationBuilder parameterScopeOperationBuilder);
}
