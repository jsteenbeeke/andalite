package com.jeroensteenbeeke.andalite.java.analyzer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.IReplaceable;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class LocatedName<T extends Node & NodeWithIdentifier<T>> implements IReplaceable {
	private final String name;

	private final Location location;

	private final T raw;

	public LocatedName(@NotNull T name, @NotNull Location location) {
		this.name = name.getIdentifier();
		this.location = location;
		this.raw = name;
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public Location getLocation() {
		return location;
	}

	@Override
	public void output(IOutputCallback callback) {
		callback.write(raw.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LocatedName that = (LocatedName) o;
		return name.equals(that.name) &&
				location.equals(that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, location);
	}

	public T getRaw() {
		return raw;
	}
}
