package com.jeroensteenbeeke.andalite.core;

import javax.annotation.Nonnull;

public interface IInsertionPointProvider<T extends IInsertionPointProvider<T,I>, I extends Enum<I> & IInsertionPoint<? super T>> {
	@Nonnull
	@SuppressWarnings("unchecked")
	default Transformation insertAt(@Nonnull I insertionPoint, @Nonnull String replacement) {
		return Transformation.atInsertionPoint((T) this, insertionPoint, replacement);
	}
}
