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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;

public class HasIfStatementOperation implements IBodyContainerOperation {
	private static final Logger logger = LoggerFactory
			.getLogger(HasIfStatementOperation.class);

	private final String condition;

	public HasIfStatementOperation(String condition) {
		super();
		this.condition = condition;
	}

	@Override
	public List<Transformation> perform(IBodyContainer input)
			throws OperationException {
		if (input.isAbstract()) {
			throw new OperationException(
					"Cannot insert statement into abstract method!");
		}

		AnalyzedStatement last = null;

		for (AnalyzedStatement analyzedStatement : input.getStatements()) {
			if (analyzedStatement instanceof IfStatement) {
				IfStatement stmt = (IfStatement) analyzedStatement;

				final String conditionAsJavaString = stmt.getCondition()
						.toJavaString();
				logger.debug("found if-statement with condition {}",
						conditionAsJavaString);

				if (conditionAsJavaString.equals(condition)) {
					return ImmutableList.of();
				}
			}

			last = analyzedStatement;
		}

		final String code = String.format("\t\tif (%s) {\n\t\t\t\n\t\t}\n",
				condition);

		Transformation t;

		if (last == null) {
			t = Transformation.insertAt(input.getLocation().getEnd() - 1,
					String.format("%s\n", code));
		} else {
			if (last instanceof ReturnStatement) {
				// insert before last
				t = Transformation.insertBefore(last,
						String.format("%s\n", code));
			} else {

				t = Transformation.insertAfter(last,
						String.format("\n%s", code));
			}
		}

		return ImmutableList.of(t);
	}

	@Override
	public String getDescription() {
		return String
				.format("Ensure if-statement with condition %s", condition);
	}

	@Override
	public ActionResult verify(IBodyContainer input) {
		for (AnalyzedStatement analyzedStatement : input.getStatements()) {
			if (analyzedStatement instanceof IfStatement) {
				IfStatement stmt = (IfStatement) analyzedStatement;

				final String conditionAsJavaString = stmt.getCondition()
						.toJavaString();
				logger.debug("found if-statement with condition {}",
						conditionAsJavaString);

				if (conditionAsJavaString.equals(condition)) {
					return ActionResult.ok();
				}
			}
		}

		return ActionResult.error("if-statement with condition %s not found",
				condition);
	}
}
