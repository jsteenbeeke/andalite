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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructorInvocationStatement extends BaseStatement<ConstructorInvocationStatement> {
	private final InvocationType invocationType;

	private AnalyzedExpression scope = null;

	private List<AnalyzedExpression> arguments;

	private List<AnalyzedType> typeArguments;

	public ConstructorInvocationStatement(Location location,
			InvocationType invocationType) {
		super(location);
		this.invocationType = invocationType;
		this.arguments = Lists.newArrayList();
		this.typeArguments = Lists.newArrayList();
	}

	public void setScope(AnalyzedExpression scope) {
		this.scope = scope;
	}

	public void addArgument(AnalyzedExpression argument) {
		this.arguments.add(argument);
	}

	public void addTypeArgument(AnalyzedType type) {
		this.typeArguments.add(type);
	}

	public InvocationType getInvocationType() {
		return invocationType;
	}

	public List<AnalyzedExpression> getArguments() {
		return arguments;
	}

	public AnalyzedExpression getScope() {
		return scope;
	}

	public List<AnalyzedType> getTypeArguments() {
		return typeArguments;
	}

	@Override
	public String toJavaString() {
		StringBuilder result = new StringBuilder();

		if (scope != null) {
			result.append(scope.toJavaString());
			result.append(".");
		}
		if (!typeArguments.isEmpty()) {
			result.append("<");
			result.append(Joiner.on(",").join(
				typeArguments.stream().map(AnalyzedType::toJavaString).collect(Collectors.toList())));
			result.append(">");
		}

		result.append(invocationType.toJavaString());

		result.append("(");
		if (!arguments.isEmpty()) {
			result.append(Joiner.on(", ").join(
				arguments.stream().map(AnalyzedExpression::toJavaString).collect(Collectors.toList())));
		}
		result.append(")");

		return result.toString();
	}

	public static enum InvocationType {
		THIS, SUPER;

		public String toJavaString() {
			return name().toLowerCase();
		}
	}

}
