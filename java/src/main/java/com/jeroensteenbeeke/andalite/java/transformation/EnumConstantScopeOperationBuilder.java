package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnumConstant;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumConstantOperation;

import javax.annotation.Nonnull;

public class EnumConstantScopeOperationBuilder extends AbstractOperationBuilder<AnalyzedEnumConstant, IEnumConstantOperation> {
	protected EnumConstantScopeOperationBuilder(IStepCollector collector, IJavaNavigation<AnalyzedEnumConstant> navigation) {
		super(collector, navigation);


	}

	public EnsureEnumConstantMethodBuilder ensureMethod() {
		return new EnsureEnumConstantMethodBuilder(this::ensure);
	}


	@Nonnull
	public EnumConstantMethodLocator forMethod() {
		return new EnumConstantMethodLocator(getCollector(), getNavigation());
	}

}
