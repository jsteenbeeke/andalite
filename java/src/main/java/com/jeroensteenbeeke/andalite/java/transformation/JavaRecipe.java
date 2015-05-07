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

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;

public class JavaRecipe {
	private static final Logger logger = LoggerFactory
			.getLogger(JavaRecipe.class);

	private final List<JavaRecipeStep<?>> steps;

	JavaRecipe(List<JavaRecipeStep<?>> steps) {
		super();
		this.steps = steps;
	}

	public ActionResult applyTo(File file) {
		logger.debug("Applying transformation ({} steps) to {}", steps.size(),
				file.getName());

		for (JavaRecipeStep<?> step : steps) {

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
				logger.debug("OK: {}", step.toString());
			}
		}

		logger.debug("All steps executed, checking if resulting file can be parsed");

		return new ClassAnalyzer(file).analyze();
	}
}
