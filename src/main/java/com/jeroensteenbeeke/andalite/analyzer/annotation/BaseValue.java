package com.jeroensteenbeeke.andalite.analyzer.annotation;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.Outputable;

public abstract class BaseValue<T> implements Outputable {
	private final String name;

	private final T value;

	public BaseValue(@Nullable String name, @Nonnull T value) {
		super();
		this.name = name;
		this.value = value;
	}

	@CheckForNull
	public String getName() {
		return name;
	}

	@Nonnull
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

}
