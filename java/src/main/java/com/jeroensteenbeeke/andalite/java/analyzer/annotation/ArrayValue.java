/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.java.analyzer.annotation;

import com.google.common.base.Joiner;
import com.jeroensteenbeeke.andalite.core.*;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public final class ArrayValue extends BaseValue<List<BaseValue<?,?,?>>,ArrayValue, ArrayValue.ArrayValueInsertionPoint> {

	public ArrayValue(@NotNull Location location, @Nullable String name,
					  @NotNull List<BaseValue<?,?,?>> value) {
		super(location, name, value);
	}

	@Override
	public void output(IOutputCallback callback) {
		callback.write("{ ");

		int i = 0;
		for (BaseValue<?,?,?> baseValue : getValue()) {
			if (i++ > 0) {
				callback.write(", ");
			}

			baseValue.output(callback);
		}

		callback.write(" }");
	}

	@Override
	public String toJavaString() {
		return String.format(
			"{%s}",
			Joiner.on(", ").join(
				getValue().stream().map(BaseValue::toJavaString).collect(Collectors.toList())));
	}

	@Override
	public ArrayValueInsertionPoint getBeforeInsertionPoint() {
		return ArrayValueInsertionPoint.BEFORE;
	}

	@Override
	public ArrayValueInsertionPoint getAfterInsertionPoint() {
		return ArrayValueInsertionPoint.AFTER;
	}

	@NotNull
	@Override
	public Transformation insertAt(@NotNull ArrayValueInsertionPoint insertionPoint, @NotNull String replacement) {
		if (getValue().isEmpty()) {
			return super.insertAt(insertionPoint, String.format("{%s}", replacement));
		}

		return super.insertAt(insertionPoint, String.format(", %s", replacement));
	}

	public enum ArrayValueInsertionPoint implements IInsertionPoint<ArrayValue> {
		BEFORE {
			@Override
			public int position(ArrayValue container) {
				return container.getLocation().getStart();
			}
		},
		AFTER {
			@Override
			public int position(ArrayValue container) {
				return container.getLocation().getEnd();
			}
		},
		AFTER_LAST_ELEMENT {
			@Override
			public int position(ArrayValue container) {
				return container
					.getValue()
					.stream()
					.map(BaseValue::getLocation)
					.map(Location::getEnd)
					.max(Integer::compareTo)
					.orElseGet(() -> container.getLocation().getEnd());
			}
		}
	}
}
