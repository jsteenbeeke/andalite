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
package com.jeroensteenbeeke.andalite.transformation;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;

public abstract class AbstractMethodBuilder<T> {
	private final Builder<ParameterDescriptor> descriptors;

	private String type;

	private AccessModifier modifier;

	protected AbstractMethodBuilder(String defaultType,
			AccessModifier defaultAccess) {
		this.descriptors = ImmutableList.builder();
		this.type = defaultType;
		this.modifier = defaultAccess;
	}

	public ParameterDescriber<T> withParameter(@Nonnull String name) {
		return new ParameterDescriber<T>(this, name);
	}

	protected List<ParameterDescriptor> getDescriptors() {
		return descriptors.build();
	}

	protected String getType() {
		return type;
	}

	protected AccessModifier getModifier() {
		return modifier;
	}

	public AbstractMethodBuilder<T> withReturnType(@Nonnull String returnType) {
		this.type = returnType;
		return this;
	}

	public AbstractMethodBuilder<T> withModifier(
			@Nonnull AccessModifier modifier) {
		this.modifier = modifier;
		return this;
	}

	@CheckForNull
	public abstract T named(@Nonnull String name);

	public static class ParameterDescriber<T> {
		private final AbstractMethodBuilder<T> builder;

		private final String name;

		private ParameterDescriber(AbstractMethodBuilder<T> builder, String name) {
			this.builder = builder;
			this.name = name;
		}

		public AbstractMethodBuilder<T> ofType(@Nonnull String type) {
			builder.descriptors.add(new ParameterDescriptor(type, name));
			return builder;
		}

	}
}
