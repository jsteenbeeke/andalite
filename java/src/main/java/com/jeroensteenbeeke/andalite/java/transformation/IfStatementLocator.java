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
package com.jeroensteenbeeke.andalite.java.transformation;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.java.transformation.IfStatementLocator.IfStatementLocatorTerminator;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ByExpressionIfStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ElseStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ThenStatementNavigation;

public class IfStatementLocator<T extends IBodyContainer<T, I>, I extends Enum<I> & IInsertionPoint<T>> extends
		AbstractExpressionMatcher<IfStatement, IfStatementLocatorTerminator> {

	private BodyContainerOperationBuilder<T,I> body;

	public IfStatementLocator(BodyContainerOperationBuilder<T,I> body) {
		this.body = body;
	}

	public IfStatementLocatorTerminator withExpression(
			@NotNull String expression) {
		return nextInChain(new ByExpressionIfStatementNavigation<>(
				body.getNavigation(), expression));
	}

	@Override
	protected IfStatementLocatorTerminator nextInChain(
			IJavaNavigation<IfStatement> statementNavigation) {
		return new IfStatementLocatorTerminator(body.getCollector(),
				statementNavigation);
	}

	public static class IfStatementLocatorTerminator {
		private final IStepCollector collector;

		private final IJavaNavigation<IfStatement> statementNavigation;

		private IfStatementLocatorTerminator(IStepCollector collector,
				IJavaNavigation<IfStatement> statementNavigation) {
			this.collector = collector;
			this.statementNavigation = statementNavigation;
		}

		public <S extends AnalyzedStatement<S,?>> StatementOperationBuilder<S> thenStatement() {
			return new StatementOperationBuilder<S>(collector,
												   new ThenStatementNavigation<>(statementNavigation));
		}

		public  <S extends AnalyzedStatement<S,?>> StatementOperationBuilder<S> elseStatement() {
			return new StatementOperationBuilder<S>(collector,
													new ElseStatementNavigation<>(statementNavigation));
		}

		public IfStatementOperationBuilder<IfStatement> wholeStatement() {
			return new IfStatementOperationBuilder<>(collector,
													 statementNavigation);
		}

	}

}
