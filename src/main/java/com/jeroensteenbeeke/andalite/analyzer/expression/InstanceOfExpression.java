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

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class InstanceOfExpression extends AnalyzedExpression {

	private final AnalyzedExpression target;

	private final AnalyzedType type;

	public InstanceOfExpression(Location location, AnalyzedExpression target,
			AnalyzedType type) {
		super(location);
		this.target = target;
		this.type = type;
	}

	public AnalyzedExpression getTarget() {
		return target;
	}

	public AnalyzedType getType() {
		return type;
	}

	@Override
	public String toJavaString() {
		final String expression = String.format("%s instanceof %s",
				target.toJavaString(), type.toJavaString());
		return expression;
	}

}
