package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public abstract class Locatable implements Outputable {
	private final Location location;

	public Locatable(@Nonnull Location location) {
		super();
		this.location = location;
	}

	@Nonnull
	public final Location getLocation() {
		return location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
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
		Locatable other = (Locatable) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

}
