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

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.NamedReturnType;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.VoidReturnType;
import com.jeroensteenbeeke.lux.Result;
import com.jeroensteenbeeke.lux.TypedResult;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class RecipeTest extends DummyAwareTest {
	@Test
	public void testBuilder() throws IOException {
		JavaRecipeBuilder java = new JavaRecipeBuilder();

		java.ensureImport("javax.persistence.Entity");
		java.ensureImport("javax.persistence.Table");
		java.ensureImport("javax.persistence.UniqueConstraint");
		java.ensureImport("javax.persistence.Column");
		java.ensureImport("javax.persistence.ManyToOne");
		java.ensurePublicClass();
		java.inPublicClass().ensureAnnotation("Entity");
		java.inPublicClass().ensureAnnotation("Table");
		java.inPublicClass().forAnnotation("Table")
				.ensureAnnotationValue("uniqueConstraints", "UniqueConstraint")
				.whichCouldBeAnArray().ifNotAlreadyPresentWith()
				.value("name", "U_BARE_FOO").arrayValue("columnNames", "foo")
				.get();
		java.inPublicClass().forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").ifNotAnArray()
				.ensureStringValue("name", "U_BARE_FOO");
		java.inPublicClass().forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").ifNotAnArray()
				.ensureStringValue("columnNames", "foo");
		java.inPublicClass().forAnnotation("Table")
				.ensureAnnotationValue("uniqueConstraints", "UniqueConstraint")
				.whichCouldBeAnArray().ifNotAlreadyPresentWith()
				.value("name", "U_BARE_BAR").arrayValue("columnNames", "bar")
				.get();
		java.inPublicClass().forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").inArray()
				.noValueOrEquals("name", "U_BARE_BAR").then()
				.ensureStringValue("name", "U_BARE_BAR");
		java.inPublicClass().forAnnotation("Table")
				.forAnnotationField("uniqueConstraints").inArray()
				.value("name", "U_BARE_BAR").then()
				.ensureStringValue("columnNames", "bar");

		java.inPublicClass().ensureField("foo").typed("String")
				.withAccess(AccessModifier.PRIVATE);
		java.inPublicClass().ensureField("bar").typed("String")
				.withAccess(AccessModifier.PRIVATE);
		java.inPublicClass().forField("foo").ensureAnnotation("Column");
		java.inPublicClass().forField("bar").ensureAnnotation("Column");
		java.inPublicClass().ensureMethod().withParameter("foo")
				.ofType("String").named("setFoo");
		java.inPublicClass().ensureMethod().withParameter("bar")
				.ofType("String").named("setBar");
		java.inPublicClass().ensureMethod().withReturnType(new NamedReturnType("String"))
				.named("getFoo");
		java.inPublicClass().ensureMethod().withReturnType(new NamedReturnType("String"))
				.named("getBar");

		java.inPublicClass().forMethod().withModifier(AccessModifier.PUBLIC)
				.withReturnType(new NamedReturnType("String")).named("getFoo").inBody()
				.ensureReturnAsLastStatement("foo");

		java.inPublicClass().forMethod().withModifier(AccessModifier.PUBLIC)
				.withReturnType(new NamedReturnType("String")).named("getBar").inBody()
				.ensureStatement("return bar;");

		java.inPublicClass().forMethod().withModifier(AccessModifier.PUBLIC)
				.withReturnType(VoidReturnType.VOID).withParameter("foo").ofType("String")
				.named("setFoo").inBody().ensureStatement("this.foo = foo;");

		java.inPublicClass().forMethod().withModifier(AccessModifier.PUBLIC)
				.withReturnType(VoidReturnType.VOID).withParameter("bar").ofType("String")
				.named("setBar").inBody().ensureStatement("this.bar = bar;");

		java.ensureImport(
				"com.jeroensteenbeeke.hyperion.data.BaseDomainObject");

		java.inPublicClass().ensureSuperclass("BaseDomainObject");

		JavaRecipe recipe = java.build();

		File bare = getDummy(BaseDummies.BareClass);

		Result<?,?> result = recipe.applyTo(bare);

		MatcherAssert.assertThat(result, isOk());

		ClassAnalyzer resultChecker = new ClassAnalyzer(bare);
		TypedResult<AnalyzedSourceFile> analysisResult = resultChecker
				.analyze();

		MatcherAssert.assertThat(analysisResult, isOk());
		AnalyzedSourceFile sourceFile = analysisResult.getObject();

		assertThat(sourceFile, importsClass("javax.persistence.Entity"));
		assertThat(sourceFile,
				importsClass("javax.persistence.UniqueConstraint"));
		assertThat(sourceFile, importsClass("javax.persistence.Table"));
		assertThat(sourceFile, importsClass("javax.persistence.Column"));
		assertThat(sourceFile, importsClass(
				"com.jeroensteenbeeke.hyperion.data.BaseDomainObject"));
		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasAnnotation("Entity"));
		assertThat(analyzedClass, hasAnnotation("Table"));

		assertThat(analyzedClass, extendsClass("BaseDomainObject"));

		AnalyzedAnnotation tableAnnotation = analyzedClass
				.getAnnotation("Table");

		assertThat(tableAnnotation,
				hasValue("uniqueConstraints", ArrayValue.class));

		ArrayValue uniqueConstraints = tableAnnotation
				.getValue(ArrayValue.class, "uniqueConstraints");

		List<BaseValue<?,?,?>> values = uniqueConstraints.getValue();

		assertThat(values.size(), equalTo(2));

		BaseValue<?,?,?> firstConstraintBase = values.get(0);
		BaseValue<?,?,?> secondConstraintBase = values.get(1);

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

		StringValue firstConstraintName = firstConstraint
				.getValue(StringValue.class, "name");
		StringValue firstConstraintColumnNames = firstConstraint
				.getValue(StringValue.class, "columnNames");

		assertThat(firstConstraintName.getValue(), equalTo("U_BARE_FOO"));
		assertThat(firstConstraintColumnNames.getValue(), equalTo("foo"));

		assertThat(secondConstraint, hasValue("name", StringValue.class));
		assertThat(secondConstraint,
				hasValue("columnNames", StringValue.class));

		StringValue secondConstraintName = secondConstraint
				.getValue(StringValue.class, "name");
		StringValue secondConstraintColumnNames = secondConstraint
				.getValue(StringValue.class, "columnNames");

		assertThat(secondConstraintName.getValue(), equalTo("U_BARE_BAR"));
		assertThat(secondConstraintColumnNames.getValue(), equalTo("bar"));
	}

}
