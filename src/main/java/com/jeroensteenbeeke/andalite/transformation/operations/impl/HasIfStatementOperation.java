package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.IBodyContainerOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public class HasIfStatementOperation implements IBodyContainerOperation {
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

				if (stmt.getCondition().toJavaString().equals(condition)) {
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

}
