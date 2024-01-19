/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.lux.TypedResult;
import io.spring.javaformat.formatter.FileFormatter;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class JavaRecipe {
	private static final Logger logger = LoggerFactory
		.getLogger(JavaRecipe.class);

	private final List<JavaRecipeStep<?>> steps;

	JavaRecipe(List<JavaRecipeStep<?>> steps) {
		super();
		this.steps = steps;
	}

	public List<JavaRecipeStep<?>> getSteps() {
		return steps;
	}

	public TypedResult<AnalyzedSourceFile> applyTo(File file) {
		logger.debug("Applying transformation ({} steps) to {}", steps.size(),
					 file.getName());

		JavaRecipeStep<?> prev = null;

		for (JavaRecipeStep<?> step : steps) {

			TypedResult<AnalyzedSourceFile> result = new ClassAnalyzer(
				file).analyze();

			if (!result.isOk()) {
				logger.error("ERROR, could not read file: {}",
							 result.getMessage());
				if (prev != null) {
					logger.error("Previous action: {}", prev.toString());
				}

				return TypedResult.fail(
					"Navigation: %s\nOperation: %s\nParse result: %s",
					step.navigationToString(), step.operationToString(),
					result.getMessage());

			}

			ActionResult stepResult = step.perform(result.getObject());

			if (!stepResult.isOk()) {
				logger.error("ERROR: {} {}", stepResult.getMessage(),
							 step);
				return TypedResult.fail("Navigation: %s\nOperation: %s\nTransformation result: %s",
										step.navigationToString(),
										step.operationToString(),
										stepResult.getMessage());
			} else {
				logger.debug("OK: {}", step);
			}

			result = new ClassAnalyzer(file).analyze();

			if (!result.isOk()) {
				logger.error("ERROR: transformation rendered file {} unparseable", file.getPath());
				Try.of(() -> Files.readString(file.toPath())).onSuccess(logger::error);
				return TypedResult.fail(
					"Navigation: %s\nOperation: %s\nParse result: %s",
					step.navigationToString(), step.operationToString(),
					result.getMessage());
			}

			ActionResult verify = step.verify(result.getObject());

			if (!verify.isOk()) {
				logger.error("ERROR: recipe step failed verification");
				return TypedResult.fail("Navigation: %s\nOperation: %s\nVerification result: %s",
										step.navigationToString(),
										step.operationToString(),
										verify.getMessage());
			}

			try {

				FileFormatter formatter = new FileFormatter();

				String contents = formatter.formatFile(file, StandardCharsets.UTF_8).getFormattedContent();

				Files.writeString(file.toPath(), contents);

			} catch (Exception e) {
				return TypedResult.fail("Could not format source after transform: %s, %s", e
					.getClass()
					.getSimpleName(), e.getMessage());
			}

			prev = step;
		}

		logger.debug("All steps executed, checking if resulting file can be parsed");

		return new ClassAnalyzer(file).analyze();
	}
}
