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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.filer.FormattingFiler;
import com.jeroensteenbeeke.lux.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.lux.TypedResult;
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
				logger.error("ERROR: {} {}", result.getMessage(),
							 step.toString());
				return TypedResult.fail("Navigation: %s\nOperation: %s\nTransformation result: %s",
										step.navigationToString(),
										step.operationToString(),
										stepResult.getMessage());
			} else {
				logger.debug("OK: {}", step.toString());
			}

			try {
				System.out.printf("// OK: %s", step.toString()).println();

				Files.lines(file.toPath()).forEach(System.out::println);
			} catch (IOException e) {
				return TypedResult.fail(e.getMessage());
			}


			result = new ClassAnalyzer(file).analyze();

			if (!result.isOk()) {
				logger.error("ERROR: transformation rendered file unparseable");
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
				Formatter formatter = new Formatter();

				Files.writeString(file.toPath(), formatter.formatSourceAndFixImports(Files.readString(file.toPath())));

			} catch (FormatterException | IOException e) {
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
