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
import org.hamcrest.TypeSafeDiagnosingMatcher;

class SizeMatcher<I, T extends Collection<I>> extends
		TypeSafeDiagnosingMatcher<T> {
	private final int expectedSize;

	public SizeMatcher(int expectedSize) {
		super();
		this.expectedSize = expectedSize;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has size ").appendValue(expectedSize);
	}

	@Override
	protected boolean matchesSafely(T item, Description mismatchDescription) {

		boolean match = expectedSize == item.size();

		if (!match) {
			mismatchDescription.appendText("has size ")
					.appendValue(item.size());
		}

		return match;
	}

}
