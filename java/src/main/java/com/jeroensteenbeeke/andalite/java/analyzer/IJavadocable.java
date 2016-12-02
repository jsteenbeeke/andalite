package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Element that can have Javadoc comments
 * 
 * @author Jeroen Steenbeeke
 */
public interface IJavadocable {
	/**
	 * @return Any javadoc for this element, or {@code null} if none is present
	 */
	@CheckForNull
	String getJavadoc();

	/**
	 * Set the javadoc for this element
	 * 
	 * @non.public
	 */
	void setJavadoc(@Nonnull String javadoc);
}
