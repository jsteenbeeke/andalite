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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.*;

public abstract class InnerAnnotationConditionBuilder<T extends InnerAnnotationConditionBuilder<T, R>, R> {
	private final ImmutableList.Builder<InnerAnnotationCondition> conditions;

	protected InnerAnnotationConditionBuilder() {
		this.conditions = ImmutableList.builder();
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nullable final Boolean value) {
		conditions.add(new BooleanValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nullable final Character value) {
		conditions.add(new CharValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nullable final Long value) {
		conditions.add(new LongValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nullable final Double value) {
		conditions.add(new DoubleValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nullable final Integer value) {
		conditions.add(new IntegerValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nullable final String value) {
		conditions.add(new StringValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nonnull final String name, @Nullable final Boolean value) {
		conditions.add(new BooleanValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nonnull final String name, @Nullable final Long value) {
		conditions.add(new LongValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nonnull final String name, @Nullable final Double value) {
		conditions.add(new DoubleValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nonnull final String name, @Nullable final Integer value) {
		conditions.add(new IntegerValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T value(@Nonnull final String name, @Nullable final String value) {
		conditions.add(new StringValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T noValue(@Nonnull final String name) {
		conditions.add(new NoValueCondition(name));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final String name,
			@Nonnull final Boolean... values) {
		conditions.add(new BooleanArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final String name,
			@Nonnull final Double... values) {
		conditions.add(new DoubleArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final String name,
			@Nonnull final Integer... values) {
		conditions.add(new IntegerArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final String name,
			@Nonnull final Long... values) {
		conditions.add(new LongArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final String name,
			@Nonnull final String... values) {
		conditions.add(new StringArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final Boolean... values) {
		conditions.add(new BooleanArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final Double... values) {
		conditions.add(new DoubleArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final Integer... values) {
		conditions.add(new IntegerArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T arrayValue(@Nonnull final Long... values) {
		conditions.add(new LongArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public T defaultArrayValue(@Nonnull final String... values) {
		conditions.add(new StringArrayValueCondition(null, values));
		return (T) this;
	}

	public R then() {
		return get();
	}

	@Nonnull
	public R get() {
		List<InnerAnnotationCondition> list = conditions.build();

		if (list.size() == 1) {
			return getReturnObject(list.get(0));
		} else {
			return getReturnObject(new MatchesAllCondition(list));
		}
	}

	protected abstract R getReturnObject(InnerAnnotationCondition finalCondition);

}
