package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.Result;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstructorTest extends DummyAwareTest {
	@Test
	public void testConstructorStatementInsertion() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder
			.inPublicClass()
			.ensureConstructor()
			.withParameter("a")
			.ofType("String")
			.withParameter("b")
			.ofType("String")
			.withAccessModifier(AccessModifier.PUBLIC);

		builder.inPublicClass()
			   .ensureField("a").typed("String").withAccess(AccessModifier.PRIVATE);
		builder.inPublicClass()
			   .ensureField("b").typed("String").withAccess(AccessModifier.PRIVATE);

		builder.inPublicClass().forConstructor().withParameter("a").ofType("String")
			   .withParameter("b")
			   .ofType("String")
			   .withModifier(AccessModifier.PUBLIC).get()
			   .inBody()
			   .ensureStatement("this.a = a");
		builder.inPublicClass().forConstructor().withParameter("a").ofType("String")
			   .withParameter("b")
			   .ofType("String")
			   .withModifier(AccessModifier.PUBLIC).get()
			   .inBody()
			   .ensureStatement("this.b = b");

		builder.inPublicClass().forConstructor().withParameter("a").ofType("String")
			   .withParameter("b")
			   .ofType("String")
			   .withModifier(AccessModifier.PUBLIC).get().addConstructorParameter("c").ofType("String");

		builder
			.inPublicClass()
			.forConstructor()
			.withParameter("a")
			.ofType("String")
			.withParameter("b")
			.ofType("String")
			.withParameter("c")
			.ofType("String")
			.withModifier(AccessModifier.PUBLIC)
			.get()
			.forParameterNamed("c")
			.ofType("String")
			.ensureAnnotation("Nonnull");

		builder
			.inPublicClass()
			.forConstructor()
			.withParameter("a")
			.ofType("String")
			.withParameter("b")
			.ofType("String")
			.withParameter("c")
			.ofType("String")
			.withModifier(AccessModifier.PUBLIC)
			.get()
			.forParameterAtIndex(0)
			.ensureAnnotation("Nonnull");

		builder
			.inPublicClass()
			.forConstructor()
			.withParameter("a")
			.ofType("String")
			.withParameter("b")
			.ofType("String")
			.withParameter("c")
			.ofType("String")
			.withModifier(AccessModifier.PUBLIC)
			.get()
			.forParameterAtIndex(2)
			.removeAnnotation("Nonnull");

		builder
			.inPublicClass()
			.forConstructor()
			.andAnySignature()
			.ensureAnnotation("Nonnull");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		Result<?, ?> result = recipe.applyTo(bare);

		assertThat(result, isOk());
	}

}
