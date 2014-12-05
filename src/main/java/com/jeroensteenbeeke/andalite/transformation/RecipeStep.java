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

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.NavigationException;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;

public class RecipeStep<T extends Locatable> {
	private final Navigation<T> navigation;

	private final Operation<T> operation;

	RecipeStep(Navigation<T> navigation, Operation<T> operation) {
		super();
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult perform(AnalyzedSourceFile file) {
		T target;
		try {
			target = navigation.navigate(file);
		} catch (NavigationException e) {
			return ActionResult.error(e.getMessage());
		}
		if (target != null) {
			List<Transformation> transformations = operation.perform(target);
			for (Transformation transformation : transformations) {
				ActionResult result = transformation.applyTo(file
						.getOriginalFile());
				if (!result.isOk()) {
					return result;
				}
			}

			return ActionResult.ok();
		} else {
			return ActionResult.error("Could not locate %s",
					navigation.getDescription());
		}
	}
}
