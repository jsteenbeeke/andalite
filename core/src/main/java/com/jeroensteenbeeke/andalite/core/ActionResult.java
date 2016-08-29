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

package com.jeroensteenbeeke.andalite.core;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * General-purpose return object, meant as a replacement for returning a single boolean. Indicates
 * success or failure, and in case of failure, why exactly the action failed.
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class ActionResult {
	private final boolean ok;

	private final String message;

	/**
	 * Creates a new ActionResult
	 * @param ok Whether or not the action was a success
	 * @param message The reason for the current status. Left {@code null} when ok
	 */
	protected ActionResult(boolean ok, @Nullable String message) {
		this.ok = ok;
		this.message = message;
	}

	/**
	 * Indicates whether or not this is a successful result
	 * @return {@code true} if successful, {@code false} otherwise
	 */
	public final boolean isOk() {
		return ok;
	}

	/**
	 * In case of failure, this message indicates the reason for failure
	 * @return A message indicating the cause of failure if {@code isOk() returns {@code false}, {@code null} otherwise 
	 */
	@CheckForNull
	public final String getMessage() {
		return message;
	}

	/**
	 * Create a new result that indicates success
	 * @return An {@code ActionResult} object without a message, and {@code isOk()} returning {@code true}
	 */
	@Nonnull
	public static ActionResult ok() {
		return new ActionResult(true, null);
	}

	/**
	 * Create a new result that indicates failure
	 * @param message The reason for failure. Can either be a plain string, or a format String as used in {@code String.format}. This may not be null or empty
	 * @param params Parameter objects to pass to {@code String.format}. Optional, but may not be null
	 * @return An {@code ActionResult} object with the indicated message, and {@code isOk()} returning {@code false}
	 */
	@Nonnull
	public static ActionResult error(@Nonnull String message, Object... params) {
		if (message == null || message.trim().isEmpty()) {
			throw new IllegalArgumentException("Empty error message");
		}

		if (params.length == 0) {
			return new ActionResult(false, message);
		} else {
			return new ActionResult(false, String.format(message, params));
		}
	}

	@Override
	public String toString() {
		if (ok) {
			return "Result: OK";
		}

		if (message != null) {
			return "Result: FAIL, ".concat(message);
		} else {
			return "Result: FAIL, NO REASON GIVEN";
		}
	}
}