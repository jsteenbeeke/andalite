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
package com.jeroensteenbeeke.andalite.analyzer.expression;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;

public class BinaryExpression extends AnalyzedExpression {
	public static enum Operator {
		OR("||"), AND("&&"), BINARY_OR("|"), BINARY_AND("&"), XOR("^"), EQUALS(
				"=="), NOTEQUALS("!="), LESS("<"), GREATER(">"), LESS_EQUALS(
				"<="), GREATER_EQUALS(">="), LSHIFT("<<"), SIGNED_RSHIFT(">>"), UNSIGNED_RSHIFT(
				">>>"), PLUS("+"), MINUS("-"), TIMES("*"), DIVIDE("/"), REMAINDER(
				"%");

		private final String javaRepresentation;

		private Operator(@Nonnull String javaRepresentation) {
			this.javaRepresentation = javaRepresentation;
		}

		@Nonnull
		public String getJavaRepresentation() {
			return javaRepresentation;
		}
	}

	private final AnalyzedExpression left;

	private final Operator operator;

	private final AnalyzedExpression right;

	public BinaryExpression(@Nonnull Location location,
			@Nonnull AnalyzedExpression left, @Nonnull Operator operator,
			@Nonnull AnalyzedExpression right) {
		super(location);
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Nonnull
	public AnalyzedExpression getLeft() {
		return left;
	}

	@Nonnull
	public Operator getOperator() {
		return operator;
	}

	@Nonnull
	public AnalyzedExpression getRight() {
		return right;
	}

	@Override
	public String toJavaString() {
		return String.format("%s %s %s", left.toJavaString(),
				operator.getJavaRepresentation(), right.toJavaString());
	}

}
