/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IStatementOperation;

public class EnsureNextStatement<T extends AnalyzedStatement<T,I>, I extends Enum<I> & IInsertionPoint<T>> implements IStatementOperation<T,I> {
	private final String statement;

	public EnsureNextStatement(String statement) {
		this.statement = statement.endsWith(";") ? statement : String.format(
			"%s;", statement);
	}

	@Override
	public List<Transformation> perform(AnalyzedStatement<T,I> last)
		throws OperationException {

		final String code = String.format("\t\t%s", statement);

		I point;

		if (last instanceof ReturnStatement) {
			// insert before last
			point = last.getBeforeInsertionPoint();
		} else {
			point = last.getAfterInsertionPoint();
		}

		return ImmutableList.of(last.insertAt(point, String.format("\n%s", code)));
	}

	@Override
	public String getDescription() {
		return String.format("it has statement: %s", statement);
	}

	@Override
	public ActionResult verify(AnalyzedStatement input) {
		// FIXME: Not verifiable?

		return ActionResult.ok();
		//
		// return ActionResult.error("Missing statement: %s", statement);
	}
}
