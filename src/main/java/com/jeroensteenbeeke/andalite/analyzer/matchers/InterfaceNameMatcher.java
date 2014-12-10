package com.jeroensteenbeeke.andalite.analyzer.matchers;

import javax.annotation.Nonnull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;

class InterfaceNameMatcher extends TypeSafeMatcher<AnalyzedClass> {
	private final String expectedName;

	public InterfaceNameMatcher(@Nonnull String expectedName) {
		this.expectedName = expectedName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" implements interface ").appendText(
				expectedName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedClass item) {
		return item.getInterfaces().contains(expectedName);
	}

}
