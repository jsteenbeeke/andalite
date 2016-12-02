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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of an import statement
 * 
 * @author Jeroen Steenbeeke
 */
public final class AnalyzedImport extends Locatable {
	private static final String ASTERISK = "*";

	private final String statement;

	private final boolean staticImport;

	private final boolean asterisk;

	/**
	 * Create a new AnalyzedImport
	 * 
	 * @param location
	 *            The location of the import
	 * @param statement
	 *            The import statement
	 * @param staticImport
	 *            Whether or not this is a static import
	 * @param asterisk
	 *            Whether or not this is a wildcard import
	 */
	protected AnalyzedImport(@Nonnull Location location,
			@Nonnull String statement, boolean staticImport, boolean asterisk) {
		super(location);
		this.statement = statement;
		this.staticImport = staticImport;
		this.asterisk = asterisk;
	}

	/**
	 * Check if a given method is imported statically
	 * 
	 * @param fqdn
	 *            The fully qualified name of the method. Should not end in an
	 *            asterisk
	 * @return {@code true} if the method is imported statically, {@code false}
	 *         otherwise
	 */
	public boolean importsMethodStatically(@Nonnull String fqdn) {
		ClassDefinition def = ClassDefinition.fromFQDN(fqdn);

		if (ASTERISK.equals(def.getClassName())) {
			// Someone didn't read the Javadoc
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

	/**
	 * Checks if this import matches the given fully qualified domain name
	 * 
	 * @param fqdn
	 *            The fully qualified domain name of the class to check. Should
	 *            not end in an asterisk
	 * @return {@code true} if the class is imported, {@code false}
	 *         otherwise
	 */
	public boolean matchesClass(@Nonnull String fqdn) {
		ClassDefinition def = ClassDefinition.fromFQDN(fqdn);

		if (ASTERISK.equals(def.getClassName())) {
			// Someone didn't read the Javadoc
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

	/**
	 * Get the import statement
	 * 
	 * @return The import statement
	 */
	@Nonnull
	public String getStatement() {
		return statement;
	}

	/**
	 * Check if the import is a static import
	 * 
	 * @return {@code true} if the import is a static import, {@code false}
	 *         otherwise
	 */
	public boolean isStaticImport() {
		return staticImport;
	}

	/**
	 * Check if the import is a wildcard import
	 * 
	 * @return {@code true} if the import is a wildcard import, {@code false}
	 *         otherwise
	 */
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
