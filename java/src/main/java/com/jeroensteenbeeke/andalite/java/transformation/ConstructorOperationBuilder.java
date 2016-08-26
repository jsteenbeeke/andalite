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

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.BodyContainerNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ByIndexConstructorParameterNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ConstructorParameterNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IConstructorOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.ConstructorParameterAdditionBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureConstructorAnnotation;

public class ConstructorOperationBuilder extends
		AbstractOperationBuilder<AnalyzedConstructor, IConstructorOperation> {

	public static class ParameterLocator {
		private final ConstructorOperationBuilder parent;

		private final String name;

		private ParameterLocator(@Nonnull ConstructorOperationBuilder parent,
				@Nonnull String name) {
			super();
			this.parent = parent;
			this.name = name;
		}

		public ParameterScopeOperationBuilder ofType(@Nonnull String type) {
			return new ParameterScopeOperationBuilder(parent.getCollector(),
					new ConstructorParameterNavigation(parent.getNavigation(),
							type, name));
		}
	}

	ConstructorOperationBuilder(@Nonnull IStepCollector collector,
			@Nonnull IJavaNavigation<AnalyzedConstructor> navigation) {
		super(collector, navigation);
	}

	@Nonnull
	public ParameterLocator forParameterNamed(@Nonnull String name) {
		return new ParameterLocator(this, name);
	}

	/**
	 * @param index
	 *            The 0-based index of the parameter
	 */
	@Nonnull
	public ParameterScopeOperationBuilder forParameterAtIndex(int index) {
		return new ParameterScopeOperationBuilder(getCollector(),
				new ByIndexConstructorParameterNavigation(getNavigation(),
						index));
	}

	@Nonnull
	public BodyContainerOperationBuilder inBody() {
		return new BodyContainerOperationBuilder(getCollector(),
				new BodyContainerNavigation<AnalyzedConstructor>(
						getNavigation()));
	}

	@Nonnull
	public ConstructorParameterAdditionBuilder addConstructorParameter(
			@Nonnull String name) {
		return new ConstructorParameterAdditionBuilder(name, o -> ensure(o));
	}

	public void ensureAnnotation(@Nonnull String annotation) {
		ensure(new EnsureConstructorAnnotation(annotation));
	}
}
