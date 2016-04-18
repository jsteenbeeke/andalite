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
package com.jeroensteenbeeke.andalite.java.transformation;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;

public abstract class AbstractMethodBuilder<T, B extends AbstractMethodBuilder<T, B>>
		extends AbstractParameterizedBuilder<T, B> {

	private String type;

	public AbstractMethodBuilder(String defaultType,
			AccessModifier defaultAccess) {
		super(defaultAccess);
		this.type = defaultType;
	}

	@SuppressWarnings("unchecked")
	public B withReturnType(String returnType) {
		this.type = returnType;
		return (B) this;
	}

	public String getType() {
		return type;
	}

	@CheckForNull
	public abstract T named(@Nonnull String name);
}
