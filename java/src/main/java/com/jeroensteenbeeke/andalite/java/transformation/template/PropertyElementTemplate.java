package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.FieldOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

public interface PropertyElementTemplate extends FieldElementTemplate{
	default void onSetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {

	}

	default void onGetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {

	}

	default void onOptionalGetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {

	}

	default void onFluentSetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {

	}

	@Override
	default void onField(JavaRecipeBuilder builder, FieldOperationBuilder fieldBuilder) {

	}
}
