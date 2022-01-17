package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

import org.jetbrains.annotations.NotNull;

public class EnsureMethodFinal implements IMethodOperation {

	public EnsureMethodFinal() {
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedMethod input)
			throws OperationException {
		if (!input.isFinal()) {
			return ImmutableList.of(input.insertAt(
				AnalyzedMethod.MethodInsertionPoint.BEFORE_RETURN_TYPE, " final "));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedMethod input) {
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
