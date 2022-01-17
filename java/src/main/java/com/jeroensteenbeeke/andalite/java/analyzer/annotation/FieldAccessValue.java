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
package com.jeroensteenbeeke.andalite.java.analyzer.annotation;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

public class FieldAccessValue extends BaseValue<String,FieldAccessValue, FieldAccessValue.FieldAccessValueInsertionPoint> {
	public FieldAccessValue(@NotNull Location location, @Nullable String name,
			@Nullable String scope, @NotNull String value) {
		super(location, name, createValue(scope, value));
	}

	@Override
	public void output(IOutputCallback callback) {
		String value = getValue();

		if (value != null) {
			callback.write(value);
		} else {
			callback.write(null);
		}
	}

	@Override
	public String toJavaString() {
		String value = getValue();

		return value != null ? value : "null";
	}

	private static String createValue(@Nullable String scope,
			@NotNull String value) {
		if (scope != null) {
			return String.format("%s.%s", scope, value);
		}

		return value;
	}

	@Override
	public FieldAccessValue.FieldAccessValueInsertionPoint getBeforeInsertionPoint() {
		return FieldAccessValue.FieldAccessValueInsertionPoint.BEFORE;
	}

	@Override
	public FieldAccessValue.FieldAccessValueInsertionPoint getAfterInsertionPoint() {
		return FieldAccessValue.FieldAccessValueInsertionPoint.AFTER;
	}

	public enum FieldAccessValueInsertionPoint implements IInsertionPoint<FieldAccessValue> {
		BEFORE {
			@Override
			public int position(FieldAccessValue container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(FieldAccessValue container) {
				return container.getLocation().getEnd();
			}
		};
	}
}
