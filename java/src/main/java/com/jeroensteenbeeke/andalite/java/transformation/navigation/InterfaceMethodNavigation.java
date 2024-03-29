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
package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import java.util.List;

import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

public class InterfaceMethodNavigation extends
	ChainedNavigation<AnalyzedInterface, AnalyzedMethod> {
	private final String name;

	private final MethodReturnType type;

	private final List<ParameterDescriptor> descriptors;

	public InterfaceMethodNavigation(
		@NotNull IJavaNavigation<AnalyzedInterface> classNavigation,
		@NotNull String name, @NotNull MethodReturnType type,
		@NotNull List<ParameterDescriptor> descriptors) {
		super(classNavigation);
		this.name = name;
		this.type = type;
		this.descriptors = descriptors;
	}

	@Override
	public String getStepDescription() {
		return String.format("go to method %s",
			AnalyzeUtil.getMethodSignature(name, descriptors));
	}

	@Override
	public AnalyzedMethod navigate(AnalyzedInterface chainedTarget)
		throws NavigationException {
		for (AnalyzedMethod analyzedMethod : chainedTarget.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType
						.toJavaString();

					if (type != null && !type.toJavaString(chainedTarget).equals(returnTypeAsString)) {
						throw new NavigationException(
							"Method %s found, but has incorrect return type %s (expected %s)",
							AnalyzeUtil.getMethodSignature(name,
								descriptors), returnTypeAsString, type);
					}

					return analyzedMethod;
				}
			}
		}

		throw new NavigationException("Could not find method %s",
			AnalyzeUtil.getMethodSignature(name, descriptors));
	}

}
