package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface Commentable {
	/**
	 * @non.public
	 */
	void addComment(@NotNull String comment);

	@NotNull
	List<String> getComments();
}
