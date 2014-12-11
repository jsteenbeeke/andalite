package com.jeroensteenbeeke.andalite.analyzer.matchers;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

class CollectionItemMatcher<I, C extends Collection<I>> extends
		TypeSafeDiagnosingMatcher<C> {
	private final Matcher<I> elementMatcher;

	public CollectionItemMatcher(Matcher<I> elementMatcher) {
		this.elementMatcher = elementMatcher;
	}

	@Override
	protected boolean matchesSafely(C collection,
			Description mismatchDescription) {
		boolean isPastFirst = false;

		for (I item : collection) {
			if (elementMatcher.matches(item)) {
				return true;
			}

			if (isPastFirst) {
				mismatchDescription.appendText(", ");
			}
			elementMatcher.describeMismatch(item, mismatchDescription);
			isPastFirst = true;

		}

		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("a collection containing ").appendDescriptionOf(
				elementMatcher);
	}
}
