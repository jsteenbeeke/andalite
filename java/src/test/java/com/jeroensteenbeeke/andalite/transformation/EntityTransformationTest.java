package com.jeroensteenbeeke.andalite.transformation;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.TypedResult;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

public class EntityTransformationTest extends DummyAwareTest
{
	@Test
	void add_nullability_annotations() throws IOException
	{
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensureImport("org.jetbrains.annotations.NotNull");

		builder.inPublicClass()
			.forMethod()
			.withParameter("id")
			.ofType("Long")
			.named("setId")
			.forParameterNamed("id")
			.ofType("Long")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.named("getId")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.withParameter("rules")
			.ofType("List<FooBarRule>")
			.named("setRules")
			.forParameterNamed("rules")
			.ofType("List<FooBarRule>")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.named("getRules")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.withParameter("name")
			.ofType("String")
			.named("setName")
			.forParameterNamed("name")
			.ofType("String")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.named("getName")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.withParameter("owner")
			.ofType("User")
			.named("setOwner")
			.forParameterNamed("owner")
			.ofType("User")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.named("getOwner")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.withParameter("templates")
			.ofType("List<FooBarTemplate>")
			.named("setTemplates")
			.forParameterNamed("templates")
			.ofType("List<FooBarTemplate>")
			.ensureAnnotation("NotNull");

		builder.inPublicClass()
			.forMethod()
			.named("getTemplates")
			.ensureAnnotation("NotNull");

		JavaRecipe recipe = builder.build();

		File entity = getDummy(BaseDummies.Entity);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(entity);

		assertThat(result, isOk());

		Try.of(() -> Files.readString(result.getObject().getOriginalFile().toPath()))
			.onSuccess(System.out::println);
	}
}
