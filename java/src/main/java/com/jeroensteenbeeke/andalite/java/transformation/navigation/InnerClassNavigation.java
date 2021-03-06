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
package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;

public class InnerClassNavigation<S extends ContainingDenomination> extends
		ChainedNavigation<S, AnalyzedClass> {
	private final String className;

	public InnerClassNavigation(IJavaNavigation<S> chained, String className) {
		super(chained);
		this.className = className;
	}

	@Override
	public AnalyzedClass navigate(S chainedTarget) throws NavigationException {
		if (chainedTarget.hasInnerClass(className)) {
			return chainedTarget.getInnerClass(className);
		}

		throw new NavigationException(String.format(
				"Denomination %s has no inner class named %s",
				chainedTarget.getDenominationName(), className));
	}

	@Override
	public String getStepDescription() {

		return String.format("Go to inner class %s", className);
	}
}
