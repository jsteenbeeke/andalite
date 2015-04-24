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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;

public class ThisExpression extends AnalyzedExpression {

	private final AnalyzedExpression classExpression;

	public ThisExpression(@Nonnull Location location,
			@Nullable AnalyzedExpression classExpression) {
		super(location);
		this.classExpression = classExpression;
	}

	@CheckForNull
	public AnalyzedExpression getClassExpression() {
		return classExpression;
	}

	@Override
	public String toJavaString() {
		if (classExpression != null) {
			return String.format("%s.this", classExpression.toJavaString());
		}

		return "this";
	}

}
