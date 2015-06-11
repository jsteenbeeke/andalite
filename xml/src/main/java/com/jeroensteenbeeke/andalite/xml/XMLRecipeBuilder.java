package com.jeroensteenbeeke.andalite.xml;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.xml.navigation.AnyElementNamedNavigation;
import com.jeroensteenbeeke.andalite.xml.navigation.RootElementNavigation;

public class XMLRecipeBuilder implements IStepCollector {
	private final List<XMLRecipeStep> steps;
	
	public XMLRecipeBuilder() {
		this.steps = Lists.newLinkedList();
	}
	
	public XMLElementContextBuilder atRoot() {
		return new XMLElementContextBuilder(this, new RootElementNavigation());
	}
	
	public XMLElementContextBuilder forAnyElement(String elementName) {
		return new XMLElementContextBuilder(this, new AnyElementNamedNavigation(elementName));
	}
	
	@Override
	public void addStep(@Nonnull XMLRecipeStep step) {
		this.steps.add(step);
	}
}
