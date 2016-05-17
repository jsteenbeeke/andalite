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

public class TypedActionResult<T> extends ActionResult {
	private final T object;

	protected TypedActionResult(boolean ok, String message, T object) {
		super(ok, message);
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public static final <T> TypedActionResult<T> fail(ActionResult failure) {
		if (failure.isOk())
			throw new IllegalArgumentException(
					"Cannot turn a successful result into a failure");

		return fail(failure.getMessage());

	}

	public static final <T> TypedActionResult<T> ok(T object) {
		return new TypedActionResult<T>(true, null, object);
	}

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
