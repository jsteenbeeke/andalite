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
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class CastExpression extends AnalyzedExpression {

	private final AnalyzedType type;
	private final AnalyzedExpression expression;

	public CastExpression(@Nonnull Location location,
			@Nonnull AnalyzedType type, @Nonnull AnalyzedExpression expression) {
		super(location);
		this.type = type;
		this.expression = expression;
	}

	@Nonnull
	public AnalyzedType getType() {
		return type;
	}

	@Nonnull
	public AnalyzedExpression getExpression() {
		return expression;
	}

	@Override
	public String toJavaString() {
		return String.format("(%s) %s", type.toJavaString(),
				expression.toJavaString());
	}

}
