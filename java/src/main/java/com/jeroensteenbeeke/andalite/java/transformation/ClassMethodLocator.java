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
package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ContainingDenominationMethodNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;

import org.jetbrains.annotations.NotNull;

public class ClassMethodLocator extends
		AbstractMethodBuilder<MethodOperationBuilder, ClassMethodLocator> {

	private final IStepCollector collector;

	private final IJavaNavigation<AnalyzedClass> parentNavigation;

	ClassMethodLocator(IStepCollector collector,
			IJavaNavigation<AnalyzedClass> parentNavigation) {
		super(null, null);
		this.collector = collector;
		this.parentNavigation = parentNavigation;

	}

	@NotNull
	public MethodOperationBuilder named(@NotNull String name) {
		return new MethodOperationBuilder(collector, new ContainingDenominationMethodNavigation<>(
				parentNavigation, name, getType(), getModifier(),
				getDescriptors()));
	}

}
