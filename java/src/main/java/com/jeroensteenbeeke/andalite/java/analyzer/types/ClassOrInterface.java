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
package com.jeroensteenbeeke.andalite.java.analyzer.types;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

public class ClassOrInterface extends AnalyzedType {
	private final String name;

	private final ClassOrInterface scope;

	private final List<AnalyzedType> typeArguments;

	public ClassOrInterface(@Nonnull final Location location,
			@Nonnull final String name, @Nonnull final ClassOrInterface scope,
			@Nonnull final List<AnalyzedType> typeArguments) {
		super(location);
		this.name = name;
		this.scope = scope;
		this.typeArguments = ImmutableList.copyOf(typeArguments);
	}

	public String getName() {
		return name;
	}

	public ClassOrInterface getScope() {
		return scope;
	}

	public List<AnalyzedType> getTypeArguments() {
		return typeArguments;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		if (scope != null) {
			sb.append(scope.toJavaString());
			sb.append(".");
		}

		sb.append(name);

		if (!typeArguments.isEmpty()) {
			sb.append("<");
			for (AnalyzedType analyzedType : typeArguments) {
				sb.append(analyzedType.toJavaString());
			}

			sb.append(">");
		}

		return sb.toString();
	}

}
