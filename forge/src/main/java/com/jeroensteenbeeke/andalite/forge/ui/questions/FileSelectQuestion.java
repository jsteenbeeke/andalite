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

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Question that requires the user to select a file
 * 
 * @author Jeroen Steenbeeke
 *
 */
public abstract class FileSelectQuestion extends AbstractQuestion<File> {
	/**
	 * Create a new FileSelectQuestion with the given question
	 * 
	 * @param question
	 *            The question to ask
	 */
	protected FileSelectQuestion(@Nonnull String question) {
		super(question);
	}

	/**
	 * Give a selection of files the user can choose from
	 * 
	 * @return A list of files
	 */
	@Nonnull
	public abstract List<File> getChoices();

}
