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

public final class BooleanValue extends BaseValue<Boolean,BooleanValue, BooleanValue.BooleanValueInsertionPoint> {

	public BooleanValue(@NotNull Location location, @Nullable String name,
			boolean value) {
		super(location, name, value);
	}

	@Override
	public void output(IOutputCallback callback) {
		Boolean value = getValue();
		callback.write(Boolean.toString(value));
	}

	@Override
	public String toJavaString() {
		return Boolean.toString(getValue());
	}

	@Override
	public BooleanValue.BooleanValueInsertionPoint getBeforeInsertionPoint() {
		return BooleanValue.BooleanValueInsertionPoint.BEFORE;
	}

	@Override
	public BooleanValue.BooleanValueInsertionPoint getAfterInsertionPoint() {
		return BooleanValue.BooleanValueInsertionPoint.AFTER;
	}

	public enum BooleanValueInsertionPoint implements IInsertionPoint<BooleanValue> {
		BEFORE {
			@Override
			public int position(BooleanValue container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(BooleanValue container) {
				return container.getLocation().getEnd();
			}
		};
	}
}
