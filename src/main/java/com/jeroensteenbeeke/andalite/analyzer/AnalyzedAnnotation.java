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

package com.jeroensteenbeeke.andalite.analyzer;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;

public final class AnalyzedAnnotation extends Locatable {
	private final String type;

	private final Map<String, BaseValue<?>> annotationValues;

	private Location parametersLocation;

	private boolean hasParentheses = true;

	public AnalyzedAnnotation(@Nonnull Location location, @Nonnull String type) {
		super(location);
		this.type = type;
		this.annotationValues = Maps.newLinkedHashMap();
	}

	@CheckForNull
	public Location getParametersLocation() {
		return parametersLocation;
	}

	void setParametersLocation(@Nonnull Location parametersLocation) {
		this.parametersLocation = parametersLocation;
	}

	@Nonnull
	public String getType() {
		return type;
	}

	void addAnnotation(BaseValue<?> value) {
		annotationValues.put(value.getName(), value);
	}

	public <T extends BaseValue<?>> boolean hasValueOfType(
			Class<T> expectedType, String name) {
		if (annotationValues.containsKey(name)) {
			BaseValue<?> value = annotationValues.get(name);
			if (expectedType.isAssignableFrom(value.getClass())) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@CheckForNull
	public <T extends BaseValue<?>> T getValue(Class<T> expectedType,
			String name) {
		if (hasValueOfType(expectedType, name)) {
			return (T) annotationValues.get(name);

		}

		return null;
	}

	@Override
	public void output(IOutputCallback callback) {
		callback.write("@");
		callback.write(type);
		if (!annotationValues.isEmpty()) {
			callback.write("(");
			callback.increaseIndentationLevel();
			callback.newline();

			int i = 0;
			for (Entry<String, BaseValue<?>> entry : annotationValues
					.entrySet()) {
				if (i++ > 0) {
					callback.write(",");
					callback.newline();
				}
				callback.write(entry.getKey());
				callback.write("=");
				entry.getValue().output(callback);
			}

			callback.decreaseIndentationLevel();
			callback.newline();
			callback.write(")");
		}

		callback.write(" ");

	}

	public boolean hasValues() {
		return !annotationValues.isEmpty();
	}

	public boolean hasParentheses() {
		return hasParentheses;
	}

	void setHasParentheses(boolean hasParentheses) {
		this.hasParentheses = hasParentheses;
	}

	public boolean hasValueNamed(@Nonnull final String name) {
		return annotationValues.containsKey(name);
	}

	@CheckForNull
	public String getValueType(@Nonnull final String name) {
		if (hasValueNamed(name)) {
			return annotationValues.get(name).getClass().getName();
		}

		return null;
	}

	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append("@");
		java.append(type);
		if (!annotationValues.isEmpty()) {
			java.append("(");
			Joiner.on(", ").appendTo(
					java,
					FluentIterable.from(annotationValues.entrySet()).transform(
							ENTRY_TO_JAVASTRING_FUNCTION));
			java.append(")");
		}

		return java.toString();
	}

	public static Function<AnalyzedAnnotation, String> toJavaStringFunction() {
		return TO_JAVASTRING_FUNCTION;
	}

	private static final Function<AnalyzedAnnotation, String> TO_JAVASTRING_FUNCTION = new ToJavaStringFunction();

	private static final Function<Entry<String, BaseValue<?>>, String> ENTRY_TO_JAVASTRING_FUNCTION = new EntryToString();

	private static final class ToJavaStringFunction implements
			Function<AnalyzedAnnotation, String>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String apply(AnalyzedAnnotation input) {
			return input.toJavaString();
		}
	}

	private static final class EntryToString implements
			Function<Entry<String, BaseValue<?>>, String>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String apply(Entry<String, BaseValue<?>> input) {
			return String.format("%s=%s", input.getKey(), input.getValue()
					.toJavaString());
		}
	}
}
