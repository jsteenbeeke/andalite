package com.jeroensteenbeeke.andalite;

public final class CodePoint implements Comparable<CodePoint> {
	private final int line;

	private final int column;

	public CodePoint(int line, int column) {
		super();
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + line;
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
		CodePoint other = (CodePoint) obj;
		if (column != other.column)
			return false;
		if (line != other.line)
			return false;
		return true;
	}

	@Override
	public int compareTo(CodePoint o) {
		if (o.line == line) {
			return Integer.compare(column, o.column);
		}

		return Integer.compare(line, o.line);
	}
}
