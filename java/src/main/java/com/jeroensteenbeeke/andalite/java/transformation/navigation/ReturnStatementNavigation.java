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
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;

public class ReturnStatementNavigation extends
		ChainedNavigation<IBodyContainer, ReturnStatement> {
	public ReturnStatementNavigation(IJavaNavigation<IBodyContainer> chained) {
		super(chained);
	}

	@Override
	public ReturnStatement navigate(IBodyContainer chainedTarget)
			throws NavigationException {
		return (ReturnStatement) chainedTarget
				.getStatements()
				.stream()
				.filter(s -> s instanceof ReturnStatement)
				.findAny()
				.orElseThrow(
						() -> new NavigationException(
								"No return statement in block"));
	}

	@Override
	public String getStepDescription() {
		return " find return statement in block";
	}

}
