package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.transformation.navigation.CompilationUnitNavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;

class CompilationUnitOperationBuilder implements
		ScopedOperationBuilder<AnalyzedSourceFile, CompilationUnitOperation> {
	private final RecipeBuilder parent;

	CompilationUnitOperationBuilder(RecipeBuilder parent) {
		super();
		this.parent = parent;
	}

	public void ensure(CompilationUnitOperation compilationUnitOperation) {
		parent.addStep(CompilationUnitNavigation.getInstance(),
				compilationUnitOperation);
	}
}
