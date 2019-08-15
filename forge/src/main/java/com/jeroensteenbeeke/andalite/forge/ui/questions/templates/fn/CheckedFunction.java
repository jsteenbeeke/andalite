package com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn;

import com.jeroensteenbeeke.andalite.forge.ForgeException;

@FunctionalInterface
public interface CheckedFunction<T,U> {
	U apply(T input) throws ForgeException;
}
