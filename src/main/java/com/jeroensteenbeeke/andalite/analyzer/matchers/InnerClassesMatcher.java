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

import java.util.List;

import org.hamcrest.Matcher;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;

class InnerClassesMatcher extends
		ByPropertyMatcher<AnalyzedClass, List<AnalyzedClass>> {
	InnerClassesMatcher(Matcher<List<AnalyzedClass>> delegateMatcher) {
		super(delegateMatcher);
	}

	@Override
	protected List<AnalyzedClass> transform(AnalyzedClass item) {
		return item.getInnerClasses();
	}

	@Override
	protected String getProperty() {
		return "inner classes";
	}
}