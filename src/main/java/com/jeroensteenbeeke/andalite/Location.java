package com.jeroensteenbeeke.andalite;

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.api.Node;

public final class Location {
	private final CodePoint start;

	private final CodePoint end;

	public Location(@Nonnull CodePoint start, @Nonnull CodePoint end) {
		super();
		this.start = start;
		this.end = end;
	}

	@Nonnull
	public CodePoint getStart() {
		return start;
	}

	@Nonnull
	public CodePoint getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		Location other = (Location) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Nonnull
	public static Location from(@Nonnull Node node) {
		return new Location(new CodePoint(node.getBeginLine(),
				node.getBeginColumn()), new CodePoint(node.getEndLine(),
				node.getEndColumn()));
	}
}
