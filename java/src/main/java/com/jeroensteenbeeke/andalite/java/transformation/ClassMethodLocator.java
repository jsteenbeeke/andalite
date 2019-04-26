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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ClassMethodNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;

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

	@Nonnull
	public MethodOperationBuilder named(@Nonnull String name) {
		return new MethodOperationBuilder(collector, new ClassMethodNavigation(
				parentNavigation, name, getType(), getModifier(),
				getDescriptors()));
	}

}
