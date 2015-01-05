package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.IBodyContainerOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

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
					.getEnd() - 1, String.format("return %s;", returnValue)));
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
						String.format("return %s;", returnValue)));
			}
		}

		return ImmutableList.of();
	}

	@Override
	public String getDescription() {
		return String.format("Body container returns %s", returnValue);
	}

}
