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

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;

public class SwitchStatement extends AnalyzedStatement {

	private final AnalyzedExpression value;

	private final List<SwitchEntryStatement> statements;

	public SwitchStatement(Location from, AnalyzedExpression value) {
		super(from);
		this.value = value;
		this.statements = Lists.newArrayList();
	}

	public AnalyzedExpression getValue() {
		return value;
	}

	public void addStatement(SwitchEntryStatement statement) {
		this.statements.add(statement);
	}

	public List<SwitchEntryStatement> getStatements() {
		return statements;
	}

	@Override
	public String toJavaString() {
		return String.format("case %s: %s", getValue(),
				statements.stream().map(AnalyzedStatement::toJavaString)
						.collect(Collectors.joining("; ")));
	}
}
