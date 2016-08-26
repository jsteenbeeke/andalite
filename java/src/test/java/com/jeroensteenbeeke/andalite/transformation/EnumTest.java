package com.jeroensteenbeeke.andalite.transformation;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class EnumTest extends DummyAwareTest {
	@Test
	public void testAddConstant() throws IOException {
		File enumFile = getDummy(BaseDummies.BareEnum);

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant().named("Test");
		JavaRecipe addEnumConstant = builder.build();

		ActionResult result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant().named("AnotherTest");
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
	public void testAddParameter() throws IOException {
		File enumFile = getDummy(BaseDummies.EnumWithStringParam);

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensureEnumConstant()
				.withStringParameterExpression("This is a test").named("Test");
		JavaRecipe addEnumConstant = builder.build();

		ActionResult result = addEnumConstant.applyTo(enumFile);

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
}
