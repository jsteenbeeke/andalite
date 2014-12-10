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
