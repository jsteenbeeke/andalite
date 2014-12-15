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
package com.jeroensteenbeeke.andalite.transformation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.transformation.navigation.MethodNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;

public class MethodLocator extends
		AbstractMethodBuilder<MethodOperationBuilder> {

	private final StepCollector collector;

	private final Navigation<AnalyzedClass> parentNavigation;

	MethodLocator(StepCollector collector,
			Navigation<AnalyzedClass> parentNavigation) {
		super(null, null);
		this.collector = collector;
		this.parentNavigation = parentNavigation;

	}

	public MethodOperationBuilder named(@Nonnull String name) {
		return new MethodOperationBuilder(collector, new MethodNavigation(
				parentNavigation, name, getType(), getModifier(),
				getDescriptors()));
	}

}
