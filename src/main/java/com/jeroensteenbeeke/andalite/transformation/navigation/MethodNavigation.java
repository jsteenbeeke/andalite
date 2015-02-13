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
package com.jeroensteenbeeke.andalite.transformation.navigation;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.util.AnalyzeUtil;

public class MethodNavigation extends
		ChainedNavigation<AnalyzedClass, AnalyzedMethod> {
	private final String name;

	private final String type;

	private final AccessModifier modifier;

	private final List<ParameterDescriptor> descriptors;

	public MethodNavigation(
			@Nonnull INavigation<AnalyzedClass> classNavigation,
			@Nonnull String name, @Nullable String type,
			@Nullable AccessModifier modifier,
			@Nonnull List<ParameterDescriptor> descriptors) {
		super(classNavigation);
		this.name = name;
		this.type = type;
		this.modifier = modifier;
		this.descriptors = descriptors;
	}

	@Override
	public String getStepDescription() {
		return String.format("Method %s",
				AnalyzeUtil.getMethodSignature(name, descriptors));
	}

	@Override
	public AnalyzedMethod navigate(AnalyzedClass chainedTarget)
			throws NavigationException {
		for (AnalyzedMethod analyzedMethod : chainedTarget.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					if (type != null
							&& !type.equals(analyzedMethod.getReturnType()
									.toJavaString())) {
						throw new NavigationException(
								"Method %s found, but has incorrect return type %s (expected %s)",
								AnalyzeUtil.getMethodSignature(name,
										descriptors), analyzedMethod
										.getReturnType().toJavaString(), type);
					}

					if (modifier != null
							&& !modifier.equals(analyzedMethod
									.getAccessModifier())) {
						throw new NavigationException(
								"Method %s found, but has incorrect access type %s (expected %s)",
								AnalyzeUtil.getMethodSignature(name,
										descriptors), analyzedMethod
										.getReturnType(), modifier);
					}

					return analyzedMethod;
				}
			}
		}

		throw new NavigationException("Could not find method %s",
				AnalyzeUtil.getMethodSignature(name, descriptors));
	}

}
