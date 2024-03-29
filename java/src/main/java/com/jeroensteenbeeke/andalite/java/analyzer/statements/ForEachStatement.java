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
package com.jeroensteenbeeke.andalite.java.analyzer.statements;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.expression.VariableDeclarationExpression;

public class ForEachStatement extends BaseStatement<ForEachStatement> {
	private final VariableDeclarationExpression variableExpression;

	private final AnalyzedExpression iterable;

	private final AnalyzedStatement<?,?> body;

	public ForEachStatement(@NotNull Location location,
			@NotNull VariableDeclarationExpression variableExpression,
			@NotNull AnalyzedExpression iterable,
			@NotNull AnalyzedStatement<?,?> body) {
		super(location);
		this.variableExpression = variableExpression;
		this.iterable = iterable;
		this.body = body;
	}

	@NotNull
	public AnalyzedStatement<?,?> getBody() {
		return body;
	}

	@NotNull
	public AnalyzedExpression getIterable() {
		return iterable;
	}

	@NotNull
	public VariableDeclarationExpression getVariableExpression() {
		return variableExpression;
	}

	@Override
	public String toJavaString() {
		return String.format("for (%s: %s) %s",
				variableExpression.toJavaString(), iterable.toJavaString(),
				body.toJavaString());
	}

}
