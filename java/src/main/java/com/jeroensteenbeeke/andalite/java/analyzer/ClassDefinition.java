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

public final class ClassDefinition {
	private final String packageName;

	private final String className;

	private ClassDefinition(@NotNull String packageName, @NotNull String className) {
		super();
		this.packageName = packageName;
		this.className = className;
	}

	@NotNull
	public String getFullName() {
		StringBuilder sb = new StringBuilder();
		sb.append(packageName);
		if (!packageName.isEmpty()) {
			sb.append('.');
		}
		sb.append(className);

		return sb.toString();
	}

	@NotNull
	public String getClassName() {
		return className;
	}

	@NotNull
	public String getPackageName() {
		return packageName;
	}

	@NotNull
	public static ClassDefinition fromFQDN(@NotNull String fqdn) {
		if (fqdn.isEmpty()) {
			return new ClassDefinition("", "");
		} else {
			final int dotIndex = fqdn.lastIndexOf('.');

			if (dotIndex == -1) {
				return new ClassDefinition("", fqdn);
			} else {
				final String packageName = fqdn.substring(0, dotIndex);
				final String className = fqdn.substring(dotIndex + 1);

				return new ClassDefinition(packageName, className);
			}
		}
	}
}
