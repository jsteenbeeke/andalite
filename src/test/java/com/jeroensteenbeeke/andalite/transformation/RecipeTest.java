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

import static com.jeroensteenbeeke.andalite.ResultMatchers.*;
import static com.jeroensteenbeeke.andalite.analyzer.matchers.AndaliteMatchers.*;
import static com.jeroensteenbeeke.andalite.transformation.ClassLocator.*;
import static com.jeroensteenbeeke.andalite.transformation.Operations.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.DummyAwareTest;
import com.jeroensteenbeeke.andalite.TypedActionResult;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.ClassAnalyzer;

public class RecipeTest extends DummyAwareTest {
	@Test
	public void testBuilder() throws IOException {
		RecipeBuilder builder = new RecipeBuilder();

		builder.atRoot().ensure(imports("javax.persistence.Entity"));
		builder.atRoot().ensure(imports("javax.persistence.Column"));
		builder.atRoot().ensure(hasPublicClass());
		builder.inClass(publicClass()).ensure(hasClassAnnotation("Entity"));
		builder.inClass(publicClass()).ensure(
				hasField("foo").typed("String").withAccess(
						AccessModifier.PRIVATE));
		builder.inClass(publicClass()).forField("foo")
				.ensure(hasFieldAnnotation("Column"));
		builder.inClass(publicClass()).ensure(
				hasMethod().withParameter("foo").ofType("String")
						.named("setFoo"));

		builder.inClass(publicClass()).ensure(
				hasMethod().withReturnType("String").named("getFoo"));

		Recipe recipe = builder.build();

		File bare = getDummy("BareClass");

		ActionResult result = recipe.applyTo(bare);

		assertThat(result, isOk());

		ClassAnalyzer resultChecker = new ClassAnalyzer(bare);
		TypedActionResult<AnalyzedSourceFile> analysisResult = resultChecker
				.analyze();

		assertThat(analysisResult, isOk());
		AnalyzedSourceFile sourceFile = analysisResult.getObject();

		assertThat(sourceFile, importsClass("javax.persistence.Entity"));
		assertThat(sourceFile, importsClass("javax.persistence.Column"));
		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasAnnotation("Entity"));

	}
}
