package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.FieldOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public interface FieldElementTemplate {
	void onField(JavaRecipeBuilder builder, FieldOperationBuilder fieldBuilder);
}
