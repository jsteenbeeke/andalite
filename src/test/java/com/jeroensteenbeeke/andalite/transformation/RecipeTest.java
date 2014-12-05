package com.jeroensteenbeeke.andalite.transformation;

import static com.jeroensteenbeeke.andalite.transformation.ClassLocator.*;
import static com.jeroensteenbeeke.andalite.transformation.Operations.*;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.transformation.RecipeBuilder;

public class RecipeTest {
	@Test
	public void testBuilder() {
		RecipeBuilder builder = new RecipeBuilder();

		builder.atRoot().ensure(imports("javax.persistence.Entity"));
		builder.atRoot().ensure(hasPublicClass());
		builder.inClass(publicClass()).ensure(hasAnnotation("Entity"));
	}
}
