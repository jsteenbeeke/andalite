package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import org.jetbrains.annotations.NotNull;

public interface MethodElementTemplate {
	void onMethod(@NotNull JavaRecipeBuilder builder, @NotNull MethodOperationBuilder methodBuilder);
}
