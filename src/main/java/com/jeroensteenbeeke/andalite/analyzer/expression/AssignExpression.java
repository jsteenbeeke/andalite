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

public class AssignExpression extends AnalyzedExpression {
	public static enum Operator {
		ASSIGN("="), PLUS_ASSIGN("+="), MINUS_ASSIGN("-="), STAR_ASSIGN("*="), SLASH_ASSIGN(
				"/="), AND_ASSIGN("&="), OR_ASSIGN("|="), XOR_ASSIGN("^="), MODULO_ASSIGN(
				"%="), LSHIFT_ASSIGN("<<="), SIGNED_RSHIFT_ASSIGN(">>="), UNSIGNED_RSHIFT_ASSIGN(
				">>>=");

		private final String javaRepresentation;

		private Operator(String javaRepresentation) {
			this.javaRepresentation = javaRepresentation;
		}

		public String getJavaRepresentation() {
			return javaRepresentation;
		}
	}

	private final AnalyzedExpression target;

	private final Operator operator;

	private final AnalyzedExpression value;

	public AssignExpression(@Nonnull Location location,
			@Nonnull AnalyzedExpression target, @Nonnull Operator operator,
			@Nonnull AnalyzedExpression value) {
		super(location);
		this.target = target;
		this.operator = operator;
		this.value = value;
	}

	@Nonnull
	public AnalyzedExpression getTarget() {
		return target;
	}

	@Nonnull
	public Operator getOperator() {
		return operator;
	}

	@Nonnull
	public AnalyzedExpression getValue() {
		return value;
	}

	@Override
	public String toJavaString() {
		return String.format("%s %s %s", target.toJavaString(),
				operator.getJavaRepresentation(), value.toJavaString());
	}

}
