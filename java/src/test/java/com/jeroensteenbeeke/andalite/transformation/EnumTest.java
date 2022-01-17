package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.Result;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.MatcherAssert.assertThat;

public class EnumTest extends DummyAwareTest {
	@Test
	public void testAddConstant() throws IOException {
		File enumFile = getDummy(BaseDummies.BareEnum);

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant().named("Test");
		JavaRecipe addEnumConstant = builder.build();

		Result<?, ?> result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant().named("AnotherTest");
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant().named("foo");
		builder
			.inPublicEnum()
			.ensureMethod()
			.withModifier(AccessModifier.PUBLIC)
			.withReturnType("String")
			.named("getDescription");
		builder
			.inPublicEnum()
			.forMethod()
			.withModifier(AccessModifier.PUBLIC)
			.withReturnType("String")
			.named("getDescription")
			.inBody()
			.ensureReturnAsLastStatement("\"Test\"");
		builder
			.inPublicEnum()
			.forConstant("foo")
			.ensureMethod()
			.withModifier(AccessModifier.PUBLIC)
			.withReturnType("String")
			.named("getBar");
		builder
			.inPublicEnum()
			.forConstant("foo")
			.forMethod()
			.withModifier(AccessModifier.PUBLIC)
			.withReturnType("String")
			.named("getBar")
			.inBody()
			.ensureReturnAsLastStatement("\"BAR\"");

		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

	}

	@Test
	public void testAddParameter() throws IOException {
		File enumFile = getDummy(BaseDummies.EnumWithStringParam);

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant()
			   .withStringParameterExpression("This is a test").named("Test");
		JavaRecipe addEnumConstant = builder.build();

		Result<?, ?> result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant()
			   .withStringParameterExpression("This is another test")
			   .named("AnotherTest");
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant().named("foo");
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());
	}

	@Test
	public void addPublicEnum() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.ensurePublicEnum();

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

	}

	@Test
	public void addPackageEnum() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.ensurePackageEnum("FooBar");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

	}
}
