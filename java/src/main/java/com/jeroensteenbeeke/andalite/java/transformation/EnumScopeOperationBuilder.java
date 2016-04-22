package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;

public class EnumScopeOperationBuilder extends
		AbstractOperationBuilder<AnalyzedEnum, IEnumOperation> {

	public EnumScopeOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedEnum> navigation) {
		super(collector, navigation);
	}

}
