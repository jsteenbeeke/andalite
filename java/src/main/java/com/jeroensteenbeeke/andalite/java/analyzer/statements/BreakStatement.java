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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BreakStatement extends BaseStatement {
	private final AnalyzedExpression expression;

	public BreakStatement(@Nonnull Location location, @Nullable AnalyzedExpression expression) {
		super(location);
		this.expression = expression;
	}

	@Nonnull
	public Optional<AnalyzedExpression> getExpression() {
		return Optional.ofNullable(expression);
	}

	@Override
	public String toJavaString() {
		if (expression != null) {
			return String.format("break %s", expression.toJavaString());
		}

		return "break";
	}

}
