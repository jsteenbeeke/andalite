package com.jeroensteenbeeke.andalite.java.analyzer;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.IReplaceable;
import com.jeroensteenbeeke.andalite.core.Location;

public class GenerifiedName implements IReplaceable {
	private final String name;

	private final Location location;

	public GenerifiedName(String name, Location location) {
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void output(IOutputCallback callback) {
		callback.write(name);
	}

	@Override
	public String toString() {
		return name;
	}
}
