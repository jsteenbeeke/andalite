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

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumConstantOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumConstantMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumMethod;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class EnsureEnumConstantMethodBuilder
		extends AbstractMethodBuilder<IEnumConstantOperation, EnsureEnumConstantMethodBuilder> {
	private final Consumer<IEnumConstantOperation> onCreate;

	EnsureEnumConstantMethodBuilder(Consumer<IEnumConstantOperation> onCreate) {
		super("void", AccessModifier.PUBLIC);
		this.onCreate = onCreate;
	}

	@Override
	public IEnumConstantOperation named(@NotNull String name) {
		EnsureEnumConstantMethod ensureEnumMethod = new EnsureEnumConstantMethod(name,
																		 getType(), getModifier(), getDescriptors());
		onCreate.accept(ensureEnumMethod);
		return ensureEnumMethod;
	}
}
