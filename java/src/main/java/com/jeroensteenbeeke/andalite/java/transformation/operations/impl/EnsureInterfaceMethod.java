/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IInterfaceOperation;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

import org.jetbrains.annotations.NotNull;

public class EnsureInterfaceMethod implements IInterfaceOperation {
	private final String name;

	private final MethodReturnType type;

	private final InterfaceMethodType methodType;

	private final List<ParameterDescriptor> descriptors;

	public EnsureInterfaceMethod(String name, MethodReturnType type, InterfaceMethodType methodType,
								 List<ParameterDescriptor> descriptors) {
		this.name = name;
		this.type = type;
		this.methodType = methodType;
		this.descriptors = descriptors;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedInterface input)
		throws OperationException {
		for (AnalyzedMethod analyzedMethod : input.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType
						.toJavaString();

					if (!type.toJavaString(input).equals(returnTypeAsString)) {
						throw new OperationException(
							String.format(
								"Method with expected signature exists, but has incorrect return type %s (expected %s)",
								returnTypeAsString, type));
					}

					return ImmutableList.of();
				}
			}
		}
		StringBuilder code = new StringBuilder();
		code.append("\t");
		switch (methodType) {
			case DEFAULT:
				code.append("default ");
				break;
			case PRIVATE:
				code.append("private ");
				break;
			case PRIVATE_STATIC:
				code.append("private ");
			case PUBLIC_STATIC:
				code.append("static ");
				break;
		}

		code.append(type.toJavaString(input));
		code.append(" ");
		code.append(name);
		code.append("(");
		code.append(Joiner.on(", ").join(descriptors));
		code.append(")");

		switch (methodType) {
			case DEFAULT:
			case PRIVATE:
			case PRIVATE_STATIC:
			case PUBLIC_STATIC:
				code.append(" {}");
				break;
			case ABSTRACT:
				code.append(";");
				break;
		}

		code.append("\n\n");

		return ImmutableList.of(input.insertAt(AnalyzedInterface.InterfaceInsertionPoint.AFTER_LAST_METHOD, code.toString()));
	}

	@Override
	public String getDescription() {
		return String.format("existence of method: %s %s", type,
							 AnalyzeUtil.getMethodSignature(name, descriptors));
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedInterface input) {
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

					return ActionResult.ok();
				}
			}
		}

		return ActionResult.error(
			"No method named %s returning %s with parameters ( %s ) found",
			name, type,
			descriptors.stream().map(ParameterDescriptor::toString)
					   .collect(Collectors.joining(", ")));
	}
}
