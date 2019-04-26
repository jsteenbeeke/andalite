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

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;

public class AfterStatementNavigation<T extends IBodyContainer<T, ?>, S extends AnalyzedStatement<S,?>> extends
		ChainedNavigation<T, S> {
	private final String statement;

	public AfterStatementNavigation(String statement,
			IJavaNavigation<T> chained) {
		super(chained);
		this.statement = statement;
	}

	@Override
	@SuppressWarnings("unchecked")
	public S navigate(T chainedTarget)
			throws NavigationException {
		return chainedTarget
				.getStatements()
				.stream()
				.filter(s -> s.toJavaString().equals(statement))
				.map(s -> (S) s)
				.findFirst()
				.orElseThrow(
						() -> new NavigationException(String.format(
								"No statement %s in block", statement)));
	}

	@Override
	public String getStepDescription() {
		return String.format(" find statement %s in block ", statement);
	}

}
