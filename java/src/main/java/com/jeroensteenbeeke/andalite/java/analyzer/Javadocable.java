package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface Javadocable {
	/**
	 * @return Any javadoc for this element, or {@code null} if none is present
	 */
	@CheckForNull
	String getJavadoc();

	/**
	 * @non.public
	 */
	void setJavadoc(@Nonnull String javadoc);
}
