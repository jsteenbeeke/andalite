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
