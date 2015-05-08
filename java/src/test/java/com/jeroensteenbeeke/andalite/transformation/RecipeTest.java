/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.transformation;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.*;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.*;
import static com.jeroensteenbeeke.andalite.java.transformation.ClassLocator.*;
import static com.jeroensteenbeeke.andalite.java.transformation.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.DummyAwareTest;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.Operations;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class RecipeTest extends DummyAwareTest {
	@Test
	public void testBuilder() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.atRoot().ensure(imports("javax.persistence.Entity"));
		builder.atRoot().ensure(imports("javax.persistence.Table"));
		builder.atRoot().ensure(imports("javax.persistence.UniqueConstraint"));
		builder.atRoot().ensure(imports("javax.persistence.Column"));
		builder.atRoot().ensure(imports("javax.persistence.ManyToOne"));
		builder.atRoot().ensure(hasPublicClass());
		builder.inClass(publicClass()).ensure(hasClassAnnotation("Entity"));
		builder.inClass(publicClass()).ensure(hasClassAnnotation("Table"));
		builder.inClass(publicClass())
				.forAnnotation("Table")
				.ensure(hasAnnotationValue("uniqueConstraints",
						"UniqueConstraint").whichCouldBeAnArray()
						.ifNotAlreadyPresentWith().value("name", "U_BARE_FOO")
						.arrayValue("columnNames", "foo").get());
		builder.inClass(publicClass()).forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").ifNotAnArray()
				.ensure(hasStringValue("name", "U_BARE_FOO"));
		builder.inClass(publicClass()).forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").ifNotAnArray()
				.ensure(hasStringValue("columnNames", "foo"));
		builder.inClass(publicClass())
				.forAnnotation("Table")
				.ensure(hasAnnotationValue("uniqueConstraints",
						"UniqueConstraint").whichCouldBeAnArray()
						.ifNotAlreadyPresentWith().value("name", "U_BARE_BAR")
						.arrayValue("columnNames", "bar").get());
		builder.inClass(publicClass()).forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").inArray()
				.noValue("name").then()
				.ensure(hasStringValue("name", "U_BARE_BAR"));
		builder.inClass(publicClass()).forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").inArray()
				.value("name", "U_BARE_BAR").then()
				.ensure(hasStringValue("columnNames", "bar"));

		builder.inClass(publicClass()).ensure(
				hasField("foo").typed("String").withAccess(
						AccessModifier.PRIVATE));
		builder.inClass(publicClass()).ensure(
				hasField("bar").typed("String").withAccess(
						AccessModifier.PRIVATE));
		builder.inClass(publicClass()).forField("foo")
				.ensure(hasFieldAnnotation("Column"));
		builder.inClass(publicClass()).forField("bar")
				.ensure(hasFieldAnnotation("Column"));
		builder.inClass(publicClass()).ensure(
				Operations.hasMethod().withParameter("foo").ofType("String")
						.named("setFoo"));
		builder.inClass(publicClass()).ensure(
				Operations.hasMethod().withParameter("bar").ofType("String")
						.named("setBar"));
		builder.inClass(publicClass())
				.ensure(Operations.hasMethod().withReturnType("String")
						.named("getFoo"));
		builder.inClass(publicClass())
				.ensure(Operations.hasMethod().withReturnType("String")
						.named("getBar"));

		builder.inClass(publicClass()).forMethod()
				.withModifier(AccessModifier.PUBLIC).withReturnType("String")
				.named("getFoo").inBody().ensure(returnsAsLastStatement("foo"));

		builder.inClass(publicClass()).forMethod()
				.withModifier(AccessModifier.PUBLIC).withReturnType("String")
				.named("getBar").inBody().ensure(hasStatement("return bar;"));

		builder.inClass(publicClass()).forMethod()
				.withModifier(AccessModifier.PUBLIC).withReturnType("void")
				.withParameter("foo").ofType("String").named("setFoo").inBody()
				.ensure(hasStatement("this.foo = foo;"));

		builder.inClass(publicClass()).forMethod()
				.withModifier(AccessModifier.PUBLIC).withReturnType("void")
				.withParameter("bar").ofType("String").named("setBar").inBody()
				.ensure(hasStatement("this.bar = bar;"));

		JavaRecipe recipe = builder.build();

		File bare = getDummy("BareClass");

		ActionResult result = recipe.applyTo(bare);

		assertThat(result, isOk());

		ClassAnalyzer resultChecker = new ClassAnalyzer(bare);
		TypedActionResult<AnalyzedSourceFile> analysisResult = resultChecker
				.analyze();

		assertThat(analysisResult, isOk());
		AnalyzedSourceFile sourceFile = analysisResult.getObject();

		assertThat(sourceFile, importsClass("javax.persistence.Entity"));
		assertThat(sourceFile,
				importsClass("javax.persistence.UniqueConstraint"));
		assertThat(sourceFile, importsClass("javax.persistence.Table"));
		assertThat(sourceFile, importsClass("javax.persistence.Column"));
		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasAnnotation("Entity"));
		assertThat(analyzedClass, hasAnnotation("Table"));

		AnalyzedAnnotation tableAnnotation = analyzedClass
				.getAnnotation("Table");

		assertThat(tableAnnotation,
				hasValue("uniqueConstraints", ArrayValue.class));

		ArrayValue uniqueConstraints = tableAnnotation.getValue(
				ArrayValue.class, "uniqueConstraints");

		List<BaseValue<?>> values = uniqueConstraints.getValue();

		assertThat(values.size(), equalTo(2));

		BaseValue<?> firstConstraintBase = values.get(0);
		BaseValue<?> secondConstraintBase = values.get(1);

		assertThat(firstConstraintBase, instanceOf(AnnotationValue.class));
		assertThat(secondConstraintBase, instanceOf(AnnotationValue.class));

		AnalyzedAnnotation firstConstraint = ((AnnotationValue) firstConstraintBase)
				.getValue();
		AnalyzedAnnotation secondConstraint = ((AnnotationValue) secondConstraintBase)
				.getValue();

		assertThat(firstConstraint.getType(), equalTo("UniqueConstraint"));
		assertThat(secondConstraint.getType(), equalTo("UniqueConstraint"));

		assertThat(firstConstraint, hasValue("name", StringValue.class));
		assertThat(firstConstraint, hasValue("columnNames", StringValue.class));

		StringValue firstConstraintName = firstConstraint.getValue(
				StringValue.class, "name");
		StringValue firstConstraintColumnNames = firstConstraint.getValue(
				StringValue.class, "columnNames");

		assertThat(firstConstraintName.getValue(), equalTo("U_BARE_FOO"));
		assertThat(firstConstraintColumnNames.getValue(), equalTo("foo"));

		assertThat(secondConstraint, hasValue("name", StringValue.class));
		assertThat(secondConstraint, hasValue("columnNames", StringValue.class));

		StringValue secondConstraintName = secondConstraint.getValue(
				StringValue.class, "name");
		StringValue secondConstraintColumnNames = secondConstraint.getValue(
				StringValue.class, "columnNames");

		assertThat(secondConstraintName.getValue(), equalTo("U_BARE_BAR"));
		assertThat(secondConstraintColumnNames.getValue(), equalTo("bar"));
	}

}
