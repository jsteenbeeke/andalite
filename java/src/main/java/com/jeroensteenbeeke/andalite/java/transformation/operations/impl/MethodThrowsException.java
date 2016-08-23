package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedThrownException;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

public class MethodThrowsException implements IMethodOperation {
	private final String thrownException;

	public MethodThrowsException(String thrownException) {
		this.thrownException = thrownException;
	}

	@Override
	public List<Transformation> perform(AnalyzedMethod input)
			throws OperationException {
		List<AnalyzedThrownException> ex = input.getThrownExceptions();
		if (ex.contains(thrownException)) {
			return ImmutableList.of();
		}

		if (!ex.isEmpty()) {
			return ImmutableList
					.of(Transformation.insertAfter(ex.get(ex.size() - 1),
							String.format(", %s", thrownException)));
		}

		Location rparen = input.getRightParenthesisLocation();

		if (rparen != null) {
			return ImmutableList.of(Transformation.insertAfter(rparen,
					String.format(" throws %s", thrownException)));
		}

		throw new OperationException(
				"Cannot determine where to place exception statement");
	}

	@Override
	public ActionResult verify(AnalyzedMethod input) {
		if (input.getThrownExceptions().contains(thrownException)) {
			return ActionResult.ok();
		}

		return ActionResult.error("Method %s does not throw %s",
				input.toString(), thrownException);
	}

	@Override
	public String getDescription() {
		return "Ensure method throws exception ".concat(thrownException);
	}

}
