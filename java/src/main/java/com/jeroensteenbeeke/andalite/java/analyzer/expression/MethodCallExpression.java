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

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

public class MethodCallExpression extends AnalyzedExpression {
	private final String name;

	private final List<AnalyzedType> typeArguments;

	private final List<AnalyzedExpression> arguments;

	private AnalyzedExpression scope;

	public MethodCallExpression(@Nonnull final Location location,
			@Nonnull final String name,
			@Nonnull final List<AnalyzedType> analyzedTypeArguments,
			@Nonnull final List<AnalyzedExpression> arguments) {
		super(location);
		this.name = name;
		this.typeArguments = ImmutableList.copyOf(analyzedTypeArguments);
		this.arguments = ImmutableList.copyOf(arguments);
	}

	public String getName() {
		return name;
	}

	public AnalyzedExpression getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            The scope to set
	 * @nonpublic
	 */
	public void setScope(AnalyzedExpression scope) {
		this.scope = scope;
	}

	public List<AnalyzedExpression> getArguments() {
		return arguments;
	}

	public List<AnalyzedType> getTypeArguments() {
		return typeArguments;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		if (scope != null) {
			sb.append(scope.toJavaString());
			sb.append(".");
		}

		if (!typeArguments.isEmpty()) {
			boolean first = true;

			sb.append("<");
			for (AnalyzedType type : typeArguments) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}

				sb.append(type.toJavaString());
			}

			sb.append(">");
		}

		sb.append(name);
		sb.append("(");

		if (!arguments.isEmpty()) {
			boolean first = true;

			for (AnalyzedExpression analyzedExpression : arguments) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}

				sb.append(analyzedExpression.toJavaString());
			}
		}

		sb.append(")");

		return sb.toString();
	}
}
