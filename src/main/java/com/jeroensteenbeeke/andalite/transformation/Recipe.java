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

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.TypedActionResult;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.ClassAnalyzer;

public class Recipe {
	private static final Logger logger = LoggerFactory.getLogger(Recipe.class);

	private final List<RecipeStep<?>> steps;

	Recipe(List<RecipeStep<?>> steps) {
		super();
		this.steps = steps;
	}

	public ActionResult applyTo(File file) {
		logger.info("Applying transformation ({} steps) to {}", steps.size(),
				file.getName());

		for (RecipeStep<?> step : steps) {

			TypedActionResult<AnalyzedSourceFile> result = new ClassAnalyzer(
					file).analyze();

			if (!result.isOk()) {
				logger.error("ERROR, could not read file: {}",
						result.getMessage());

				return ActionResult.error(result.getMessage());

			}

			ActionResult stepResult = step.perform(result.getObject());

			if (!stepResult.isOk()) {
				logger.error("ERROR: {} {}", result.getMessage(),
						step.toString());
				return stepResult;
			} else {
				logger.info("OK: {}", step.toString());
			}
		}

		return new ClassAnalyzer(file).analyze();
	}
}
