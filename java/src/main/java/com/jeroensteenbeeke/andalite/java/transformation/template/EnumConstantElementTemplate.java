package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.EnumConstantScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.EnumScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public interface EnumConstantElementTemplate {
	void onEnumConstant(JavaRecipeBuilder builder,
		EnumConstantScopeOperationBuilder enumConstantBuilder);
}
