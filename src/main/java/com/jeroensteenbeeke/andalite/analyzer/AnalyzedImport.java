package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedImport extends Locatable {
	private static final String ASTERISK = "*";

	private final String statement;

	private final boolean staticImport;

	private final boolean asterisk;

	public AnalyzedImport(@Nonnull Location location,
			@Nonnull String statement, boolean staticImport, boolean asterisk) {
		super(location);
		this.statement = statement;
		this.staticImport = staticImport;
		this.asterisk = asterisk;
	}

	public boolean importsMethodStatically(@Nonnull String fqdn) {
		ClassDefinition def = ClassDefinition.fromFQDN(fqdn);

		if (ASTERISK.equals(def.getClassName())) {
			return false;
		}

		if (!staticImport) {
			// This statement covers a whole class, not static methods
			return false;
		}

		final String methodName = def.getClassName();
		final String qualifiedClass = def.getPackageName();

		if (asterisk) {
			return qualifiedClass.equals(statement);
		} else {
			final int methodIndex = statement.lastIndexOf('.');
			if (methodIndex == -1) {
				return false;
			}

			final String classPart = statement.substring(0, methodIndex);
			final String methodPart = statement.substring(methodIndex + 1);

			return classPart.equals(qualifiedClass)
					&& methodPart.equals(methodName);
		}

	}

	public boolean matchesClass(@Nonnull String fqdn) {
		ClassDefinition def = ClassDefinition.fromFQDN(fqdn);

		if (ASTERISK.equals(def.getClassName())) {
			return false;
		}

		if (staticImport) {
			// This statement does not cover this class as a whole, but only
			// one or all static method(s)
			return false;
		}

		if (asterisk) {
			return statement.equals(def.getPackageName());
		} else {
			return statement.equals(def.getFullName());
		}

	}

	@Nonnull
	public String getStatement() {
		return statement;
	}

	public boolean isStaticImport() {
		return staticImport;
	}

	public boolean isAsterisk() {
		return asterisk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (asterisk ? 1231 : 1237);
		result = prime * result
				+ ((statement == null) ? 0 : statement.hashCode());
		result = prime * result + (staticImport ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalyzedImport other = (AnalyzedImport) obj;
		if (asterisk != other.asterisk)
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		if (staticImport != other.staticImport)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("import ");
		if (staticImport) {
			sb.append("static ");
		}
		sb.append(statement);
		if (asterisk) {
			sb.append(".*");
		}
		sb.append(";");
		return sb.toString();
	}

	@Override
	public void output(OutputCallback callback) {
		callback.write(toString());
		callback.newline();
	}
}
