package com.jeroensteenbeeke.andalite.core;

public interface IInsertionPoint<T extends IInsertionPointProvider<? super T,?>> {
	int position(T container);
}
