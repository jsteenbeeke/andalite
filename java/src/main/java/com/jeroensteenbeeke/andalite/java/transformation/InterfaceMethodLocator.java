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

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.InterfaceMethodNavigation;

public class InterfaceMethodLocator extends
		AbstractMethodBuilder<MethodOperationBuilder, InterfaceMethodLocator> {

	private final IStepCollector collector;

	private final IJavaNavigation<AnalyzedInterface> parentNavigation;

	InterfaceMethodLocator(IStepCollector collector,
			IJavaNavigation<AnalyzedInterface> parentNavigation) {
		super(null, null);
		this.collector = collector;
		this.parentNavigation = parentNavigation;

	}

	@NotNull
	public MethodOperationBuilder named(@NotNull String name) {
		return new MethodOperationBuilder(collector,
				new InterfaceMethodNavigation(parentNavigation, name,
						getType(), getDescriptors()));
	}

}
