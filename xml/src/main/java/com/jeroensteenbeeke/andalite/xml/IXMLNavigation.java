package com.jeroensteenbeeke.andalite.xml;

import java.io.File;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;

public interface IXMLNavigation<T> {
	@Nonnull
	T navigate(@Nonnull File file) throws NavigationException;

	@Nonnull
	String getDescription();
}
