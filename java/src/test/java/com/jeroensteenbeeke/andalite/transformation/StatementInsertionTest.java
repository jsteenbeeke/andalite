package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.Result;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.MatcherAssert.assertThat;

public class StatementInsertionTest extends DummyAwareTest {
	@Test
	public void testStatementInsertion() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureMethod().named("foo");
		builder.inPublicClass().forMethod().named("foo").inBody().ensureStatement("String a = \"\";");
		builder
			.inPublicClass()
			.forMethod()
			.named("foo")
			.inBody()
			.afterStatement("String a = \"\";")
			.ensureNextStatement("String b = \"\";");

		builder
			.inPublicClass()
			.forMethod()
			.named("foo")
			.inBody()
			.ensureReturnAsLastStatement("a + b");

		builder
			.inPublicClass()
			.forMethod()
			.named("foo")
			.inBody()
			.afterStatement("return a + b")
			.ensureNextStatement("a = \"aaaa\";");

		builder
			.inPublicClass()
			.forMethod()
			.named("foo")
			.inBody()
			.ensureIfStatement("b.isEmpty()");

		builder.inPublicClass()
			   .forMethod()
			   .named("foo")
			   .inBody()
			   .inIfStatement()
			   .withExpression("b.isEmpty()")
			   .thenStatement()
			   .ensureNextStatement("b = b + a");

		builder.inPublicClass()
			   .forMethod()
			   .named("foo")
			   .inBody()
			   .inIfStatement()
			   .withExpression("b.isEmpty()")
			   .wholeStatement().ensureElseBlock();

		builder.inPublicClass()
			   .forMethod()
			   .named("foo")
			   .inBody()
			   .inIfStatement()
			   .withExpression("b.isEmpty()")
			   .elseStatement()
			   .ensureNextStatement("b = b + b");


		builder.inPublicClass()
			   .forMethod()
			   .named("foo")
			   .inBody()
			   .inIfStatement()
			   .withExpression("b.isEmpty()")
			   .elseStatement()
			   .body()
			   .afterStatement("b = b + b")
			   .ensurePrefixComment("Your mom");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		Result<?, ?> result = recipe.applyTo(bare);

		assertThat(result, isOk());
	}

}
