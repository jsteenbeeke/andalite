package com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn;

import com.jeroensteenbeeke.andalite.forge.ForgeException;

@FunctionalInterface
public interface CheckedBiFunction<T,U, R> {
	R apply(T t, U u) throws ForgeException;
}
