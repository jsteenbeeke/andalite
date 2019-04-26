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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

public final class DoubleValue extends BaseValue<Double,DoubleValue, DoubleValue.DoubleValueInsertionPoint> {

	public DoubleValue(@Nonnull Location location, @Nullable String name,
			@Nonnull Double value) {
		super(location, name, value);
	}

	@Override
	public void output(IOutputCallback callback) {
		Double value = getValue();
		callback.write(Double.toString(value));

	}

	@Override
	public String toJavaString() {
		Double value = getValue();
		return Double.toString(value);
	}

	@Override
	public DoubleValue.DoubleValueInsertionPoint getBeforeInsertionPoint() {
		return DoubleValue.DoubleValueInsertionPoint.BEFORE;
	}

	@Override
	public DoubleValue.DoubleValueInsertionPoint getAfterInsertionPoint() {
		return DoubleValue.DoubleValueInsertionPoint.AFTER;
	}

	public enum DoubleValueInsertionPoint implements IInsertionPoint<DoubleValue> {
		BEFORE {
			@Override
			public int position(DoubleValue container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(DoubleValue container) {
				return container.getLocation().getEnd();
			}
		};
	}
}
