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
package com.jeroensteenbeeke.andalite.forge.ui.questions;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Question that has a limited set of valid answers, and requires the user to
 * select one
 * 
 * @author Jeroen Steenbeeke
 *
 */
public abstract class MultipleChoiceQuestion extends AbstractQuestion<String> {

	/**
	 * Creates a new MultipleChoiceQuestion for the given question
	 * 
	 * @param question
	 *            The question to ask the user
	 */
	protected MultipleChoiceQuestion(@Nonnull String question) {
		super(question);
	}

	/**
	 * Create a list of valid choices for this question
	 * 
	 * @return A list of answers
	 */
	@Nonnull
	public abstract List<String> getChoices();

}
