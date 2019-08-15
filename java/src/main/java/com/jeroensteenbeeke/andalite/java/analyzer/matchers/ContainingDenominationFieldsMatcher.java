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

import java.util.List;

import org.hamcrest.Matcher;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;

class ContainingDenominationFieldsMatcher extends
		ByPropertyMatcher<ContainingDenomination<?,?>, List<AnalyzedField>> {
	ContainingDenominationFieldsMatcher(
			Matcher<List<AnalyzedField>> delegateMatcher) {
		super(delegateMatcher);
	}

	@Override
	protected List<AnalyzedField> transform(ContainingDenomination<?,?> item) {
		return item.getFields();
	}

	@Override
	protected String getProperty() {
		return "fields";
	}
}
