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

package com.jeroensteenbeeke.andalite.core.exceptions;

import javax.annotation.Nonnull;

/**
 * Exception thrown when navigating through a source file
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class NavigationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a new NavigationException with the given message
	 * 
	 * @param message
	 *            The reason for failure. Can either be a plain string, or a
	 *            format String as used in {@code String.format}. This may not
	 *            be null or empty
	 * @param params
	 *            Parameter objects to pass to {@code String.format}. Optional,
	 *            but may not be null
	 */
	public NavigationException(@Nonnull String message,
			@Nonnull Object... params) {
		super(params.length > 0 ? String.format(message, params) : message);
	}

}
