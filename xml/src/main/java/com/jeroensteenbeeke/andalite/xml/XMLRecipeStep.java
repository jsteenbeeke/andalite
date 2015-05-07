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

package com.jeroensteenbeeke.andalite.xml;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;

public class XMLRecipeStep<T extends ILocatable> {
	private final Logger logger = LoggerFactory.getLogger(XMLRecipeStep.class);

	private final IXMLNavigation<T> navigation;

	private final IXMLOperation<T> operation;

	XMLRecipeStep(IXMLNavigation<T> navigation, IXMLOperation<T> operation) {
		super();
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult perform(File file) {
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
