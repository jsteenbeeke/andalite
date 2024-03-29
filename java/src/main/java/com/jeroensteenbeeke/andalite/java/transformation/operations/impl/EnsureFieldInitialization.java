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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IFieldOperation;

import org.jetbrains.annotations.NotNull;

public class EnsureFieldInitialization implements IFieldOperation {
	private final String expression;

	public EnsureFieldInitialization(String expression) {
		super();
		this.expression = expression;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedField input)
			throws OperationException {
		AnalyzedExpression init = input.getInitializationExpression();

		if (init == null) {
			return ImmutableList.of(input.insertAt(AnalyzedField.FieldInsertionPoint.BEFORE_SEMICOLON, String.format(" = %s", expression)));
		} else {
			if (init.toJavaString().equals(expression)) {
				return ImmutableList.of();
			} else {
				return ImmutableList.of(init.replace(expression));
			}
		}

	}

	@Override
	public String getDescription() {
		return String.format("Initialize field to %s", expression);
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedField input) {
		AnalyzedExpression init = input.getInitializationExpression();

		if (init != null) {
			String expected = expression;
			String observed = init.toJavaString();

			if (expected.equals(observed)) {
				return ActionResult.ok();
			}

			return ActionResult.error(
					"Incorrect initialization value. Expected %s but found %s",
					expected, observed);
		}

		return ActionResult.error("Missing initialization expression");
	}
}
