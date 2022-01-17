package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

import org.jetbrains.annotations.NotNull;

public class EnsureMethodComment implements IMethodOperation {
	private final String comment;

	public EnsureMethodComment(String comment) {
		this.comment = comment;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedMethod input)
			throws OperationException {
		if (!input.getComments().contains(comment)) {
			return ImmutableList
					.of(input.insertAt(AnalyzedMethod.MethodInsertionPoint.BEFORE, String.format("// %s\n", comment)));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedMethod input) {
		if (input.getComments().stream().map(String::trim).anyMatch(comment::equals)) {
			return ActionResult.ok();
		}

		return ActionResult.error("Missing comment: %s (found: %s)", comment,
				input.getComments().stream().collect(Collectors.joining(", ")));
	}

	@Override
	public String getDescription() {
		return String.format("Ensure method has comment %s", comment);
	}

}
