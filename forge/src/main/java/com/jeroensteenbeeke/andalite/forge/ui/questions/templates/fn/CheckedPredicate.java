package com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn;

import com.jeroensteenbeeke.andalite.forge.ForgeException;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedPredicate<T> {
	boolean test(@NotNull T input) throws ForgeException;
}
