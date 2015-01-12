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
package com.jeroensteenbeeke.andalite.analyzer.types;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class Wildcard extends AnalyzedType {
	private final Reference superReference;

	private final Reference extendsReference;

	public Wildcard(@Nonnull Location location,
			@Nullable Reference superReference,
			@Nullable Reference extendsReference) {
		super(location);
		this.superReference = superReference;
		this.extendsReference = extendsReference;
	}

	@CheckForNull
	public Reference getExtendsReference() {
		return extendsReference;
	}

	@CheckForNull
	public Reference getSuperReference() {
		return superReference;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		sb.append("?");
		if (superReference != null) {
			sb.append(" super ");
			sb.append(superReference.toJavaString());
		}
		if (extendsReference != null) {
			sb.append(" extends ");
			sb.append(extendsReference.toJavaString());
		}

		return sb.toString();
	}
}
