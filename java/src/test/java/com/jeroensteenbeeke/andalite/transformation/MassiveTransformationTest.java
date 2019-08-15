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

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.Result;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.junit.Assert.assertThat;

@Ignore
public class MassiveTransformationTest extends DummyAwareTest {
	private static final int OPERATIONS = 100;

	private static final SecureRandom random = initRandom();

	private static final String AVAILABLE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static SecureRandom initRandom() {
		try {
			return SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			// If SHA1PRNG does not exist, then go with default implementation
			return new SecureRandom();
		}
	}

	@Test
	public void testMassiveTransformation() throws IOException {
		Map<Integer, String> paramNames = Maps.newHashMap();

		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();
		;
		for (int i = 0; i < OPERATIONS; i++) {
			String randomParam = "_".concat(random(40));
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

	/**
	 * Create a string of random alphanumerical characters, with the given length
	 * @param length The target length
	 * @return A String of the given length containing random alphanumerical characters
	 */
	public static String random(int length) {
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < length; i++) {
			password.append(randomCharacter());
		}

		return password.toString();
	}

	private static char randomCharacter() {
		return AVAILABLE_CHARS.charAt(random.nextInt(AVAILABLE_CHARS.length()));
	}
}
