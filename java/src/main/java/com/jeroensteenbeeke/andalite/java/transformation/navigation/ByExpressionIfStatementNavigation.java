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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;

public class ByExpressionIfStatementNavigation extends
		ChainedNavigation<IBodyContainer, IfStatement> {
	private static final Logger logger = LoggerFactory
			.getLogger(ByExpressionIfStatementNavigation.class);

	private final String expression;

	public ByExpressionIfStatementNavigation(
			IJavaNavigation<IBodyContainer> chained, String expression) {
		super(chained);
		this.expression = expression;
	}

	@Override
	public IfStatement navigate(IBodyContainer chainedTarget)
			throws NavigationException {
		for (AnalyzedStatement analyzedStatement : chainedTarget
				.getStatements()) {
			if (analyzedStatement instanceof IfStatement) {
				IfStatement stmt = (IfStatement) analyzedStatement;

				AnalyzedExpression condition = stmt.getCondition();

				logger.debug("found if-statement with condition {}",
						condition != null ? condition.toJavaString() : "null");

				if (condition != null
						&& expression.equals(condition.toJavaString())) {
					return stmt;
				}

			}
		}

		throw new NavigationException(
				"No if statement with expression %s in body", expression);
	}

	@Override
	public String getStepDescription() {
		return String.format("to the first if-statement with expression %s",
				expression);
	}

}
