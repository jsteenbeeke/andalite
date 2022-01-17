/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer.matchers;

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
