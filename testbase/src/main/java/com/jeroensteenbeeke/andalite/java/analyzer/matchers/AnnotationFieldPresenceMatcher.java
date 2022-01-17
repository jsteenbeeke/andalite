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

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

public class AnnotationFieldPresenceMatcher extends
		TypeSafeDiagnosingMatcher<AnalyzedAnnotation> {
	private final String name;

	private final Class<? extends BaseValue<?,?,?>> type;

	AnnotationFieldPresenceMatcher(String name,
			Class<? extends BaseValue<?,?,?>> type) {
		super();
		this.name = name;
		this.type = type;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format(" has value '%s' of type %s",
				name, type.getName()));
	}

	@Override
	protected boolean matchesSafely(AnalyzedAnnotation item,
			Description mismatchDescription) {
		if (item.hasValueNamed(name)) {
			if (item.hasValueOfType(type, name)) {
				return true;
			}

			mismatchDescription.appendText(String.format(
					" value named '%s' has type %s", name,
					item.getValueType(name)));
		} else {
			mismatchDescription.appendText(String.format(
					" no value named '%s'", name));
		}

		return false;
	}
}
