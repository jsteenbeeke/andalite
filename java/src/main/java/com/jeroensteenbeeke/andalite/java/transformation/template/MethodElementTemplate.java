package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public interface MethodElementTemplate {
	void onMethod(@Nonnull JavaRecipeBuilder builder, @Nonnull MethodOperationBuilder methodBuilder);
}
