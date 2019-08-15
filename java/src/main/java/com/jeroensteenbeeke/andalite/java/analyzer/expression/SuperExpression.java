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
package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.javaparser.ast.expr.Name;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.LocatedName;

import java.util.Optional;

public class SuperExpression extends AnalyzedExpression {

	private final LocatedName<Name> typeName;

	public SuperExpression(@Nonnull Location location,
			@Nullable LocatedName<Name> typeName) {
		super(location);
		this.typeName = typeName;
	}

	@CheckForNull
	public LocatedName<Name> getTypeName() {
		return typeName;
	}

	@Nonnull
	public Optional<LocatedName<Name>> typeName() {
		return Optional.ofNullable(getTypeName());
	}

	@Override
	public String toJavaString() {
		if (typeName != null) {
			return String.format("%s.super", typeName.getName());
		}

		return "super";
	}

}
