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

import com.jeroensteenbeeke.andalite.java.transformation.returntypes.VoidReturnType;
import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureClassMethod;

public class EnsureClassMethodBuilder extends
		AbstractMethodBuilder<IClassOperation, EnsureClassMethodBuilder> {
	private final Consumer<IClassOperation> onCreate;

	EnsureClassMethodBuilder(@NotNull Consumer<IClassOperation> onCreate) {
		super(VoidReturnType.VOID, AccessModifier.PUBLIC);
		this.onCreate = onCreate;
	}

	@Override
	@NotNull
	public EnsureClassMethod named(@NotNull String name) {
		EnsureClassMethod classMethod = new EnsureClassMethod(name, getType(),
				getModifier(), getDescriptors());
		onCreate.accept(classMethod);
		return classMethod;
	}
}
