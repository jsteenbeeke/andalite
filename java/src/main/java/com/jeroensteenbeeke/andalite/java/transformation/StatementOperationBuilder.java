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
package com.jeroensteenbeeke.andalite.java.transformation;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.StatementAsBodyNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureNextStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatementComment;

public class StatementOperationBuilder<S extends AnalyzedStatement<? super S, ?>>
		extends AbstractOperationBuilder<S, IJavaOperation<S>> {

	public StatementOperationBuilder(IStepCollector collector,
									 IJavaNavigation<S> navigation) {
		super(collector, navigation);
	}

	public BodyContainerOperationBuilder<?,?> body() {
		return new BodyContainerOperationBuilder<>(getCollector(),
												   new StatementAsBodyNavigation<S>(getNavigation())) {
			@Override
			public BlockStatement.BlockStatementInsertionPoint getLastStatementLocation() {
				return BlockStatement.BlockStatementInsertionPoint.END_OF_BODY;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public void ensureNextStatement(@NotNull String statement) {
		ensure((IJavaOperation<S>) new EnsureNextStatement<>(statement));
	}

	@SuppressWarnings("unchecked")
	public <I extends Enum<I> & IInsertionPoint<S>> void ensurePrefixComment(@NotNull String comment) {
		ensure((IJavaOperation<S>) new EnsureStatementComment<I>(comment, true));
	}

	@SuppressWarnings("unchecked")
	public <I extends Enum<I> & IInsertionPoint<S>> void ensureSuffixComment(@NotNull String comment) {
		ensure((IJavaOperation<S>) new EnsureStatementComment<I>(comment,
																 false));
	}

}
