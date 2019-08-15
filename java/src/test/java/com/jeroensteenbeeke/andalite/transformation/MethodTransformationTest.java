package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MethodTransformationTest extends DummyAwareTest {
	@Test
	public void doStuffWithMethod() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePackageClass("FooBar");
		builder.inPackageClass("FooBar").ensureMethod().withParameter("amount").ofType("int").named("corn");
		builder.inPackageClass("FooBar").forMethod().withParameter("amount").ofType("int").named("corn")
			   .forParameterAtIndex(0).ensureAnnotation("Nonnull");
		builder.inPackageClass("FooBar").forMethod().withParameter("amount").ofType("int").named("corn")
			   .forParameterAtIndex(0).removeAnnotation("Nonnull");
		builder.inPackageClass("FooBar").forMethod().withParameter("amount").ofType("int").named("corn")
			   .forParameterNamed("amount").ofType("int").removeAnnotation("Nonnull");
		builder.inPackageClass("FooBar").forMethod().withParameter("amount").ofType("int").named("corn")
			   .forParameterNamed("amount").ofType("int").removeAnnotation("Nonnull");
		builder
			.inPackageClass("FooBar")
			.forMethod()
			.withParameter("amount")
			.ofType("int")
			.named("corn")
			.ensureAnnotation("Nonnull");
		builder
			.inPackageClass("FooBar")
			.forMethod()
			.withParameter("amount")
			.ofType("int")
			.named("corn")
			.removeAnnotation("Nonnull");

		builder
			.inPackageClass("FooBar")
			.forMethod()
			.withParameter("amount")
			.ofType("int")
			.named("corn")
			.ensureComment("Test");

		builder
			.inPackageClass("FooBar")
			.forMethod()
			.withParameter("amount")
			.ofType("int")
			.named("corn")
			.ensureJavadoc("I am Javadoc");

		builder
			.inPackageClass("FooBar")
			.forMethod()
			.withParameter("amount")
			.ofType("int")
			.named("corn")
			.ensureException("Exception");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareInterface);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));

		AnalyzedClass analyzedClass = classes.get(0);
	}
}
