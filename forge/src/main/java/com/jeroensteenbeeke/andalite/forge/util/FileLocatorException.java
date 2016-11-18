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
package com.jeroensteenbeeke.andalite.forge.util;

/**
 * Checked exception used by FileLocator
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class FileLocatorException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new file locator exception with the given message
	 * 
	 * @param message
	 *            A format String
	 * @param params
	 *            The parameters for the format strings
	 * @see java.lang.String.format
	 */
	public FileLocatorException(String message, Object... params) {
		super(String.format(message, params));
	}

}
