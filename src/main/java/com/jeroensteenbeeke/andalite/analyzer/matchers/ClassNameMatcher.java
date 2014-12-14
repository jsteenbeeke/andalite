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

import javax.annotation.Nonnull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;

class ClassNameMatcher extends TypeSafeDiagnosingMatcher<AnalyzedClass> {
	private final String expectedName;

	public ClassNameMatcher(@Nonnull String expectedName) {
		this.expectedName = expectedName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has name ").appendText(expectedName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedClass item,
			Description mismatchDescription) {
		boolean match = expectedName.equals(item.getClassName());

		if (!match) {
			mismatchDescription.appendText(" is not named ").appendText(
					expectedName);
		}

		return match;
	}

}