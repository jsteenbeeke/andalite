package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.ClassScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.InterfaceScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class EnsuredImport implements ClassElementTemplate, InterfaceElementTemplate {
	private final TypeReference reference;

	EnsuredImport(TypeReference reference) {
		this.reference = reference;
	}

	@Override
	public void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder classScopeOperationBuilder) {
		reference.importStatement().ifPresent(builder::ensureImport);
	}

	@Override
	public void onInterface(JavaRecipeBuilder builder, InterfaceScopeOperationBuilder interfaceScopeOperationBuilder) {
		reference.importStatement().ifPresent(builder::ensureImport);
	}
}
