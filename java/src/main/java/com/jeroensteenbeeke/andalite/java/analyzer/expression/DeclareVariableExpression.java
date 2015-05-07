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

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;

public class DeclareVariableExpression extends AnalyzedExpression {
	private final String name;

	private final AnalyzedExpression initializationExpression;

	public DeclareVariableExpression(Location location, String name,
			AnalyzedExpression initializationExpression) {
		super(location);
		this.name = name;
		this.initializationExpression = initializationExpression;
	}

	public AnalyzedExpression getInitializationExpression() {
		return initializationExpression;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toJavaString() {
		if (initializationExpression == null)
			return name;

		return String.format("%s = %s", name,
				initializationExpression.toJavaString());
	}
}
