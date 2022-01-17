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

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ReturnStatement extends BaseStatement<ReturnStatement> {
	private final AnalyzedExpression returnExpression;

	public ReturnStatement(@NotNull final Location location,
			@Nullable final AnalyzedExpression returnExpression) {
		super(location);
		this.returnExpression = returnExpression;
	}

	@NotNull
	public Optional<AnalyzedExpression> getReturnExpression() {
		return Optional.ofNullable(returnExpression);
	}

	@Override
	public String toJavaString() {
		return String.format("return %s", returnExpression != null ? returnExpression.toJavaString() : "");
	}

}
