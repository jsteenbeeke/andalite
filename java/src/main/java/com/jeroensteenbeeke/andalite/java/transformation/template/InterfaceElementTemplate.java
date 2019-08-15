package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.InterfaceScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public interface InterfaceElementTemplate {
	void onInterface(JavaRecipeBuilder builder, InterfaceScopeOperationBuilder interfaceScopeOperationBuilder);
}
