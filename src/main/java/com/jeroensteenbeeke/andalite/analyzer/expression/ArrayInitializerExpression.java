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

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;

public class ArrayInitializerExpression extends AnalyzedExpression {

	private List<AnalyzedExpression> values;

	public ArrayInitializerExpression(@Nonnull Location location) {
		super(location);
		this.values = Lists.newArrayList();
	}

	public void addValue(@Nonnull AnalyzedExpression expression) {
		values.add(expression);
	}

	@Nonnull
	public List<AnalyzedExpression> getValues() {
		return ImmutableList.copyOf(values);
	}

	@Override
	public String toJavaString() {
		return String.format(
				"{%s}",
				Joiner.on(", ").join(
						FluentIterable.from(values).transform(
								toJavaStringFunction())));
	}

}
