package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

public class EnsureMethodFinal implements IMethodOperation {

	public EnsureMethodFinal() {
	}

	@Override
	public List<Transformation> perform(AnalyzedMethod input)
			throws OperationException {
		if (!input.isFinal()) {
			return ImmutableList.of(Transformation.insertBefore(
					input.getReturnType(), " final "));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(AnalyzedMethod input) {
		if (input.isFinal()) {
			return ActionResult.ok();
		}

		return ActionResult.error("Method not final");
	}

	@Override
	public String getDescription() {
		return String.format("Ensure method is final");
	}

}
