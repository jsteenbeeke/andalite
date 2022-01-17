package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.hasError;
import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldInsertionTest extends DummyAwareTest {
	@Test
	public void testFieldInsertions() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("b").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("b").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeFinal();
		builder.inPublicClass().ensureField("b").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeFinal();
		builder.inPublicClass().ensureField("c").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("c").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeStatic();
		builder.inPublicClass().ensureField("c").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeStatic();
		builder.inPublicClass().ensureField("d").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("d").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeVolatile();
		builder.inPublicClass().ensureField("d").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeVolatile();

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), equalTo(1));

		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass.hasField("a"));
		assertTrue(analyzedClass.hasField("b"));
		assertTrue(analyzedClass.hasField("c"));
		assertTrue(analyzedClass.hasField("d"));

		assertThat(analyzedClass.getField("a").getAccessModifier(), equalTo(AccessModifier.PRIVATE));
		assertThat(analyzedClass.getField("b").getAccessModifier(), equalTo(AccessModifier.PRIVATE));
		assertThat(analyzedClass.getField("c").getAccessModifier(), equalTo(AccessModifier.PRIVATE));
		assertThat(analyzedClass.getField("d").getAccessModifier(), equalTo(AccessModifier.PRIVATE));

		assertTrue(analyzedClass.getField("b").isFinal());
		assertTrue(analyzedClass.getField("c").isStatic());
		assertTrue(analyzedClass.getField("d").isVolatile());

	}

	@Test
	public void testInvalidNonVolatileModification() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeVolatile();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, hasError("Navigation: Public Class\nOperation: presence of field: private  String a\nTransformation result: Operation cannot be performed: Field a should not be volatile, but is"));

	}

	@Test
	public void testInvalidNonStaticModification() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeStatic();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, hasError("Navigation: Public Class\nOperation: presence of field: private  String a\nTransformation result: Operation cannot be performed: Field a should not be static, but is"));

	}


	@Test
	public void testInvalidNonFinalModification() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE).shouldBeFinal();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, hasError("Navigation: Public Class\nOperation: presence of field: private  String a\nTransformation result: Operation cannot be performed: Field a should not be final, but is"));

	}

	@Test
	public void testInvalidAccessModification() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PUBLIC);

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, hasError("Navigation: Public Class\nOperation: presence of field: public  String a\nTransformation result: Operation cannot be performed: Field a should have access modifier public but instead has access modifier private"));

	}

	@Test
	public void testInvalidTypeModification() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("a").typed("int").withAccess(
			AccessModifier.PRIVATE);

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, hasError("Navigation: Public Class\nOperation: presence of field: private  int a\nTransformation result: Operation cannot be performed: Field a should have type int but instead has type String"));

	}

	@Test
	public void testArrayInsertion() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureField("a").typed("String[]").withAccess(
			AccessModifier.PRIVATE);
		builder.inPublicClass().ensureField("b").typed("int[]").withAccess(
			AccessModifier.PRIVATE);

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());
	}
}
