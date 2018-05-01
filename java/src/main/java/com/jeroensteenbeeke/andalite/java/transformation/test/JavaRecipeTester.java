package com.jeroensteenbeeke.andalite.java.transformation.test;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.lux.TypedResult;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeStep;

public class JavaRecipeTester {
	public static void assertValid(JavaRecipe recipe, File baseFile) {
		List<JavaRecipeStep<?>> steps = recipe.getSteps();

		TypedResult<AnalyzedSourceFile> parseResult = new ClassAnalyzer(
				baseFile).analyze();
		assertThat("Well-formed Java file before transformation", parseResult,
				isOk());

		for (JavaRecipeStep<?> step : steps) {
			AnalyzedSourceFile sourceFile = parseResult.getObject();
			ActionResult transformResult = step.perform(sourceFile);
			assertThat("Transformation successful", transformResult, isOk());
			parseResult = new ClassAnalyzer(baseFile).analyze();
			assertThat("Well-formed Java file after transformation",
					parseResult, isOk());
			sourceFile = parseResult.getObject();
			ActionResult verificationResult = step.verify(sourceFile);
			assertThat("Verification passes", verificationResult, isOk());

		}

	}
}
