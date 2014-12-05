package com.jeroensteenbeeke.andalite.transformation;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;

public class RecipeBuilder {
	private final List<RecipeStep<?>> steps;

	public RecipeBuilder() {
		this.steps = Lists.newArrayList();
	}

	<T extends Locatable> void addStep(Navigation<T> nav, Operation<T> oper) {
		this.steps.add(new RecipeStep<T>(nav, oper));
	}

	public CompilationUnitOperationBuilder atRoot() {
		return new CompilationUnitOperationBuilder(this);
	}

	public ClassScopeOperationBuilder inClass(ClassLocator locator) {
		return new ClassScopeOperationBuilder(this, locator.getNavigation());
	}

}
