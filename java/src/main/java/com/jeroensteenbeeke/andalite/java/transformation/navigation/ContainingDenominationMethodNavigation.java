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
package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;

public class ContainingDenominationMethodNavigation<T extends ContainingDenomination<T,?>> extends
		ChainedNavigation<T, AnalyzedMethod> {
	private final String name;

	private final MethodReturnType type;

	private final AccessModifier modifier;

	private final List<ParameterDescriptor> descriptors;

	public ContainingDenominationMethodNavigation(
			@NotNull IJavaNavigation<T> denominationNavigation,
			@NotNull String name, @NotNull MethodReturnType type,
			@NotNull AccessModifier modifier,
			@NotNull List<ParameterDescriptor> descriptors) {
		super(denominationNavigation);
		this.name = name;
		this.type = type;
		this.modifier = modifier;
		this.descriptors = descriptors;
	}

	@Override
	public String getStepDescription() {
		return String.format("go to method %s",
				AnalyzeUtil.getMethodSignature(name, descriptors));
	}

	@Override
	public AnalyzedMethod navigate(T chainedTarget)
			throws NavigationException {
		for (AnalyzedMethod analyzedMethod : chainedTarget.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType
												.toJavaString();

					String expectedReturnType = type.toJavaString(chainedTarget);
					if (!expectedReturnType.equals(returnTypeAsString)) {
						throw new NavigationException(
								"Method %s found, but has incorrect return type %s (expected %s)",
								AnalyzeUtil.getMethodSignature(name,
										descriptors), returnTypeAsString, expectedReturnType);
					}

					if (modifier != null
							&& !modifier.equals(analyzedMethod
									.getAccessModifier())) {
						throw new NavigationException(
								"Method %s found, but has incorrect access type %s (expected %s)",
								AnalyzeUtil.getMethodSignature(name,
										descriptors), analyzedMethod
										.getAccessModifier(), modifier);
					}

					return analyzedMethod;
				}
			}
		}

		throw new NavigationException("Could not find method %s",
				AnalyzeUtil.getMethodSignature(name, descriptors));
	}

}
