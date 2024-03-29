package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.NamedReturnType;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.VoidReturnType;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static com.jeroensteenbeeke.andalite.java.transformation.template.Templates.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemplatesTest extends DummyAwareTest {
	@Test
	public void testRegularClass() throws IOException {
		JavaRecipe recipe = aPublicClass().toRecipe();

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
			.withSuperClass("java.util.AbstractList").toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass.getSuperClass().map(GenerifiedName::getName)
								.filter("AbstractList"::equals).isPresent());
	}

	@Test
	public void testRegularClassWithInterface() throws IOException {
		JavaRecipe recipe = aPublicClass()
			.withImplementedInterface("java.io.Serializable")
			.withImplementedInterface("java.io.Serializable").toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass.getSuperClass().isEmpty());
		assertThat(analyzedClass.getInterfaces().size(), equalTo(1));
		assertTrue(analyzedClass.implementsInterface("Serializable"));
	}

	@Test
	public void testRegularClassWithInterfacesAndSuperclass()
		throws IOException {
		JavaRecipe recipe = aPublicClass()
			.withSuperClass("java.util.AbstractList")
			.withImplementedInterface("java.io.Serializable")
			.withImplementedInterface("java.lang.Cloneable").toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass.getSuperClass().map(GenerifiedName::getName)
								.filter("AbstractList"::equals).isPresent());
		assertTrue(analyzedClass.implementsInterface("Serializable"));
	}

	@Test
	public void testProperties() throws IOException {
		JavaRecipe recipe = aPublicClass().with(
			property("foo").ofType("String").nonNull()
						   .with(setterParameterAnnotation("org.jetbrains.annotations.NotNull"),
								 getterAnnotation("org.jetbrains.annotations.NotNull")),
			property("bar").ofType("int"),
			property("baz").ofType("java.util.Date").with(
				fieldAnnotation("javax.persistence.Column")
					.withValues(stringField("name").withValue("BAZ"),
								booleanField("nullable").withValue(false),
								fieldAccessField("precision").withValue("Constants.DEFAULT_PRECISION")),
				setterParameterAnnotation("javax.annotation.Nullable")
					.withValues(stringField("areYouSure").withValue("yes"),
								classField("nullabilityClass").withValue("Nullious")),
				getterAnnotation("javax.annotation.CheckForNull")
					.withValues(intField("howNullable").withValue(5)),
				optionalGetterAnnotation("org.jetbrains.annotations.NotNull")
					.withValues(booleanField("really").withValue(true))),
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

		AnalyzedMethod setFoo = analyzedClass
			.getMethod()
			.withParameterOfType("String")
			.withReturnType(VoidReturnType.VOID)
			.named("setFoo");
		AnalyzedMethod setBar = analyzedClass
			.getMethod()
			.withParameterOfType("int")
			.withReturnType(VoidReturnType.VOID)
			.named("setBar");
		AnalyzedMethod setBaz = analyzedClass
			.getMethod()
			.withParameterOfType("Date")
			.withReturnType(VoidReturnType.VOID)
			.named("setBaz");
		AnalyzedMethod setUnits = analyzedClass
			.getMethod()
			.withParameterOfType("List<Unit>")
			.withReturnType(VoidReturnType.VOID)
			.named("setUnits");

		assertThat(setFoo, notNullValue());
		assertThat(setBar, notNullValue());
		assertThat(setBaz, notNullValue());
		assertThat(setUnits, notNullValue());

		assertThat(setFoo.getParameters().get(0).getName(), equalTo("foo"));
		assertThat(setBar.getParameters().get(0).getName(), equalTo("bar"));
		assertThat(setBaz.getParameters().get(0).getName(), equalTo("baz"));
		assertThat(setUnits.getParameters().get(0).getName(), equalTo("units"));


	}

	@Test
	public void testConditionals() throws IOException {
		JavaRecipe recipe = aPublicClass().with(
			property("foo").ofType("String").nullable(true)
						   .ifMatched(PropertyTemplate::isNullable)
						   .include(optionalGetterAnnotation("org.jetbrains.annotations.NotNull"),
									setterParameterAnnotation("javax.annotation.Nullable"),
									getterAnnotation("javax.annotation.CheckForNull"))
						   .ifNotMatched(PropertyTemplate::isNullable)
						   .include(setterParameterAnnotation("org.jetbrains.annotations.NotNull"),
									getterAnnotation("org.jetbrains.annotations.NotNull")),
			property("bar").ofType("String").nullable(false)
						   .ifMatched(PropertyTemplate::isNullable)
						   .include(setterParameterAnnotation("javax.annotation.Nullable"),
									getterAnnotation("javax.annotation.CheckForNull"))
						   .ifNotMatched(PropertyTemplate::isNullable)
						   .include(setterParameterAnnotation("org.jetbrains.annotations.NotNull"),
									getterAnnotation("org.jetbrains.annotations.NotNull"))).toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), is(1));
		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass.getMethod().withModifier(AccessModifier.PUBLIC)
								.withReturnType(new NamedReturnType("String")).named("getFoo")
								.hasAnnotation("CheckForNull"));
		assertTrue(analyzedClass.getMethod().withModifier(AccessModifier.PUBLIC)
								.withReturnType(new NamedReturnType("Optional<String>")).named("foo")
								.hasAnnotation("NotNull"));
		AnalyzedMethod setFoo = analyzedClass.getMethod()
											 .withModifier(AccessModifier.PUBLIC).withReturnType(VoidReturnType.VOID)
											 .withParameterOfType("String").named("setFoo");
		assertTrue(setFoo.getParameters().get(0).hasAnnotation("Nullable"));
		assertTrue(analyzedClass.getMethod().withModifier(AccessModifier.PUBLIC)
								.withReturnType(new NamedReturnType("String")).named("getBar").hasAnnotation("NotNull"));

		assertTrue(analyzedClass.getMethod().withModifier(AccessModifier.PUBLIC)
								.withParameterOfType("String").named("setBar").getParameters()
								.get(0).hasAnnotation("NotNull"));
	}

	@Test
	public void testEnum() throws Exception {
		JavaRecipe recipe = aPublicEnum()
			.withImplementedInterface("java.io.Serializable")
			.withImplementedInterface("java.lang.Cloneable")
			.toRecipe();

		File bare = getDummy(BaseDummies.Empty);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();
		assertThat(sourceFile.getEnums().size(), equalTo(1));

		List<AnalyzedEnum> enums = sourceFile.getEnums();

		AnalyzedEnum anEnum = enums.get(0);

		assertTrue(anEnum.implementsInterface("Serializable"));
	}
}
