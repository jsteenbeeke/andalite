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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedImport;

class ImportMatcher extends TypeSafeDiagnosingMatcher<AnalyzedImport> {
	private final String expectedClassName;

	public ImportMatcher(String expectedClassName) {
		super();
		this.expectedClassName = expectedClassName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("an import of ").appendText(expectedClassName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedImport item,
			Description mismatchDescription) {
		boolean match = item.matchesClass(expectedClassName);

		if (!match) {
			mismatchDescription.appendText("which imports ").appendText(
					item.getStatement());
		}

		return match;
	}
}
