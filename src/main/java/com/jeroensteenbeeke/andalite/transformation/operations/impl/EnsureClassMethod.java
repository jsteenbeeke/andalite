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
package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;
import com.jeroensteenbeeke.andalite.util.AnalyzeUtil;

public class EnsureClassMethod implements ClassOperation {
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
		for (AnalyzedMethod analyzedMethod : input.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					if (!type.equals(analyzedMethod.getReturnType())) {
						throw new OperationException(
								String.format(
										"Method with expected signature exists, but has incorrect return type %s (expected %s)",
										analyzedMethod.getReturnType(), type));
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
		code.append(type);
		code.append(" ");
		code.append(name);
		code.append("(");
		code.append(Joiner.on(", ").join(descriptors));
		code.append(") {\n");
		code.append("\t}\n");

		return ImmutableList.of(Transformation.insertBefore(
				input.getBodyLocation(), code.toString()));
	}

	@Override
	public String getDescription() {
		return String.format("existence of method: %s%s %s",
				modifier.getOutput(), type,
				AnalyzeUtil.getMethodSignature(name, descriptors));
	}

}
