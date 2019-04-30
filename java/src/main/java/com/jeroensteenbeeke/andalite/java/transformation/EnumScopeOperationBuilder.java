package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.EnumConstantNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;

import javax.annotation.Nonnull;

public class EnumScopeOperationBuilder
		extends AbstractOperationBuilder<AnalyzedEnum, IEnumOperation> {

	public EnumScopeOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedEnum> navigation) {
		super(collector, navigation);
	}

	public HasEnumConstantBuilder ensureEnumConstant() {
		return new HasEnumConstantBuilder(this::ensure);
	}

	public EnsureEnumMethodBuilder ensureMethod() {
		return new EnsureEnumMethodBuilder(this::ensure);
	}

	@Nonnull
	public EnumMethodLocator forMethod() {
		return new EnumMethodLocator(getCollector(), getNavigation());
	}

	public EnumConstantScopeOperationBuilder forConstant(@Nonnull String constantName) {
		return new EnumConstantScopeOperationBuilder(getCollector(), new EnumConstantNavigation(getNavigation(), constantName));
	}
}
