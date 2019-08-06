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

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.StatementAsBodyNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureElseBlock;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureNextStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatementComment;

import javax.annotation.Nonnull;

public class IfStatementOperationBuilder<S extends AnalyzedStatement<? super S, ?>>
		extends AbstractOperationBuilder<S, IJavaOperation<S>> {

	public IfStatementOperationBuilder(IStepCollector collector,
									   IJavaNavigation<S> navigation) {
		super(collector, navigation);
	}

	@SuppressWarnings("unchecked")
	public void ensureElseBlock() {
		ensure((IJavaOperation<S>) new EnsureElseBlock());
	}

	@SuppressWarnings("unchecked")
	public <I extends Enum<I> & IInsertionPoint<S>> void ensurePrefixComment(@Nonnull String comment) {
		ensure((IJavaOperation<S>) new EnsureStatementComment<I>(comment, true));
	}

	@SuppressWarnings("unchecked")
	public <I extends Enum<I> & IInsertionPoint<S>> void ensureSuffixComment(@Nonnull String comment) {
		ensure((IJavaOperation<S>) new EnsureStatementComment<I>(comment,
																 false));
	}

}
