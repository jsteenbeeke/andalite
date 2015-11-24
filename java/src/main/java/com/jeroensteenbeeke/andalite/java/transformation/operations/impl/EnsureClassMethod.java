/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

public class EnsureClassMethod implements IClassOperation {
	private final String name;

	private final String type;

	private final AccessModifier modifier;

	private final List<ParameterDescriptor> descriptors;

	public EnsureClassMethod(String name, String type, AccessModifier modifier,
			List<ParameterDescriptor> descriptors) {
		this.name = name;
		this.type = type;
		this.modifier = modifier;
		this.descriptors = descriptors;
	}

	@Override
	public List<Transformation> perform(AnalyzedClass input)
			throws OperationException {
		Location last = input.getBodyLocation();

		for (AnalyzedMethod analyzedMethod : input.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType != null ? returnType
							.toJavaString() : "void";

					if (!type.equals(returnTypeAsString)) {
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

			last = analyzedMethod.getLocation();
		}
		StringBuilder code = new StringBuilder();
		code.append("\t");
		code.append(modifier.getOutput());
		code.append(type);
		code.append(" ");
		code.append(name);
		code.append("(");
		code.append(Joiner.on(", ").join(descriptors));
		code.append(") {\n");
		code.append("\t}\n\n");

		ImmutableList.Builder<Transformation> transforms = ImmutableList
				.builder();

		int l = last.getEnd() + 2;

		if (last.getLength() == 0) {
			transforms
					.add(Transformation.insertAt(last.getStart() + 2, "\n\n"));

			l = last.getStart() + 3;
		}

		transforms.add(Transformation.insertAt(l, code.toString()));

		return transforms.build();
	}

	@Override
	public String getDescription() {
		return String.format("existence of method: %s%s %s",
				modifier.getOutput(), type,
				AnalyzeUtil.getMethodSignature(name, descriptors));
	}

	@Override
	public ActionResult verify(AnalyzedClass input) {
		for (AnalyzedMethod analyzedMethod : input.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType != null ? returnType
							.toJavaString() : "void";

					if (!type.equals(returnTypeAsString)) {
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

}
