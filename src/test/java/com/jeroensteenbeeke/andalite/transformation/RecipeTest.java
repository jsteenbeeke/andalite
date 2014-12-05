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

import static com.jeroensteenbeeke.andalite.transformation.ClassLocator.*;
import static com.jeroensteenbeeke.andalite.transformation.Operations.*;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;

public class RecipeTest {
	@Test
	public void testBuilder() {
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
	}
}
