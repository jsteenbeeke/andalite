package com.jeroensteenbeeke.andalite.xml;

import javax.annotation.Nonnull;


public abstract class AbstractContextBuilder<T extends IXMLOperation> implements IStepCollector, IOperationReceiver<T> {
	private final IStepCollector collector;
	
	private final IXMLNavigation navigation;

	protected AbstractContextBuilder(@Nonnull IStepCollector collector, @Nonnull IXMLNavigation navigation) {
		super();
		this.collector = collector;
		this.navigation = navigation;
	}
	
	@Override
	public void ensure(@Nonnull T operation) {
		addStep(new XMLRecipeStep(navigation, operation));
	}
	
	@Nonnull 
	public IXMLNavigation getNavigation() {
		return navigation;
	}
	
	@Override
	public void addStep(@Nonnull XMLRecipeStep step) {
		collector.addStep(step);
	}
}
