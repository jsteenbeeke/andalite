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
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IStatementOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureNextStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatementComment;

@Deprecated
public class Operations {

	private Operations() {

	}

	public static IStatementOperation hasNextStatement(
			@Nonnull String statement) {
		return new EnsureNextStatement(statement);
	}

	public static <S extends AnalyzedStatement> IJavaOperation<S> hasPrefixComment(
			@Nonnull String comment) {
		return new EnsureStatementComment<S>(comment, true);
	}

	public static <S extends AnalyzedStatement> IJavaOperation<S> hasSuffixComment(
			@Nonnull String comment) {
		return new EnsureStatementComment<S>(comment, false);
	}
}
