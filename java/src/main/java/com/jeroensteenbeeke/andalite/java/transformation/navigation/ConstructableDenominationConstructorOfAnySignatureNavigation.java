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
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.analyzer.ConstructableDenomination;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class ConstructableDenominationConstructorOfAnySignatureNavigation<T extends ConstructableDenomination<T,?>>
	extends
		ChainedNavigation<T, AnalyzedConstructor> {
	private final AccessModifier modifier;

	public ConstructableDenominationConstructorOfAnySignatureNavigation(
			@NotNull IJavaNavigation<T> classNavigation,
			@Nullable AccessModifier modifier) {
		super(classNavigation);
		this.modifier = modifier;
	}

	@Override
	public String getStepDescription() {
		return String.format("go to any constructor with access modifier %s",
				modifier.name());
	}

	@Override
	public AnalyzedConstructor navigate(T chainedTarget)
			throws NavigationException {
		for (AnalyzedConstructor analyzedConstructor : chainedTarget
				.getConstructors()) {
			if (modifier != null
					&& !modifier
							.equals(analyzedConstructor.getAccessModifier())) {
				continue;
			}

			return analyzedConstructor;

		}

		throw new NavigationException(
				"Could not find constructor with access modifier %s",
				modifier.name());
	}

}
