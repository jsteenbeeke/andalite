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

public class IfStatement extends BaseStatement<IfStatement> {
	private final AnalyzedExpression condition;

	private final AnalyzedStatement<?,?> thenStatement;

	private AnalyzedStatement<?,?> elseStatement;

	public IfStatement(Location location, AnalyzedExpression condition,
			AnalyzedStatement<?,?> thenStatement) {
		super(location);
		this.condition = condition;
		this.thenStatement = thenStatement;
	}

	public void setElseStatement(AnalyzedStatement<?,?> statement) {
		this.elseStatement = statement;
	}

	@Override
	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append("if (");
		java.append(condition.toJavaString());
		java.append(") ");
		java.append(thenStatement.toJavaString());

		if (elseStatement != null) {
			java.append(" else ");
			java.append(elseStatement.toJavaString());
		}

		return java.toString();
	}

	public AnalyzedExpression getCondition() {
		return condition;
	}

	public AnalyzedStatement<?,?> getThenStatement() {
		return thenStatement;
	}

	public AnalyzedStatement<?,?> getElseStatement() {
		return elseStatement;
	}
}
