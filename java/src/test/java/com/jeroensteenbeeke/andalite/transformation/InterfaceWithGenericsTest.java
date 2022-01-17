package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.Result;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterfaceWithGenericsTest extends DummyAwareTest {
	@Test
	public void testBuilder() throws IOException {
		JavaRecipeBuilder java = new JavaRecipeBuilder();

		java.inPublicClass().ensureImplements("Map<String,Object>");

		JavaRecipe recipe = java.build();

		File bare = getDummy(BaseDummies.BareClass);

		Result<?,?> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		ClassAnalyzer resultChecker = new ClassAnalyzer(bare);
		TypedResult<AnalyzedSourceFile> analysisResult = resultChecker
				.analyze();

		assertThat(analysisResult, isOk());

	}

	@Test
	public void addPackageInterface() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.ensurePackageInterface("FooBar");
		builder.inPackageInterface("FooBar").ensureSuperInterface("Serializable");
		builder.inPackageInterface("FooBar").ensureSuperInterface("Cloneable");


		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

	}
}
