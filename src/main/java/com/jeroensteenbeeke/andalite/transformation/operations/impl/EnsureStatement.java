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
			final String asJava = String.format("%s;",
					analyzedStatement.toJavaString());
			if (asJava.equals(statement)) {
				return ImmutableList.of();
			}

			last = analyzedStatement;
		}

		final String code = String.format("%s\n", statement);

		Transformation t;

		if (last == null) {
			t = Transformation.insertAt(input.getLocation().getEnd() - 1, code);
		} else {
			if (last instanceof ReturnStatement) {
				// insert before last
				t = Transformation.insertBefore(last, code);
			} else {

				t = Transformation.insertAfter(last, code);
			}
		}

		return ImmutableList.of(t);
	}

	@Override
	public String getDescription() {
		return String.format("Body container has statement: %s;", statement);
	}

}
