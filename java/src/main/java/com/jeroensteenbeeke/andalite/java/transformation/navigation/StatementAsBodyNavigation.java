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
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;

public class StatementAsBodyNavigation<S extends AnalyzedStatement> extends
		ChainedNavigation<S, IBodyContainer> {
	public StatementAsBodyNavigation(IJavaNavigation<S> chained) {
		super(chained);
	}

	@Override
	public String getStepDescription() {
		return "assume it is a body statement";
	}

	public IBodyContainer navigate(S chainedTarget) throws NavigationException {
		if (chainedTarget instanceof IBodyContainer) {
			return (IBodyContainer) chainedTarget;
		}

		throw new NavigationException("Statement is not a body container");
	}

}
