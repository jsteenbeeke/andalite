package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

public class EnsureMethodJavadoc implements IMethodOperation {
	private final String javadoc;

	public EnsureMethodJavadoc(String javadoc) {
		this.javadoc = String.format("/**\n * %s\n */\n", javadoc);
	}

	@Override
	public List<Transformation> perform(AnalyzedMethod input)
			throws OperationException {
		if (!javadoc.equals(input.getJavadoc())) {
			return ImmutableList
					.of(input.insertAt(AnalyzedMethod.MethodInsertionPoint.BEFORE, javadoc));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(AnalyzedMethod input) {
		if (javadoc.equals(input.getJavadoc())) {
			return ActionResult.ok();
		}

		return ActionResult.error("Missing javadoc: %s (actual: %s)", javadoc,
				input.getJavadoc());
	}

	@Override
	public String getDescription() {
		return String.format("Ensure method has javadoc %s", javadoc);
	}

}
