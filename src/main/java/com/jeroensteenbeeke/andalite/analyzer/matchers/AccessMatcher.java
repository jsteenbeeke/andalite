package com.jeroensteenbeeke.andalite.analyzer.matchers;

import javax.annotation.Nonnull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifiable;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;

class AccessMatcher extends TypeSafeMatcher<AccessModifiable> {
	private final AccessModifier expected;

	public AccessMatcher(@Nonnull AccessModifier expected) {
		super();
		this.expected = expected;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has modifier ").appendText(
				expected.getOutput());
	}

	@Override
	protected boolean matchesSafely(AccessModifiable item) {
		return expected.equals(item.getAccessModifier());
	}
}
