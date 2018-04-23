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

import com.jeroensteenbeeke.hyperion.util.Result;
import org.junit.Test;

import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class FirstMethodTest extends DummyAwareTest {
	@Test
	public void testBuilder() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureMethod().withParameter("foo")
				.ofType("String").named("setFoo");

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		Result<?,?> result = recipe.applyTo(bare);

		assertThat(result, isOk());
	}

	@Test
	public void testFinalModifier() throws IOException {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		builder.inPublicClass().ensureMethod().withParameter("foo")
				.ofType("String").named("setFoo");
		builder.inPublicClass().forMethod().withParameter("foo")
				.ofType("String").named("setFoo").ensureFinal();

		JavaRecipe recipe = builder.build();

		File bare = getDummy(BaseDummies.BareClass);

		Result<?,?> result = recipe.applyTo(bare);

		assertThat(result, isOk());
	}

}
