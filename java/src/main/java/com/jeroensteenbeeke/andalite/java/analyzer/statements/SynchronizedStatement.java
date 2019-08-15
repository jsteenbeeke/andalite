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

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;

public class SynchronizedStatement extends BaseStatement<SynchronizedStatement> {

	private final AnalyzedExpression expression;

	private final AnalyzedStatement<?,?> statement;

	public SynchronizedStatement(Location from, AnalyzedExpression expression,
			AnalyzedStatement<?,?> statement) {
		super(from);
		this.expression = expression;
		this.statement = statement;
	}

	public AnalyzedExpression getExpression() {
		return expression;
	}

	public AnalyzedStatement<?,?> getStatement() {
		return statement;
	}

	@Override
	public String toJavaString() {
		return String.format("synchronized(%s) %s", expression.toJavaString(),
				statement.toJavaString());
	}

}
