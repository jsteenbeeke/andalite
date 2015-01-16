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

public class ConditionalExpression extends AnalyzedExpression {
	private final AnalyzedExpression condition;

	private final AnalyzedExpression thenExpression;

	private final AnalyzedExpression elseExpression;

	public ConditionalExpression(@Nonnull Location location,
			@Nonnull AnalyzedExpression condition,
			@Nonnull AnalyzedExpression thenExpression,
			@Nonnull AnalyzedExpression elseExpression) {
		super(location);
		this.condition = condition;
		this.thenExpression = thenExpression;
		this.elseExpression = elseExpression;
	}

	@Nonnull
	public AnalyzedExpression getCondition() {
		return condition;
	}

	@Nonnull
	public AnalyzedExpression getElseExpression() {
		return elseExpression;
	}

	@Nonnull
	public AnalyzedExpression getThenExpression() {
		return thenExpression;
	}

	@Override
	public String toJavaString() {
		return String.format("%s ? %s : %s", condition.toJavaString(),
				thenExpression.toJavaString(), elseExpression.toJavaString());
	}

}
