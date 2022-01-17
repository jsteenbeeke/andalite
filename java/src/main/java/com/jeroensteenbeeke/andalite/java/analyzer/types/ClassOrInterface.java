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

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.analyzer.LocatedName;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ClassOrInterface extends AnalyzedType {
	private final String name;

	private final Location nameLocation;

	private final ClassOrInterface scope;

	private final List<AnalyzedType> typeArguments;

	public ClassOrInterface(@NotNull final Location location,
			@NotNull final LocatedName<?> name,
			@Nullable final ClassOrInterface scope,
			@NotNull final List<AnalyzedType> typeArguments) {
		super(location);
		this.name = name.getName();
		this.nameLocation = name.getLocation();
		this.scope = scope;
		this.typeArguments = ImmutableList.copyOf(typeArguments);
	}

	public String getName() {
		return name;
	}

	public Location getNameLocation() {
		return nameLocation;
	}

	public Optional<ClassOrInterface> getScope() {
		return Optional.ofNullable(scope);
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
