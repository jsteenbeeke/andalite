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

package com.jeroensteenbeeke.andalite.java.analyzer;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.core.*;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

import javax.annotation.CheckForNull;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AnalyzedAnnotation extends Locatable implements IInsertionPointProvider<AnalyzedAnnotation, AnalyzedAnnotation.AnnotationInsertionPoint> {
	private final String type;

	private final Map<String, BaseValue<?, ?, ?>> annotationValues;

	private Location parametersLocation;

	private boolean hasParentheses = true;

	public AnalyzedAnnotation(@NotNull Location location, @NotNull String type) {
		super(location);
		this.type = type;
		this.annotationValues = Maps.newLinkedHashMap();
	}

	public List<BaseValue<?, ?, ?>> getValues() {
		return ImmutableList.copyOf(annotationValues.values());
	}

	@NotNull
	public Optional<Location> getParametersLocation() {
		return Optional.ofNullable(parametersLocation);
	}

	void setParametersLocation(@Nullable Location parametersLocation) {
		this.parametersLocation = parametersLocation;
	}

	@NotNull
	public String getType() {
		return type;
	}

	void addAnnotation(BaseValue<?, ?, ?> value) {
		annotationValues.put(value.getName(), value);
	}

	public <T extends BaseValue<?, ?, ?>> boolean hasValueOfType(
		Class<T> expectedType, String name) {
		if (annotationValues.containsKey(name)) {
			BaseValue<?, ?, ?> value = annotationValues.get(name);
			if (expectedType.isAssignableFrom(value.getClass())) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@CheckForNull
	public <T extends BaseValue<?, ?, ?>> T getValue(Class<T> expectedType,
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
			for (Entry<String, BaseValue<?, ?, ?>> entry : annotationValues
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

	@NotNull
	@Override
	public Transformation insertAt(@NotNull AnnotationInsertionPoint insertionPoint, @NotNull String code) {
		if (!hasParentheses()) {
			return IInsertionPointProvider.super.insertAt(insertionPoint, String.format("(%s)", code));
		}

		return IInsertionPointProvider.super.insertAt(insertionPoint, code);
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

	public boolean hasValueNamed(@NotNull final String name) {
		return annotationValues.containsKey(name);
	}

	@CheckForNull
	public String getValueType(@NotNull final String name) {
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
				annotationValues
					.entrySet()
					.stream()
					.map(e -> String.format("%s=%s", e
						.getKey(), e
												.getValue()
												.toJavaString()))
					.collect(Collectors.toList()));
			java.append(")");
		}

		return java.toString();
	}

	public Object getValueRaw(String name) {
		return this.annotationValues.get(name).getValue();
	}

	public enum AnnotationInsertionPoint implements IInsertionPoint<AnalyzedAnnotation> {
		BEFORE {
			@Override
			public int position(AnalyzedAnnotation container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(AnalyzedAnnotation container) {
				return container.getLocation().getEnd() + 1;
			}
		}, AFTER_LAST_ARGUMENT {
			@Override
			public int position(AnalyzedAnnotation container) {
				return container.getValues().stream()
								.map(BaseValue::getLocation)
								.map(Location::getEnd)
								.map(e -> e + 1)
								.max(Integer::compareTo)
								.or(() -> container.getParametersLocation()
												   .map(Location::getEnd))
								.orElseGet(() -> AFTER.position(container));
			}
		};
	}
}
