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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.forge.ui.Question;

/**
 * Abstract base class for the Question interface
 * 
 * @author Jeroen Steenbeeke
 *
 * @param <T>
 *            The type of answer the question expects
 */
public abstract class AbstractQuestion<T> implements Question<T> {
	private final String question;

	/**
	 * Create a new question with the given question String
	 * 
	 * @param question
	 *            The question to ask the user
	 */
	protected AbstractQuestion(@Nonnull String question) {
		this.question = question;
	}

	@Override
	public String getQuestion() {
		return question;
	}
}
