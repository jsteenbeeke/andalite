package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;

public class ClassScopeOperationBuilder implements
		ScopedOperationBuilder<AnalyzedClass, ClassOperation> {

	private final RecipeBuilder parent;

	private final Navigation<AnalyzedClass> navigation;

	ClassScopeOperationBuilder(RecipeBuilder recipeBuilder,
			Navigation<AnalyzedClass> navigation) {
		this.parent = recipeBuilder;
		this.navigation = navigation;
	}

	@Override
	public void ensure(ClassOperation operation) {
		parent.addStep(navigation, operation);
	}
}
