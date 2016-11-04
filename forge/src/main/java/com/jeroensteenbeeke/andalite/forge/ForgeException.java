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
package com.jeroensteenbeeke.andalite.forge;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.util.Strings;

/**
 * Exception thrown by Forge recipes on execution failure
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class ForgeException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new ForgeException with the given message
	 * 
	 * @param messageFormat
	 *            Either a simple message (if {@code params} has 0 elements), or
	 *            a message format as used by {@code String.format}
	 * @param params
	 *            The parameters to pass to {@code String.format}, optional
	 */
	public ForgeException(@Nonnull String messageFormat,
			@Nonnull Object... params) {
		super(Strings.conditionalFormat(messageFormat, params));
	}

	/**
	 * Creates a new ForgeException with the given cause message
	 * 
	 * @param cause
	 *            The exception that caused this message
	 * @param messageFormat
	 *            Either a simple message (if {@code params} has 0 elements), or
	 *            a message format as used by {@code String.format}
	 * @param params
	 *            The parameters to pass to {@code String.format}, optional
	 */
	public ForgeException(@Nonnull Throwable cause,
			@Nonnull String messageFormat, @Nonnull Object... params) {
		super(Strings.conditionalFormat(messageFormat, params), cause);
	}

}
