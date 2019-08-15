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

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BaseStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.AfterStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ReturnStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEndReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.HasIfStatementOperation;

public abstract class BodyContainerOperationBuilder<T extends IBodyContainer<T, I>, I extends Enum<I> & IInsertionPoint<T>> extends
		AbstractOperationBuilder<T, IBodyContainerOperation<T,I>> {
	BodyContainerOperationBuilder(IStepCollector collector,
			IJavaNavigation<T> navigation) {
		super(collector, navigation);
	}

	@Nonnull
	public IfStatementLocator<T,I> inIfStatement() {
		return new IfStatementLocator<>(this);
	}

	@Nonnull
	public StatementOperationBuilder<ReturnStatement> forReturnStatement() {
		return new StatementOperationBuilder<>(getCollector(),
											   new ReturnStatementNavigation<>(getNavigation()));
	}

	@Nonnull
	public <S extends AnalyzedStatement<S,?>> StatementOperationBuilder<S> afterStatement(
			String statement) {
		String stmt = statement.endsWith(";") ? statement.substring(0, statement.length()-1) : statement;

		return new StatementOperationBuilder<S>(getCollector(),
				new AfterStatementNavigation<>(stmt, getNavigation()));
	}

	public void ensureReturnAsLastStatement(@Nonnull String expression) {
		ensure(new EnsureEndReturnStatement<>(expression) {

			@Override
			public I getLastStatementLocation() {
				return BodyContainerOperationBuilder.this.getLastStatementLocation();
			}
		});
	}

	public EnsureStatement<T,I> ensureStatement(@Nonnull String statement) {
		EnsureStatement<T, I> ensureStatement = new EnsureStatement<>(statement) {

			@Override
			public I getLastStatementLocation() {
				return BodyContainerOperationBuilder.this.getLastStatementLocation();
			}
		};
		ensure(ensureStatement);

		return ensureStatement;
	}

	public void ensureIfStatement(@Nonnull String condition) {
		ensure(new HasIfStatementOperation<>(condition));
	}

	public abstract I getLastStatementLocation();
}
