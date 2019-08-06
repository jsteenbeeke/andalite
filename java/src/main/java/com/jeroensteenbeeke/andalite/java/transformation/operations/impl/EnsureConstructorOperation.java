package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

import javax.annotation.Nonnull;

public class EnsureConstructorOperation implements IClassOperation {
	private final AccessModifier modifier;

	private final List<ParameterDescriptor> parameters;

	private boolean checkSignature = true;

	public EnsureConstructorOperation(AccessModifier modifier,
			List<ParameterDescriptor> parameters) {
		super();
		this.modifier = modifier;
		this.parameters = parameters;
	}

	@Override
	public List<Transformation> perform(@Nonnull AnalyzedClass input)
			throws OperationException {

		for (AnalyzedConstructor ctor : input.getConstructors()) {
			if (!checkSignature
					|| AnalyzeUtil.matchesSignature(ctor, parameters)) {

				if (!modifier.equals(ctor.getAccessModifier())) {
					throw new OperationException(
							String.format(
									"Constructor with expected signature exists, but has incorrect access %s (expected %s)",
									ctor.getAccessModifier(), modifier));
				}

				return ImmutableList.of();
			}
		}
		StringBuilder code = new StringBuilder();

		code.append("\n\n\t");
		code.append(modifier.getOutput());
		code.append(" ");
		code.append(input.getClassName());
		code.append("(");
		code.append(createParameterList());
		code.append(") {\n");
		code.append("\t}\n\n");

		return ImmutableList.of(input.insertAt(AnalyzedClass.ClassInsertionPoint.AFTER_LAST_FIELD, code.toString()));
	}

	public EnsureConstructorOperation withAnySignature() {
		this.checkSignature = false;
		return this;
	}

	@Override
	public String getDescription() {
		return String.format(
				"Ensure presence of %s constructor with parameters (%s)",
				modifier.getOutput(), createParameterList());
	}

	protected String createParameterList() {
		return parameters.stream()
				.map(p -> String.format("%s %s", p.getType(), p.getName()))
				.collect(Collectors.joining(", "));
	}

	@Override
	public ActionResult verify(@Nonnull AnalyzedClass input) {

		for (AnalyzedConstructor ctor : input.getConstructors()) {
			if (AnalyzeUtil.matchesSignature(ctor, parameters)) {

				if (!modifier.equals(ctor.getAccessModifier())) {
					return ActionResult
							.error("Constructor with expected signature exists, but has incorrect access %s (expected %s)",
									ctor.getAccessModifier(), modifier);
				}

				return ActionResult.ok();
			}
		}

		return ActionResult.error(
				"No constructor with parameters ( %s ) found",
				parameters.stream().map(ParameterDescriptor::toString)
						.collect(Collectors.joining(", ")));
	}
}
