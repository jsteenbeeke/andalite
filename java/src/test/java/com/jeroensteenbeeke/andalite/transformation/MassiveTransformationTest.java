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
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.jeroensteenbeeke.hyperion.util.Randomizer;
import com.jeroensteenbeeke.hyperion.util.Result;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class MassiveTransformationTest extends DummyAwareTest {
	private static final int OPERATIONS = 100;

	@Test
	public void testMassiveTransformation() throws IOException {
		Map<Integer, String> paramNames = Maps.newHashMap();

		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		;
		for (int i = 0; i < OPERATIONS; i++) {
			String randomParam = "_".concat(Randomizer.random(40));
			paramNames.put(i, randomParam);
			builder.inPublicClass().ensureMethod()
					.withModifier(AccessModifier.PUBLIC)
					.withParameter(randomParam).ofType("String")
					.named(String.format("setFoo%d", i));
		}

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClassWithGenericType);

		Result<?,?> result = recipe.applyTo(bare);

		assertThat(result, isOk());

		builder = new JavaRecipeBuilder();
		builder.ensureImport("javax.annotation.Nonnull");

		for (int i = 0; i < OPERATIONS; i++) {

			String paramName = paramNames.get(i);
			builder.inPublicClass().forMethod()
					.withModifier(AccessModifier.PUBLIC)
					.withParameter(paramName).ofType("String")
					.named(String.format("setFoo%d", i))
					.ensureJavadoc("This is an automatically generated method");

			builder.inPublicClass().forMethod()
					.withModifier(AccessModifier.PUBLIC)
					.withParameter(paramName).ofType("String")
					.named(String.format("setFoo%d", i)).forParameterAtIndex(0)
					.ensureAnnotation("Nonnull");
			;
		}
		recipe = builder.build();

		result = recipe.applyTo(bare);

		assertThat(result, isOk());
	}

}
