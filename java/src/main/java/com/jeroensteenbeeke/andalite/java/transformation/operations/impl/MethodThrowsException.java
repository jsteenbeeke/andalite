package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedThrownException;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

import javax.annotation.Nonnull;

public class MethodThrowsException implements IMethodOperation {
	private final String thrownException;

	public MethodThrowsException(String thrownException) {
		this.thrownException = thrownException;
	}

	@Override
	public List<Transformation> perform(@Nonnull AnalyzedMethod input)
		throws OperationException {
		List<String> ex = input
			.getThrownExceptions()
			.stream()
			.map(AnalyzedThrownException::getException)
			.collect(Collectors
						 .toList());
		if (ex.contains(thrownException)) {
			return ImmutableList.of();
		}

		if (!ex.isEmpty()) {
			return ImmutableList
				.of(input.insertAt(AnalyzedMethod.MethodInsertionPoint.AFTER_LAST_EXCEPTION, ", "+ thrownException));
		} else {
			return ImmutableList
				.of(input.insertAt(AnalyzedMethod.MethodInsertionPoint.AFTER_LAST_EXCEPTION, " throws "+ thrownException));
		}
	}

	@Override
	public ActionResult verify(@Nonnull AnalyzedMethod input) {
		if (input.getThrownExceptions().stream()
				 .anyMatch(e -> e.getException().equals(thrownException))) {
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
