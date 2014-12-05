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

package com.jeroensteenbeeke.andalite.analyzer.annotation;

import java.util.List;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.OutputCallback;

public final class ArrayValue extends BaseValue<List<BaseValue<?>>> {

	public ArrayValue(@Nonnull String name, @Nonnull List<BaseValue<?>> value) {
		super(name, value);
	}

	@Override
	public void output(OutputCallback callback) {
		callback.write("{ ");

		int i = 0;
		for (BaseValue<?> baseValue : getValue()) {
			if (i++ > 0) {
				callback.write(", ");
			}

			baseValue.output(callback);
		}

		callback.write(" }");
	}
}
