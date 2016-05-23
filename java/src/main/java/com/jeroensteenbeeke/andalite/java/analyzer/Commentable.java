package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

public interface Commentable {
	/**
	 * @non.public
	 */
	void addComment(@Nonnull String comment);

	@Nonnull
	List<String> getComments();
}
