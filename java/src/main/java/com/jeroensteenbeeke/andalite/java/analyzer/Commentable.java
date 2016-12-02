package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Representation of an element that can have comments
 * 
 * @author Jeroen Steenbeeke
 */
public interface Commentable {
	/**
	 * Add a comment to this commentable
	 * 
	 * @param comment
	 *            the comment to add
	 * 
	 * @non.public
	 */
	void addComment(@Nonnull String comment);

	/**
	 * Get the comments of this element
	 * 
	 * @return The list of comments, possibly immutable
	 */
	@Nonnull
	List<String> getComments();
}
