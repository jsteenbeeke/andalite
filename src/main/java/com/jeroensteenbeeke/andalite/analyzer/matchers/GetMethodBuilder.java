package com.jeroensteenbeeke.andalite.analyzer.matchers;

import org.hamcrest.Matcher;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.ContainingDenomination;
import com.jeroensteenbeeke.andalite.transformation.AbstractMethodBuilder;

public class GetMethodBuilder extends
		AbstractMethodBuilder<Matcher<ContainingDenomination>> {
	public GetMethodBuilder() {
		super("void", AccessModifier.PUBLIC);
	}

	@Override
	public Matcher<ContainingDenomination> named(String name) {
		return new MethodMatcher(getModifier(), getType(), name,
				getDescriptors());
	}
}
