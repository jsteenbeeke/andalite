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

public final class IntegerValue extends BaseValue<Integer,IntegerValue, IntegerValue.IntegerValueInsertionPoint> {

	public IntegerValue(@NotNull Location location, @Nullable String name,
			@NotNull Integer value) {
		super(location, name, value);
	}

	@Override
	public void output(IOutputCallback callback) {
		Integer value = getValue();
		callback.write(Integer.toString(value));
	}

	@Override
	public String toJavaString() {
		Integer value = getValue();
		return Integer.toString(value);
	}

	@Override
	public IntegerValueInsertionPoint getBeforeInsertionPoint() {
		return IntegerValueInsertionPoint.BEFORE;
	}

	@Override
	public IntegerValueInsertionPoint getAfterInsertionPoint() {
		return IntegerValueInsertionPoint.AFTER;
	}

	public enum IntegerValueInsertionPoint implements IInsertionPoint<IntegerValue> {
		BEFORE {
			@Override
			public int position(IntegerValue container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(IntegerValue container) {
				return container.getLocation().getEnd();
			}
		};
	}
}
