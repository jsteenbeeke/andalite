package com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn;

import com.jeroensteenbeeke.andalite.forge.ForgeException;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface CheckedPredicate<T> {
	boolean test(@Nonnull T input) throws ForgeException;
}
