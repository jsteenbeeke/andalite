package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TransformInterfaceTest extends DummyAwareTest {
	@Test
	public void doStuffWithMethod() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicInterface();
		builder.inPublicInterface().ensureDefaultMethod().named("foo");
		builder.inPublicInterface().forMethod().named("foo").inBody()
			   .ensureStatement("System.out.println(\"Foo\")");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareInterface);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedInterface> interfaces = sourceFile.getInterfaces();

		assertThat(interfaces.size(), is(1));

		AnalyzedInterface analyzedInterface = interfaces.get(0);
	}

	@Test
	public void addMethodtoInterface() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicInterface();

		builder.inPublicInterface().ensureMethod().named("a");
		builder.inPublicInterface().ensureDefaultMethod().withModifier(AccessModifier.PROTECTED).named("b");
		builder.inPublicInterface().ensurePrivateMethod().withModifier(AccessModifier.PRIVATE).named("c");
		builder.inPublicInterface().ensureStaticMethod().named("d");
		builder.inPublicInterface().ensurePrivateStaticMethod().withParameter("f").ofType("String").named("e");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareInterface);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedInterface> interfaces = sourceFile.getInterfaces();

		assertThat(interfaces.size(), is(1));

		AnalyzedInterface analyzedInterface = interfaces.get(0);

		Map<String, AnalyzedMethod> methodsByName = analyzedInterface
			.getMethods()
			.stream()
			.collect(Collectors.toMap(AnalyzedMethod::getName, Function
				.identity()));

		assertTrue(methodsByName.containsKey("a"));
		assertTrue(methodsByName.containsKey("b"));
		assertTrue(methodsByName.containsKey("c"));
		assertTrue(methodsByName.containsKey("d"));
		assertTrue(methodsByName.containsKey("e"));



	}
}
