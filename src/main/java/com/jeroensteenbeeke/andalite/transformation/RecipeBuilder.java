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

import java.util.List;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;

public class RecipeBuilder {
	private final List<RecipeStep<?>> steps;

	public RecipeBuilder() {
		this.steps = Lists.newArrayList();
	}

	<T extends Locatable> void addStep(Navigation<T> nav, Operation<T> oper) {
		this.steps.add(new RecipeStep<T>(nav, oper));
	}

	public CompilationUnitOperationBuilder atRoot() {
		return new CompilationUnitOperationBuilder(this);
	}

	public ClassScopeOperationBuilder inClass(ClassLocator locator) {
		return new ClassScopeOperationBuilder(this, locator.getNavigation());
	}

}
