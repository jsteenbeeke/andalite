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
package com.jeroensteenbeeke.andalite.analyzer.statements;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;

public class DoStatement extends AnalyzedStatement {
	private final AnalyzedExpression condition;

	private final AnalyzedStatement body;

	public DoStatement(Location location, AnalyzedExpression condition,
			AnalyzedStatement body) {
		super(location);
		this.condition = condition;
		this.body = body;
	}

	public AnalyzedExpression getCondition() {
		return condition;
	}

	public AnalyzedStatement getBody() {
		return body;
	}

	@Override
	public String toJavaString() {
		return String.format("do %s while %s", body.toJavaString(),
				condition.toJavaString());
	}
}
