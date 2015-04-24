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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class ArrayCreationExpression extends AnalyzedExpression {
	private final ArrayInitializerExpression initializer;

	private final List<AnalyzedExpression> dimensions;

	private final AnalyzedType type;

	public ArrayCreationExpression(@Nonnull Location location,
			@Nonnull AnalyzedType type,
			@Nullable ArrayInitializerExpression initializer) {
		super(location);
		this.type = type;
		this.initializer = initializer;
		this.dimensions = Lists.newArrayList();
	}

	@Nonnull
	public AnalyzedType getType() {
		return type;
	}

	@CheckForNull
	public ArrayInitializerExpression getInitializer() {
		return initializer;
	}

	@Nonnull
	public List<AnalyzedExpression> getDimensions() {
		return ImmutableList.copyOf(dimensions);
	}

	@Override
	public String toJavaString() {
		StringBuilder builder = new StringBuilder();
		builder.append("new ");
		builder.append(type.toJavaString());
		builder.append("[");
		if (!dimensions.isEmpty()) {
			builder.append(Joiner.on("][").join(
					FluentIterable.from(getDimensions()).transform(
							toJavaStringFunction())));
		}

		builder.append("]");

		return builder.toString();
	}

	public void addDimension(@Nonnull AnalyzedExpression analyzeExpression) {
		dimensions.add(analyzeExpression);
	}
}
