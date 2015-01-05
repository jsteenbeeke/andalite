package com.jeroensteenbeeke.andalite.transformation.navigation;

import com.jeroensteenbeeke.andalite.analyzer.IBodyContainer;

public class BodyContainerNavigation<T extends IBodyContainer> extends
		ChainedNavigation<T, IBodyContainer> {

	public BodyContainerNavigation(INavigation<T> chained) {
		super(chained);
	}

	@Override
	public IBodyContainer navigate(T chainedTarget) throws NavigationException {

		return (IBodyContainer) chainedTarget;
	}

	@Override
	public String getDescription() {

		return "body";
	}
}
