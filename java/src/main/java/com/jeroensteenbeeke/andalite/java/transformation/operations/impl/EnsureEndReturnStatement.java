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

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;

public class EnsureEndReturnStatement implements IBodyContainerOperation {

	private final String returnValue;

	public EnsureEndReturnStatement(@Nonnull final String returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public List<Transformation> perform(IBodyContainer input)
			throws OperationException {
		if (input.isAbstract()) {
			throw new OperationException(
					"Cannot insert statement into abstract method!");
		}

		AnalyzedStatement last = Iterables.getLast(input.getStatements(), null);

		if (last == null) {
			// No statements
			return ImmutableList.of(Transformation.insertAt(input.getLocation()
					.getEnd() - 1, String.format("\t\treturn %s;\n",
					returnValue)));
		} else {
			// One or more statements
			if (last instanceof ReturnStatement) {
				// Check if return statement matches expectations
				ReturnStatement statement = (ReturnStatement) last;

				AnalyzedExpression returnExpression = statement
						.getReturnExpression();
				if (!returnExpression.toJavaString().equals(returnValue)) {
					return ImmutableList.of(Transformation.replace(
							returnExpression, returnValue));
				}
			} else {
				// Insert return statement after
				return ImmutableList.of(Transformation.insertAfter(last,
						String.format("\t\treturn %s;", returnValue)));
			}
		}

		return ImmutableList.of();
	}

	@Override
	public String getDescription() {
		return String.format("Body container returns %s", returnValue);
	}

}
