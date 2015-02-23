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

public class FieldAccessExpression extends AnalyzedExpression {

	private final AnalyzedExpression scope;

	private final String field;

	private final List<AnalyzedType> typeArguments;

	public FieldAccessExpression(Location location,
			@Nullable AnalyzedExpression scope, @Nonnull String field) {
		super(location);
		this.scope = scope;
		this.field = field;
		this.typeArguments = Lists.newArrayList();
	}

	@Nonnull
	public String getField() {
		return field;
	}

	@CheckForNull
	public AnalyzedExpression getScope() {
		return scope;
	}

	@Nonnull
	public List<AnalyzedType> getTypeArguments() {
		return ImmutableList.copyOf(typeArguments);
	}

	/**
	 * @nonpublic
	 */
	public void addTypeArgument(@Nonnull AnalyzedType type) {
		this.typeArguments.add(type);
	}

	@Override
	public String toJavaString() {
		StringBuilder java = new StringBuilder();
		if (scope != null) {
			java.append(scope.toJavaString());
			java.append(".");
		}
		if (!typeArguments.isEmpty()) {
			java.append("<");
			Joiner.on(",").appendTo(
					java,
					FluentIterable.from(typeArguments).transform(
							AnalyzedType.toJavaStringFunction()));
			java.append(">");
		}
		java.append(field);

		return java.toString();
	}

}
