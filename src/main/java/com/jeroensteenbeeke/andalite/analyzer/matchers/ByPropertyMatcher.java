package com.jeroensteenbeeke.andalite.analyzer.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

abstract class ByPropertyMatcher<I, T> extends TypeSafeMatcher<I> {
	private final Matcher<T> delegateMatcher;

	public ByPropertyMatcher(Matcher<T> delegateMatcher) {
		super();
		this.delegateMatcher = delegateMatcher;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has property ").appendText(getProperty())
				.appendText(" which ");
		delegateMatcher.describeTo(description);
	}

	@Override
	protected boolean matchesSafely(I item) {

		return delegateMatcher.matches(transform(item));
	}

	protected abstract T transform(I item);

	protected abstract String getProperty();
}
