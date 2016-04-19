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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

public class ClassConstructorNavigation extends
		ChainedNavigation<AnalyzedClass, AnalyzedConstructor> {
	private final AccessModifier modifier;

	private final List<ParameterDescriptor> descriptors;

	public ClassConstructorNavigation(
			@Nonnull IJavaNavigation<AnalyzedClass> classNavigation,
			@Nullable AccessModifier modifier,
			@Nonnull List<ParameterDescriptor> descriptors) {
		super(classNavigation);
		this.modifier = modifier;
		this.descriptors = descriptors;
	}

	@Override
	public String getStepDescription() {
		return String.format("go to constructor with signature %s",
				AnalyzeUtil.getMethodSignature("", descriptors));
	}

	@Override
	public AnalyzedConstructor navigate(AnalyzedClass chainedTarget)
			throws NavigationException {
		for (AnalyzedConstructor analyzedConstructor : chainedTarget
				.getConstructors()) {
			if (AnalyzeUtil.matchesSignature(analyzedConstructor, descriptors)) {

				if (modifier != null
						&& !modifier.equals(analyzedConstructor
								.getAccessModifier())) {
					throw new NavigationException(
							"Constructor with signature %s found, but has incorrect access type %s (expected %s)",
							AnalyzeUtil.getMethodSignature("", descriptors),
							analyzedConstructor.getAccessModifier(), modifier);
				}

				return analyzedConstructor;
			}
		}

		throw new NavigationException(
				"Could not find constructor with signature %s",
				AnalyzeUtil.getMethodSignature("", descriptors));
	}

}
