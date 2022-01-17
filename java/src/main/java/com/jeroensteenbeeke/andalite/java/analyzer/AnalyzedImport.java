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

package com.jeroensteenbeeke.andalite.java.analyzer;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

public final class AnalyzedImport extends Locatable {
	private static final String ASTERISK = "*";

	private final String statement;

	private final boolean staticImport;

	private final boolean asterisk;

	public AnalyzedImport(@NotNull Location location,
			@NotNull String statement, boolean staticImport, boolean asterisk) {
		super(location);
		this.statement = statement;
		this.staticImport = staticImport;
		this.asterisk = asterisk;
	}

	public boolean importsMethodStatically(@NotNull String fqdn) {
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

	public boolean matchesClass(@NotNull String fqdn) {
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

	@NotNull
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
	public void output(IOutputCallback callback) {
		callback.write(toString());
		callback.newline();
	}
}
