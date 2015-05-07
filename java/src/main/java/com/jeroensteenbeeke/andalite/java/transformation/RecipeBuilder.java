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

import java.util.List;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;

public class RecipeBuilder implements IStepCollector {
	private final List<JavaRecipeStep<?>> steps;

	public RecipeBuilder() {
		this.steps = Lists.newArrayList();
	}

	@Override
	public <T extends ILocatable> void addStep(IJavaNavigation<T> nav,
			IJavaOperation<T> oper) {
		this.steps.add(new JavaRecipeStep<T>(nav, oper));
	}

	public CompilationUnitOperationBuilder atRoot() {
		return new CompilationUnitOperationBuilder(this);
	}

	public ClassScopeOperationBuilder inClass(ClassLocator locator) {
		return new ClassScopeOperationBuilder(this, locator.getNavigation());
	}

	public JavaRecipe build() {
		return new JavaRecipe(steps);
	}

}
