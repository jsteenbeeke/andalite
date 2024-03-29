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

import java.util.function.Consumer;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumMethod;

import com.jeroensteenbeeke.andalite.java.transformation.returntypes.VoidReturnType;
import org.jetbrains.annotations.NotNull;

public class EnsureEnumMethodBuilder
		extends AbstractMethodBuilder<IEnumOperation, EnsureEnumMethodBuilder> {
	private final Consumer<IEnumOperation> onCreate;

	EnsureEnumMethodBuilder(Consumer<IEnumOperation> onCreate) {
		super(VoidReturnType.VOID, AccessModifier.PUBLIC);
		this.onCreate = onCreate;
	}

	@Override
	@NotNull
	public IEnumOperation named(@NotNull String name) {
		EnsureEnumMethod ensureEnumMethod = new EnsureEnumMethod(name,
				getType(), getModifier(), getDescriptors());
		onCreate.accept(ensureEnumMethod);
		return ensureEnumMethod;
	}
}
