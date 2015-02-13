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
package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.IFieldOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public class EnsureFieldInitialization implements IFieldOperation {
	private final String expression;

	public EnsureFieldInitialization(String expression) {
		super();
		this.expression = expression;
	}

	@Override
	public List<Transformation> perform(AnalyzedField input)
			throws OperationException {
		AnalyzedExpression init = input.getInitializationExpression();

		if (init == null) {
			return ImmutableList.of(Transformation.insertAfter(input,
					String.format(" = %s", expression)));
		} else {
			if (init.toJavaString().equals(expression)) {
				return ImmutableList.of();
			} else {
				return ImmutableList.of(Transformation
						.replace(init, expression));
			}
		}

	}

	@Override
	public String getDescription() {
		return String.format("Initialize field to %s", expression);
	}

}
