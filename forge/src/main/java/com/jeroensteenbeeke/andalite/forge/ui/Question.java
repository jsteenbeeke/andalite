/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.forge.ui;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.forge.ForgeException;

/**
 * A Question is an action that requests user feedback.
 * 
 * @author Jeroen Steenbeeke
 *
 * @param <T>
 *            The type of answer this question expects
 */
public interface Question<T> extends Action {
	/**
	 * The question to ask the user
	 * 
	 * @return A String containing the question
	 */
	@Nonnull
	String getQuestion();

	/**
	 * The action to undertake upon a given answer
	 * 
	 * @param answer
	 *            The answer given by the user
	 * @param handler
	 *            A callback for processing feedback
	 * @return The next action to undertake after this action
	 * @throws ForgeException
	 *             If handling the answer creates an unrecoverable error
	 */
	@Nonnull
	Action onAnswer(@Nonnull T answer, @Nonnull FeedbackHandler handler)
			throws ForgeException;
}
