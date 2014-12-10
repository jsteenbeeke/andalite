package com.jeroensteenbeeke.andalite.analyzer.matchers;

import javax.annotation.Nonnull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;

class ClassNameMatcher extends TypeSafeMatcher<AnalyzedClass> {
	private final String expectedName;

	public ClassNameMatcher(@Nonnull String expectedName) {
		this.expectedName = expectedName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has name ").appendText(expectedName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedClass item) {
		return expectedName.equals(item.getClassName());
	}

}
