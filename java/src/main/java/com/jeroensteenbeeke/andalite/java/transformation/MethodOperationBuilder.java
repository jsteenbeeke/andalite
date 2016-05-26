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

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.BodyContainerNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ByIndexMethodParameterNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.MethodParameterNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

public class MethodOperationBuilder extends
		AbstractOperationBuilder<AnalyzedMethod, IMethodOperation> {

	public static class ParameterLocator {
		private final MethodOperationBuilder parent;

		private final String name;

		private ParameterLocator(MethodOperationBuilder parent, String name) {
			super();
			this.parent = parent;
			this.name = name;
		}

		public ParameterScopeOperationBuilder ofType(String type) {
			return new ParameterScopeOperationBuilder(parent.getCollector(),
					new MethodParameterNavigation(parent.getNavigation(), type,
							name));
		}
	}

	MethodOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedMethod> navigation) {
		super(collector, navigation);
	}

	public ParameterLocator forParameterNamed(String name) {
		return new ParameterLocator(this, name);
	}

	public BodyContainerOperationBuilder inBody() {
		return new BodyContainerOperationBuilder(getCollector(),
				new BodyContainerNavigation<AnalyzedMethod>(getNavigation()));
	}

	/**
	 * @param index
	 *            The 0-based index of the parameter
	 */
	public ParameterScopeOperationBuilder forParameterAtIndex(int index) {
		return new ParameterScopeOperationBuilder(getCollector(),
				new ByIndexMethodParameterNavigation(getNavigation(), index));
	}
}
