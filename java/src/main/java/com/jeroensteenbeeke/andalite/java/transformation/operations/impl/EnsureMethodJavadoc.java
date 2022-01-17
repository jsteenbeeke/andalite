package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

import org.jetbrains.annotations.NotNull;

public class EnsureMethodJavadoc implements IMethodOperation {
	private final String javadoc;

	public EnsureMethodJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedMethod input)
		throws OperationException {
		if (input.getJavadoc().map(String::trim).filter(javadoc::equals).isEmpty()) {
			return ImmutableList
				.of(input.insertAt(AnalyzedMethod.MethodInsertionPoint.BEFORE, String.format("/**\n * %s\n */", javadoc)));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedMethod input) {
		if (input.getJavadoc().map(String::trim).filter(javadoc::equals).isPresent()) {
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
