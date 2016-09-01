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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Specialized version of ActionResult. In addition to having a boolean that
 * indicates success or failure and an error-message detailing the reason for
 * failure, it takes an object parameter on success
 * 
 * @author Jeroen Steenbeeke
 * @param <T>
 *            The type of return object
 */
public class TypedActionResult<T> extends ActionResult {
	private final T object;

	/**
	 * Creates a new TypedActionResult
	 * 
	 * @param ok
	 *            Whether or not the action was successful
	 * @param message
	 *            The error message, if any
	 * @param object
	 *            The result object, if any
	 */
	protected TypedActionResult(boolean ok, @Nullable String message,
			@Nullable T object) {
		super(ok, message);
		this.object = object;
	}

	/**
	 * Returns the object associated with this result. Can be null
	 * 
	 * @return The given object, or {@code null} if {@code isOk()} returns
	 *         {@code false}
	 */
	public T getObject() {
		return object;
	}

	/**
	 * Creates a new failing {@code TypedActionResult} based on an existing
	 * {@code ActionResult}.
	 * 
	 * @param failure
	 *            The {@code ActionResult} to use as a basis. <b>Please be
	 *            advised that {@code failure.isOk()} should
	 *            return {@code false}</b>
	 * @return A {@code TypedActionResult} with the given failure's error
	 *         message
	 * @throws IllegalArgumentException
	 *             If {@code failure.isOk()} returns {@code true}
	 */
	@Nonnull
	public static final <T> TypedActionResult<T> fail(
			@Nonnull ActionResult failure) {
		if (failure.isOk())
			throw new IllegalArgumentException(
					"Cannot turn a successful result into a failure");

		return fail(failure.getMessage());

	}

	/**
	 * Creates a new successful {@code TypedActionResult}
	 * 
	 * @param object
	 *            The result object
	 * @return A new {@code TypedActionResult} with the given object
	 */
	public static final <T> TypedActionResult<T> ok(@Nonnull T object) {
		return new TypedActionResult<T>(true, null, object);
	}

	/**
	 * Creates a new failing {@code TypedActionResult}
	 * 
	 * @param message
	 *            The reason for failure. Can either be a plain string, or a
	 *            format String as used in {@code String.format}. This may not
	 *            be null or empty
	 * @param params
	 *            Parameter objects to pass to {@code String.format}. Optional,
	 *            but may not be null
	 * @return A {@code TypedActionResult} object with the indicated message,
	 *         with
	 *         {@code isOk()} returning {@code false} and {@code getObject()}
	 *         returning {@code null}
	 * @throws IllegalArgumentException
	 *             When the error message is {@code null} or empty
	 */
	public static final <T> TypedActionResult<T> fail(String message,
			Object... params) {
		if (message == null || message.trim().isEmpty()) {
			throw new IllegalArgumentException("Empty error message");
		}

		if (params.length == 0) {
			return new TypedActionResult<T>(false, message, null);
		}

		return new TypedActionResult<T>(false, String.format(message, params),
				null);
	}

}
