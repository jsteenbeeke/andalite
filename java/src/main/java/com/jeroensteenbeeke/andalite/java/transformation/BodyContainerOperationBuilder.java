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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.AfterStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ReturnStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEndReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.HasIfStatementOperation;

public class BodyContainerOperationBuilder extends
		AbstractOperationBuilder<IBodyContainer, IBodyContainerOperation> {
	BodyContainerOperationBuilder(IStepCollector collector,
			IJavaNavigation<IBodyContainer> navigation) {
		super(collector, navigation);
	}

	@Nonnull
	public IfStatementLocator inIfExpression() {
		return new IfStatementLocator(this);
	}

	@Nonnull
	public StatementOperationBuilder<ReturnStatement> forReturnStatement() {
		return new StatementOperationBuilder<ReturnStatement>(getCollector(),
				new ReturnStatementNavigation(getNavigation()));
	}

	@Nonnull
	public StatementOperationBuilder<AnalyzedStatement> afterStatement(
			String statement) {
		return new StatementOperationBuilder<AnalyzedStatement>(getCollector(),
				new AfterStatementNavigation(statement, getNavigation()));
	}

	public void ensureReturnAsLastStatement(@Nonnull String expression) {
		ensure(new EnsureEndReturnStatement(expression));
	}

	public void ensureStatement(@Nonnull String statement) {
		ensure(new EnsureStatement(statement));
	}

	public void ensureIfStatement(@Nonnull String condition) {
		ensure(new HasIfStatementOperation(condition));
	}
}
