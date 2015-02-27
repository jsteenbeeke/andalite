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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.ILocatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.NavigationException;
import com.jeroensteenbeeke.andalite.transformation.operations.IOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public class RecipeStep<T extends ILocatable> {
	private final Logger logger = LoggerFactory.getLogger(RecipeStep.class);

	private final INavigation<T> navigation;

	private final IOperation<T> operation;

	RecipeStep(INavigation<T> navigation, IOperation<T> operation) {
		super();
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult perform(AnalyzedSourceFile file) {
		T target;
		try {
			target = navigation.navigate(file);
		} catch (NavigationException e) {
			logger.error(e.getMessage(), e);
			return ActionResult.error(String.format(
					"Navigation (%s) failed: %s", navigation.getDescription(),
					e.getMessage()));
		}
		if (target != null) {
			List<Transformation> transformations;
			try {
				transformations = operation.perform(target);
			} catch (OperationException e) {
				logger.error(e.getMessage(), e);
				return ActionResult.error("Operation cannot be performed: %s",
						e.getMessage());
			}
			for (Transformation transformation : transformations) {
				ActionResult result = transformation.applyTo(file
						.getOriginalFile());
				if (!result.isOk()) {
					return result;
				}
			}

			return ActionResult.ok();
		} else {
			return ActionResult.error("Could not navigate to %s",
					navigation.getDescription());
		}
	}

	@Override
	public String toString() {
		return String.format("Go to %s and then ensure %s",
				navigation.getDescription(), operation.getDescription());
	}
}
