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

import java.io.Serializable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

public abstract class BaseValue<T> extends Locatable {
	private final String name;

	private final T value;

	public BaseValue(@Nonnull Location location, @Nullable String name,
			@Nullable T value) {
		super(location);
		this.name = name;
		this.value = value;
	}

	@CheckForNull
	public String getName() {
		return name;
	}

	@CheckForNull
	public final T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseValue<?> other = (BaseValue<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public abstract String toJavaString();

	public static Function<BaseValue<?>, String> toJavaStringFunction() {
		return TO_JAVASTRING_FUNCTION;
	}

	private static final Function<BaseValue<?>, String> TO_JAVASTRING_FUNCTION = new ToJavaStringFunction();

	private static final class ToJavaStringFunction implements
			Function<BaseValue<?>, String>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String apply(BaseValue<?> input) {
			return input.toJavaString();
		}
	}

}