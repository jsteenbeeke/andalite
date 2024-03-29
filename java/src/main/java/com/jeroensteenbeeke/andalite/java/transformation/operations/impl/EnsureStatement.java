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

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;

import org.jetbrains.annotations.NotNull;

public abstract class EnsureStatement<T extends IBodyContainer<T, I>, I extends Enum<I> & IInsertionPoint<T>> implements IBodyContainerOperation<T,I> {
	private static final Logger logger = LoggerFactory
			.getLogger(EnsureStatement.class);

	private final String statement;

	private final boolean verification;

	public EnsureStatement(String statement) {
		this(statement.endsWith(";") ? statement : String.format(
				"%s;", statement), false);
	}

	private EnsureStatement(String statement, boolean verification) {
		this.statement = statement;
		this.verification = verification;
	}

	public EnsureStatement<T,I> withoutVerification() {
		return new EnsureStatement<>(statement, false) {
			@Override
			public I getLastStatementLocation() {
				return EnsureStatement.this.getLastStatementLocation();
			}
		};
	}

	@Override
	public List<Transformation> perform(@NotNull T input)
			throws OperationException {
		if (input.isAbstract()) {
			throw new OperationException(
					"Cannot insert statement into abstract method!");
		}

		for (AnalyzedStatement<?,?> analyzedStatement : input.getStatements()) {
			if (analyzedStatement == null)
				continue;

			final String asJava = String.format("%s;",
					analyzedStatement.toJavaString());

			logger.debug("Found statement {}", asJava);

			if (asJava.equals(statement)) {
				return ImmutableList.of();
			}
		}

		final String code = String.format("\t\t%s", statement);



		return ImmutableList.of(input.insertAt(getLastStatementLocation(), code));
	}

	public abstract I getLastStatementLocation();

	@Override
	public String getDescription() {
		return String.format("it has statement: %s", statement);
	}

	@Override
	public ActionResult verify(@NotNull T input) {
		if (!verification) {
			return ActionResult.ok();
		}

		for (AnalyzedStatement<?,?> analyzedStatement : input.getStatements()) {
			if (analyzedStatement == null)
				continue;

			final String asJava = String.format("%s;",
					analyzedStatement.toJavaString());

			if (asJava.equals(statement)) {
				return ActionResult.ok();
			}
		}

		return ActionResult.error("Missing statement: %s", statement);
	}
}
