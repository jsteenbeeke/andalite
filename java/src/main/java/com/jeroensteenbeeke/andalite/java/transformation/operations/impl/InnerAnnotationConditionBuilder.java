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

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.BooleanArrayValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.BooleanValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.CharValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.DoubleArrayValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.DoubleValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.InnerAnnotationCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.IntegerArrayValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.IntegerValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.LongArrayValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.LongValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.MatchesAllCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.NoValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.StringArrayValueCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.StringValueCondition;

public abstract class InnerAnnotationConditionBuilder<T extends InnerAnnotationConditionBuilder<T, R>, R> {
	private final ImmutableList.Builder<InnerAnnotationCondition> conditions;

	protected InnerAnnotationConditionBuilder() {
		this.conditions = ImmutableList.builder();
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@Nullable final Boolean value) {
		conditions.add(new BooleanValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@Nullable final Character value) {
		conditions.add(new CharValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@Nullable final Long value) {
		conditions.add(new LongValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@Nullable final Double value) {
		conditions.add(new DoubleValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@Nullable final Integer value) {
		conditions.add(new IntegerValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@Nullable final String value) {
		conditions.add(new StringValueCondition(null, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@NotNull final String name, @Nullable final Boolean value) {
		conditions.add(new BooleanValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@NotNull final String name, @Nullable final Long value) {
		conditions.add(new LongValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@NotNull final String name, @Nullable final Double value) {
		conditions.add(new DoubleValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@NotNull final String name, @Nullable final Integer value) {
		conditions.add(new IntegerValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T value(@NotNull final String name, @Nullable final String value) {
		conditions.add(new StringValueCondition(name, value));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public <O> T noValueOrEquals(@NotNull final String name,
			@NotNull final O expectedRawValue) {
		conditions.add(new NoValueCondition<O>(name, expectedRawValue));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final String name,
			@NotNull final Boolean... values) {
		conditions.add(new BooleanArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final String name,
			@NotNull final Double... values) {
		conditions.add(new DoubleArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final String name,
			@NotNull final Integer... values) {
		conditions.add(new IntegerArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final String name,
			@NotNull final Long... values) {
		conditions.add(new LongArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final String name,
			@NotNull final String... values) {
		conditions.add(new StringArrayValueCondition(name, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final Boolean... values) {
		conditions.add(new BooleanArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final Double... values) {
		conditions.add(new DoubleArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final Integer... values) {
		conditions.add(new IntegerArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T arrayValue(@NotNull final Long... values) {
		conditions.add(new LongArrayValueCondition(null, values));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T defaultArrayValue(@NotNull final String... values) {
		conditions.add(new StringArrayValueCondition(null, values));
		return (T) this;
	}

	public R then() {
		return get();
	}

	@NotNull
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
