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

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ContainingDenominationMethodNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;

import javax.annotation.Nonnull;

public class EnumMethodLocator extends
		AbstractMethodBuilder<MethodOperationBuilder, EnumMethodLocator> {

	private final IStepCollector collector;

	private final IJavaNavigation<AnalyzedEnum> parentNavigation;

	EnumMethodLocator(IStepCollector collector,
					  IJavaNavigation<AnalyzedEnum> parentNavigation) {
		super(null, null);
		this.collector = collector;
		this.parentNavigation = parentNavigation;

	}

	@Nonnull
	public MethodOperationBuilder named(@Nonnull String name) {
		return new MethodOperationBuilder(collector, new ContainingDenominationMethodNavigation<>(
			parentNavigation, name, getType(), getModifier(),
			getDescriptors()));
	}

}
