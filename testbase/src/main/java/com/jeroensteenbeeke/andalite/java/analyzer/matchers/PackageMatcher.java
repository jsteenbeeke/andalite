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

import org.jetbrains.annotations.NotNull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

class PackageMatcher extends TypeSafeDiagnosingMatcher<AnalyzedSourceFile> {
	private final String expectedName;

	public PackageMatcher(@NotNull String expectedName) {
		this.expectedName = expectedName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" in package ").appendText(expectedName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedSourceFile item,
			Description mismatchDescription) {
		boolean match = expectedName.equals(item.getPackageName());

		if (!match) {
			mismatchDescription.appendText("in package ").appendText(
					item.getPackageName());
		}

		return match;
	}

}
