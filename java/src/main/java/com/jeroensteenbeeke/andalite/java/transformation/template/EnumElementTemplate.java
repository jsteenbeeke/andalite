package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.EnumScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public interface EnumElementTemplate {
	void onEnum(JavaRecipeBuilder builder, EnumScopeOperationBuilder enumBuilder);
}
