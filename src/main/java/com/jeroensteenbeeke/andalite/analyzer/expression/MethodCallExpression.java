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

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.analyzer.IOutputCallback;

public class MethodCallExpression extends AnalyzedExpression {
	private final String name;

	private final List<AnalyzedType> typeArguments;

	private final List<AnalyzedExpression> arguments;

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

	public List<AnalyzedExpression> getArguments() {
		return arguments;
	}

	public List<AnalyzedType> getTypeArguments() {
		return typeArguments;
	}

	@Override
	public void output(IOutputCallback callback) {
		if (!typeArguments.isEmpty()) {
			boolean first = true;

			callback.write("<");
			for (AnalyzedType type : typeArguments) {
				if (first) {
					first = false;
				} else {
					callback.write(", ");
				}

				type.output(callback);
			}

			callback.write(">");
		}

		callback.write(name);
		callback.write("(");

		if (!arguments.isEmpty()) {
			boolean first = true;

			for (AnalyzedExpression analyzedExpression : arguments) {
				if (first) {
					first = false;
				} else {
					callback.write(", ");
				}

				analyzedExpression.output(callback);
			}
		}

		callback.write(");");
	}

}
