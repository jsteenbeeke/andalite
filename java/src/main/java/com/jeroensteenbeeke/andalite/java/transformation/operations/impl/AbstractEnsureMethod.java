package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;
import com.jeroensteenbeeke.lux.ActionResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEnsureMethod<T extends ContainingDenomination<T, I>, I extends Enum<I> & IInsertionPoint<T>> implements IJavaOperation<T> {
	private final String name;

	private final MethodReturnType type;

	private final AccessModifier modifier;

	private final List<ParameterDescriptor> descriptors;

	public AbstractEnsureMethod(String name, MethodReturnType type, AccessModifier modifier, List<ParameterDescriptor> descriptors) {
		this.name = name;
		this.type = type;
		this.modifier = modifier;
		this.descriptors = descriptors;
	}

	@Override
	public List<Transformation> perform(@NotNull T input) throws OperationException {
		for (AnalyzedMethod analyzedMethod : input.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType != null ? returnType
						.toJavaString() : "void";

					if (!type.toJavaString(input).equals(returnTypeAsString)) {
						throw new OperationException(
							String.format(
								"Method with expected signature exists, but has incorrect return type %s (expected %s)",
								returnTypeAsString, type));
					}

					if (!modifier.equals(analyzedMethod.getAccessModifier())) {
						throw new OperationException(
							String.format(
								"Method with expected signature exists, but has incorrect access %s (expected %s)",
								analyzedMethod.getAccessModifier(),
								modifier));
					}

					return ImmutableList.of();
				}
			}

		}
		StringBuilder code = new StringBuilder();
		code.append("\t");
		code.append(modifier.getOutput());
		code.append(type.toJavaString(input));
		code.append(" ");
		code.append(name);
		code.append("(");
		code.append(Joiner.on(", ").join(descriptors));
		code.append(") {\n");
		code.append("\t}\n\n");

		ImmutableList.Builder<Transformation> transforms = ImmutableList
			.builder();


		transforms.add(input.insertAt(getInsertionPoint(), code.toString()));

		return transforms.build();
	}


	@Override
	public String getDescription() {
		return String.format("existence of method: %s%s %s",
			modifier.getOutput(), type,
			AnalyzeUtil.getMethodSignature(name, descriptors));
	}

	@Override
	public ActionResult verify(@NotNull T input) {
		for (AnalyzedMethod analyzedMethod : input.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType != null ? returnType
						.toJavaString() : "void";

					if (!type.toJavaString(input).equals(returnTypeAsString)) {
						return ActionResult
							.error("Method with expected signature exists, but has incorrect return type %s (expected %s)",
								returnTypeAsString, type);
					}

					if (!modifier.equals(analyzedMethod.getAccessModifier())) {
						return ActionResult
							.error("Method with expected signature exists, but has incorrect access %s (expected %s)",
								analyzedMethod.getAccessModifier(),
								modifier);
					}

					return ActionResult.ok();
				}
			}
		}

		return ActionResult.error("No method %s with parameters ( %s ) found",
			name, descriptors.stream().map(ParameterDescriptor::toString)
				.collect(Collectors.joining(", ")));
	}

	protected abstract I getInsertionPoint();
}
