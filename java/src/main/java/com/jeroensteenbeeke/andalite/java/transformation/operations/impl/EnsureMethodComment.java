package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

public class EnsureMethodComment implements IMethodOperation {
	private final String comment;

	public EnsureMethodComment(String comment) {
		this.comment = String.format("// %s\n", comment);
	}

	@Override
	public List<Transformation> perform(AnalyzedMethod input)
			throws OperationException {
		if (!input.getComments().contains(comment)) {
			return ImmutableList
					.of(Transformation.insertBefore(input, comment));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(AnalyzedMethod input) {
		if (input.getComments().contains(comment)) {
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
