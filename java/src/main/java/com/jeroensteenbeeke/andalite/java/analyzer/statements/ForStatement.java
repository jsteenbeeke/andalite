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

public class ForStatement extends AnalyzedStatement {
	private final AnalyzedStatement body;

	private final AnalyzedExpression compare;

	private final List<AnalyzedExpression> initializerExpressions;

	private final List<AnalyzedExpression> updateExpressions;

	public ForStatement(Location location, AnalyzedStatement body,
			AnalyzedExpression compare) {
		super(location);
		this.body = body;
		this.compare = compare;
		this.initializerExpressions = Lists.newArrayList();
		this.updateExpressions = Lists.newArrayList();
	}

	public AnalyzedStatement getBody() {
		return body;
	}

	public AnalyzedExpression getCompare() {
		return compare;
	}

	public void addInitializerExpression(AnalyzedExpression expression) {
		this.initializerExpressions.add(expression);
	}

	public List<AnalyzedExpression> getInitializerExpressions() {
		return initializerExpressions;
	}

	public void addUpdateExpression(AnalyzedExpression expression) {
		this.updateExpressions.add(expression);
	}

	public List<AnalyzedExpression> getUpdateExpressions() {
		return updateExpressions;
	}

	@Override
	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append("for (");
		java.append(initializerExpressions.stream()
				.map(AnalyzedExpression::toJavaString)
				.collect(Collectors.joining(", ")));
		java.append(";");
		if (compare != null) {
			java.append(compare.toJavaString());
		}
		java.append(";");
		java.append(
				updateExpressions.stream().map(AnalyzedExpression::toJavaString)
						.collect(Collectors.joining(", ")));
		java.append(") ");
		java.append(body.toJavaString());

		return java.toString();
	}

}
