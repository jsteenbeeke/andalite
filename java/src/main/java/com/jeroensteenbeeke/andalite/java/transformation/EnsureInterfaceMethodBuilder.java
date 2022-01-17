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

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.InterfaceMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IInterfaceOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureInterfaceMethod;

public class EnsureInterfaceMethodBuilder extends
	AbstractMethodBuilder<IInterfaceOperation, EnsureInterfaceMethodBuilder> {
	private static final Logger log = LoggerFactory
		.getLogger(EnsureInterfaceMethodBuilder.class);

	private final Consumer<IInterfaceOperation> onCreate;

	private final InterfaceMethodType interfaceMethodType;

	EnsureInterfaceMethodBuilder(@NotNull InterfaceMethodType interfaceMethodType,
								 @NotNull Consumer<IInterfaceOperation> onCreate) {
		super("void", AccessModifier.PUBLIC);
		this.interfaceMethodType = interfaceMethodType;
		this.onCreate = onCreate;
	}

	@Override
	public EnsureInterfaceMethodBuilder withModifier(AccessModifier modifier) {
		if (modifier != AccessModifier.PUBLIC && modifier != AccessModifier.PRIVATE) {
			log.warn("Access modifier {} ignored on interface",
					 modifier.name());
			return this;
		}

		return super.withModifier(modifier);
	}

	@Override
	public IInterfaceOperation named(String name) {
		EnsureInterfaceMethod ensureInterfaceMethod = new EnsureInterfaceMethod(
			name, getType(), interfaceMethodType, getDescriptors());
		onCreate.accept(ensureInterfaceMethod);
		return ensureInterfaceMethod;
	}
}
