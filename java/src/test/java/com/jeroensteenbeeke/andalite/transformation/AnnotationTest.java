package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.IntegerValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnotationTest extends DummyAwareTest {
	@Test
	public void testAnnotationOperations() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureAnnotation("Table");
		builder.inPublicClass().forAnnotation("Table")
			.ensureStringValue("name", "test");
		builder.inPublicClass().forAnnotation("Table")
			.ensureIntegerValue("increment", 5);
		builder.inPublicClass().forAnnotation("Table")
			.ensureAnnotationValue("uniqueConstraints", "UniqueConstraint")
			.whichCouldBeAnArray();

		builder.inPublicClass().forAnnotation("Table")
			.forAnnotationField("uniqueConstraints").ifNotAnArray()
			.ensureStringValue("name", "U_1");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		TypedResult<AnalyzedSourceFile> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedClass> classes = sourceFile.getClasses();

		assertThat(classes.size(), equalTo(1));

		AnalyzedClass analyzedClass = classes.get(0);

		assertTrue(analyzedClass.hasAnnotation("Table"));

		AnalyzedAnnotation table = analyzedClass.getAnnotation("Table");

		assertNotNull(table);
		assertTrue(table.hasValueOfType(StringValue.class, "name"));
		assertTrue(table.hasValueOfType(IntegerValue.class, "increment"));

	}
}
