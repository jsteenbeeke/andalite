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

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;

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
		Joiner.on(",").appendTo(
				java,
				FluentIterable.from(initializerExpressions).transform(
						AnalyzedExpression.toJavaStringFunction()));
		java.append(";");
		if (compare != null) {
			java.append(compare.toJavaString());
		}
		java.append(";");
		Joiner.on(",").appendTo(
				java,
				FluentIterable.from(updateExpressions).transform(
						AnalyzedExpression.toJavaStringFunction()));
		java.append(") ");
		java.append(body.toJavaString());

		return java.toString();
	}

}
