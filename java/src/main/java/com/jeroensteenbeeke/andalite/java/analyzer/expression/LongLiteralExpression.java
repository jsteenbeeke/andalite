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
package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.core.Location;

public class LongLiteralExpression extends LiteralExpression<Long> {

	public LongLiteralExpression(@NotNull final Location location,
			@NotNull final String value) {
		super(location, toLong(value));
	}

	private static Long toLong(String value) {
		StringBuilder parseable = new StringBuilder();
		for (char c : value.toCharArray()) {
			if (!Character.isLetter(c)) {
				parseable.append(c);
			}
		}

		return Long.parseLong(parseable.toString());
	}

	@Override
	public String toJavaString() {
		return Long.toString(getValue()).concat("L");
	}
}
