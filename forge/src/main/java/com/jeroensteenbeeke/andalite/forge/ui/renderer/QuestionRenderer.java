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
package com.jeroensteenbeeke.andalite.forge.ui.renderer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.Question;

/**
 * View object for displaying questions to the user
 * 
 * @author Jeroen Steenbeeke
 *
 */
public interface QuestionRenderer {
	/**
	 * Display the given question to the user, and return the resulting action
	 * 
	 * @param question
	 *            The question to display
	 * @return The result of displaying the question. Success indicates the
	 *         question was properly answered, failure means it could not be
	 *         displayed for some reason (which will be indicated by the
	 *         ActionResult's message)
	 */
	@Nonnull
	<T> TypedActionResult<Action> renderQuestion(@Nonnull Question<T> question);
}
