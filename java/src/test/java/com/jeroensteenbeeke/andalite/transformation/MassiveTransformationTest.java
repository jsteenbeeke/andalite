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

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static com.jeroensteenbeeke.andalite.java.transformation.ClassLocator.publicClass;
import static com.jeroensteenbeeke.andalite.java.transformation.Operations.hasPublicClass;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.ClassLocator;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.Operations;

public class MassiveTransformationTest extends DummyAwareTest {
	private static final int OPERATIONS = 500;

	@Test
	public void testMassiveTransformation() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.atRoot().ensure(hasPublicClass());
		for (int i = 0; i < OPERATIONS; i++) {
			System.out.printf("Adding method %d", i);
			System.out.println();
			builder.inClass(publicClass()).ensure(
					Operations.hasMethod().withModifier(AccessModifier.PUBLIC)
							.withParameter(String.format("foo%d", i))
							.ofType("String")
							.named(String.format("setFoo%d", i)));
		}

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		System.out.print("Applying recipe...");

		ActionResult result = recipe.applyTo(bare);

		assertThat(result, isOk());

		System.out.println("OK");

		builder = new JavaRecipeBuilder();
		builder.atRoot().ensure(Operations.imports("javax.annotation.Nonnull"));

		for (int i = 0; i < OPERATIONS; i++) {
			System.out.printf("Adding comments and annotation to method %d", i);
			System.out.println();
			// builder.inClass(ClassLocator.publicClass())
			// .forMethod()
			// .withModifier(AccessModifier.PUBLIC)
			// .withParameter(String.format("foo%d", i))
			// .ofType("String")
			// .named(String.format("setFoo%d", i))
			// .ensure(Operations
			// .hasMethodComment("This is an automatically generated method"));

			builder.inClass(ClassLocator.publicClass())
					.forMethod()
					.withModifier(AccessModifier.PUBLIC)
					.withParameter(String.format("foo%d", i))
					.ofType("String")
					.named(String.format("setFoo%d", i))
					.ensure(Operations
							.hasMethodJavadoc("This is an automatically generated method"));

			builder.inClass(ClassLocator.publicClass()).forMethod()
					.withModifier(AccessModifier.PUBLIC)
					.withParameter(String.format("foo%d", i)).ofType("String")
					.named(String.format("setFoo%d", i))
					.forParameterNamed(String.format("foo%d", i))
					.ofType("String")
					.ensure(Operations.hasParameterAnnotation("Nonnull"));
			;
		}
		recipe = builder.build();

		System.out.print("Applying recipe...");

		result = recipe.applyTo(bare);

		assertThat(result, isOk());

		System.out.println("OK");

	}

}
