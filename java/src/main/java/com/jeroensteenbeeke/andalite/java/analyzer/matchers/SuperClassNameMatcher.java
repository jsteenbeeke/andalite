/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer.matchers;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.java.analyzer.GenerifiedName;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;

import java.util.Optional;

class SuperClassNameMatcher extends TypeSafeDiagnosingMatcher<AnalyzedClass> {
	private final String expectedName;

	public SuperClassNameMatcher(@Nonnull String expectedName) {
		this.expectedName = expectedName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has superclass ").appendText(expectedName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedClass item,
									Description mismatchDescription) {

		Optional<GenerifiedName> actualSuperclass = item.getSuperClass();
		boolean match = actualSuperclass.map(GenerifiedName::getName).filter(expectedName::equals).isPresent();

		if (!match) {
			actualSuperclass
				.map(GenerifiedName::getName)
				.ifPresentOrElse(a -> mismatchDescription.appendText("has superclass ").appendText(
					a), () -> mismatchDescription
					.appendText("has no superclass (i.e. java.lang.Object)"));
		}

		return match;
	}

}
