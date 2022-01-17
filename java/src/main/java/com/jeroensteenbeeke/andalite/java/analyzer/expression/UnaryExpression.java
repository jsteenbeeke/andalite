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
package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;

public class UnaryExpression extends AnalyzedExpression {
	public static enum Operator {
		POSITIVE("+%s"), // +
		NEGATIVE("-%s"), // -
		PRE_INCREMENT("++%s"), // ++
		PRE_DECREMENT("--%s"), // --
		NOT("!%s"), // !
		INVERSE("~%s"), // ~
		POST_INCREMENT("%s++"), // ++
		POST_DECREMENT("%s--"); // --
		private final String format;

		private Operator(String format) {
			this.format = format;
		}

		public String toJavaString(AnalyzedExpression expression) {
			return String.format(format, expression.toJavaString());
		}
	}

	private final Operator operator;

	private final AnalyzedExpression expression;

	public UnaryExpression(@NotNull Location location,
			@NotNull Operator operator, @NotNull AnalyzedExpression expression) {
		super(location);
		this.operator = operator;
		this.expression = expression;
	}

	public AnalyzedExpression getExpression() {
		return expression;
	}

	public Operator getOperator() {
		return operator;
	}

	@Override
	public String toJavaString() {
		return operator.toJavaString(expression);
	}

}
