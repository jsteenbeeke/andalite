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
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.IBodyContainerOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public class EnsureStatement implements IBodyContainerOperation {
	private final String statement;

	public EnsureStatement(String statement) {
		this.statement = statement.endsWith(";") ? statement : String.format(
				"%s;", statement);
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
			if (analyzedStatement == null)
				continue;

			final String asJava = String.format("%s;",
					analyzedStatement.toJavaString());
			if (asJava.equals(statement)) {
				return ImmutableList.of();
			}

			last = analyzedStatement;
		}

		final String code = String.format("\t\t%s", statement);

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
		return String.format("it has statement: %s", statement);
	}

}
