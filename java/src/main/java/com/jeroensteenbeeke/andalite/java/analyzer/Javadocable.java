package com.jeroensteenbeeke.andalite.java.analyzer;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface Javadocable {
	/**
	 * @return Any javadoc for this element, or an empty optional if none is present
	 */
	Optional<String> getJavadoc();

	/**
	 * @non.public
	 */
	void setJavadoc(@NotNull String javadoc);
}
