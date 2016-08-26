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
import com.jeroensteenbeeke.andalite.java.transformation.Operations;

public class EnumTest extends DummyAwareTest {
	@Test
	public void testAddConstant() throws IOException {
		File enumFile = getDummy(BaseDummies.BareEnum);

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inPublicEnum()
				.ensure(Operations.hasEnumConstant().named("Test"));
		JavaRecipe addEnumConstant = builder.build();

		ActionResult result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum()
				.ensure(Operations.hasEnumConstant().named("AnotherTest"));
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensure(Operations.hasEnumMethod().named("foo"));
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

	}

	@Test
	public void testAddParameter() throws IOException {
		File enumFile = getDummy(BaseDummies.EnumWithStringParam);

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensure(Operations.hasEnumConstant()
				.withStringParameterExpression("This is a test").named("Test"));
		JavaRecipe addEnumConstant = builder.build();

		ActionResult result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum()
				.ensure(Operations.hasEnumConstant()
						.withStringParameterExpression("This is another test")
						.named("AnotherTest"));
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.inPublicEnum().ensure(Operations.hasEnumMethod().named("foo"));
		addEnumConstant = builder.build();

		result = addEnumConstant.applyTo(enumFile);

		assertThat(result, isOk());

	}
}
