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

import java.util.List;
import java.util.stream.Collectors;

import com.jeroensteenbeeke.andalite.java.analyzer.GenerifiedName;
import org.hamcrest.Matcher;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;

class ContainingDenominationInterfacesMatcher extends
	ByPropertyMatcher<ContainingDenomination<?, ?>, List<String>> {
	ContainingDenominationInterfacesMatcher(
		Matcher<List<String>> delegateMatcher) {
		super(delegateMatcher);
	}

	@Override
	protected List<String> transform(ContainingDenomination<?, ?> item) {
		return ImmutableList.copyOf(item
										.getInterfaces()
										.stream()
										.map(GenerifiedName::getName)
										.collect(Collectors.toList()));
	}

	@Override
	protected String getProperty() {
		return "interfaces";
	}
}
