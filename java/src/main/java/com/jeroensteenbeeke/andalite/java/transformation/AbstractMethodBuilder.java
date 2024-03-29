/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.FluentReturnType;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMethodBuilder<T, B extends AbstractMethodBuilder<T, B>>
	extends AbstractParameterizedBuilder<T, B> {
	private MethodReturnType type;

	public AbstractMethodBuilder(
		@NotNull MethodReturnType defaultType,
		@NotNull AccessModifier defaultAccess) {
		super(defaultAccess);
		this.type = defaultType;
	}

	@SuppressWarnings("unchecked")
	public B withReturnType(@NotNull MethodReturnType returnType) {
		this.type = returnType;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B withFluentReturnType() {
		this.type = FluentReturnType.FLUENT;
		return (B) this;
	}

	@NotNull
	public MethodReturnType getType() {
		return type;
	}

	@NotNull
	public abstract T named(@NotNull String name);
}
