package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.ClassScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public interface ClassElementTemplate {
	void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder classScopeOperationBuilder);
}
