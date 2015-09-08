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

public class ActionResult {
	private final boolean ok;

	private final String message;

	protected ActionResult(boolean ok, String message) {
		this.ok = ok;
		this.message = message;
	}

	public final boolean isOk() {
		return ok;
	}

	public final String getMessage() {
		return message;
	}

	public static ActionResult ok() {
		return new ActionResult(true, null);
	}

	public static ActionResult error(String message, Object... params) {
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