package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static com.jeroensteenbeeke.andalite.java.transformation.template.Templates.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TemplatesTest extends DummyAwareTest {
	@Test
	public void testRegularClass() throws IOException {
		JavaRecipe recipe = aPublicClass()
			.toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
	}

	@Test
	public void testRegularClassWithSuperclass() throws IOException {
		JavaRecipe recipe = aPublicClass()
			.withSuperClass("java.util.AbstractList")
			.toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass
					   .getSuperClass()
					   .map(GenerifiedName::getName)
					   .filter("AbstractList"::equals)
					   .isPresent());
	}

	@Test
	public void testRegularClassWithInterface() throws IOException {
		JavaRecipe recipe = aPublicClass()
			.withImplementedInterface("java.io.Serializable")
			.toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass
					   .getSuperClass().isEmpty());
		assertTrue(analyzedClass.implementsInterface("Serializable"));
	}

	@Test
	public void testRegularClassWithInterfacesAndSuperclass() throws IOException {
		JavaRecipe recipe = aPublicClass()
			.withSuperClass("java.util.AbstractList")
			.withImplementedInterface("java.io.Serializable")
			.withImplementedInterface("java.lang.Cloneable")
			.toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass
					   .getSuperClass()
					   .map(GenerifiedName::getName)
					   .filter("AbstractList"::equals)
					   .isPresent());
		assertTrue(analyzedClass.implementsInterface("Serializable"));
	}

	@Test
	public void testProperties() throws IOException {
		JavaRecipe recipe = aPublicClass().with(
			property("foo").ofType("String").nonNull().with(
				setterParameterAnnotation("javax.annotation.Nonnull"),
				getterAnnotation("javax.annotation.Nonnull")
			),
			property("bar").ofType("int"),
			property("baz").ofType("java.util.Date").with(
				fieldAnnotation("javax.persistence.Column").withValues(
					stringField("name").withValue("BAZ"),
					booleanField("nullable").withValue(false)
				),
				setterParameterAnnotation("javax.annotation.Nullable").withValues(
					stringField("areYouSure").withValue("yes")
				),
				getterAnnotation("javax.annotation.CheckForNull").withValues(
					intField("howNullable").withValue(5)
				),
				optionalGetterAnnotation("javax.annotation.Nonnull").withValues(
					booleanField("really").withValue(true)
				)
			),
			property("units").ofType("java.util.List<Unit>").nonNull()
							 .whichRequiresImport("foo.bar.Unit")

		).toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);
	}

	@Test
	public void testConditionals() throws IOException {
		JavaRecipe recipe = aPublicClass().with(
			property("foo")
				.ofType("String")
				.nullable(true)
				.ifMatched(PropertyTemplate::isNullable).include(
				optionalGetterAnnotation("javax.annotation.Nonnull"),
				setterParameterAnnotation("javax.annotation.Nullable"),
				getterAnnotation("javax.annotation.CheckForNull")
			).ifNotMatched(PropertyTemplate::isNullable).include(
				setterParameterAnnotation("javax.annotation.Nonnull"),
				getterAnnotation("javax.annotation.Nonnull")
			),
			property("bar")
				.ofType("String")
				.nullable(false)
				.ifMatched(PropertyTemplate::isNullable).include(
				setterParameterAnnotation("javax.annotation.Nullable"),
				getterAnnotation("javax.annotation.CheckForNull")
			).ifNotMatched(PropertyTemplate::isNullable).include(
				setterParameterAnnotation("javax.annotation.Nonnull"),
				getterAnnotation("javax.annotation.Nonnull")
			)
		).toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass
					   .getMethod()
					   .withModifier(AccessModifier.PUBLIC)
					   .withReturnType("String")
					   .named("getFoo")
					   .hasAnnotation("CheckForNull"));
		assertTrue(analyzedClass
					   .getMethod()
					   .withModifier(AccessModifier.PUBLIC)
					   .withReturnType("Optional<String>")
					   .named("foo")
					   .hasAnnotation("Nonnull"));
		AnalyzedMethod setFoo = analyzedClass
			.getMethod()
			.withModifier(AccessModifier.PUBLIC)
			.withReturnType("void")
			.withParameterOfType("String")
			.named("setFoo");
		assertTrue(setFoo.getParameters().get(0).hasAnnotation("Nullable"));
		assertTrue(analyzedClass
					   .getMethod()
					   .withModifier(AccessModifier.PUBLIC)
					   .withReturnType("String")
					   .named("getBar")
					   .hasAnnotation("Nonnull"));

		assertTrue(analyzedClass
					   .getMethod()
					   .withModifier(AccessModifier.PUBLIC)
					   .withParameterOfType("String")
					   .named("setBar")
					   .getParameters().get(0).hasAnnotation("Nonnull"));
	}
}
