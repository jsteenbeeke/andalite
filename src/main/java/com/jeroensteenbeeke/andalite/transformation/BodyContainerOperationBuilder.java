package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.IBodyContainerOperation;

public class BodyContainerOperationBuilder extends
		AbstractOperationBuilder<IBodyContainer, IBodyContainerOperation> {
	BodyContainerOperationBuilder(IStepCollector collector,
			INavigation<IBodyContainer> navigation) {
		super(collector, navigation);
	}
}
