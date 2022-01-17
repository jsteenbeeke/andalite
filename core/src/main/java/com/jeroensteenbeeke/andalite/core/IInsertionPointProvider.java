package com.jeroensteenbeeke.andalite.core;

import org.jetbrains.annotations.NotNull;

public interface IInsertionPointProvider<T extends IInsertionPointProvider<T,I>, I extends Enum<I> & IInsertionPoint<? super T>> {
	@NotNull
	@SuppressWarnings("unchecked")
	default Transformation insertAt(@NotNull I insertionPoint, @NotNull String replacement) {
		return Transformation.atInsertionPoint((T) this, insertionPoint, replacement);
	}
}
