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
package com.jeroensteenbeeke.andalite.analyzer;

import java.io.Serializable;

import com.google.common.base.Function;
import com.jeroensteenbeeke.andalite.Location;

public abstract class AnalyzedType extends Locatable {

	public AnalyzedType(Location location) {
		super(location);
	}

	public abstract String toJavaString();

	@Override
	public final void output(IOutputCallback callback) {
		callback.write(toJavaString());
	}

	public static Function<AnalyzedType, String> toJavaStringFunction() {
		return TO_JAVASTRING_FUNCTION;
	}

	private static final Function<AnalyzedType, String> TO_JAVASTRING_FUNCTION = new ToJavaStringFunction();

	private static final class ToJavaStringFunction implements
			Function<AnalyzedType, String>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String apply(AnalyzedType input) {
			return input.toJavaString();
		}
	}
}
