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

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

/**
 * Representation of an annotation
 * 
 * @author Jeroen Steenbeeke
 *
 */
public final class AnalyzedAnnotation extends Locatable {
	private final String type;

	private final Map<String, BaseValue<?>> annotationValues;

	private Location parametersLocation;

	private boolean hasParentheses = true;

	/**
	 * Creates a new annotation
	 * 
	 * @param location
	 *            The location of the annotation
	 * @param type
	 *            The type of the annotation
	 */
	AnalyzedAnnotation(@Nonnull Location location, @Nonnull String type) {
		super(location);
		this.type = type;
		this.annotationValues = Maps.newLinkedHashMap();
	}

	/**
	 * Get the location of the parameters
	 * 
	 * @return The location of the parameters (if any), or {@code null}
	 *         otherwise
	 */
	@CheckForNull
	public Location getParametersLocation() {
		return parametersLocation;
	}

	/**
	 * Set the location of the annotation's parameters
	 * 
	 * @param parametersLocation
	 *            The location of the parameters
	 */
	void setParametersLocation(@Nonnull Location parametersLocation) {
		this.parametersLocation = parametersLocation;
	}

	/**
	 * Get the type of the annotation
	 * 
	 * @return The annotation's type, without package declaration, and without
	 *         leading {@literal @}
	 */
	@Nonnull
	public String getType() {
		return type;
	}

	/**
	 * Adds a parameter to the given annotation
	 * 
	 * @param value
	 *            The parameter to add
	 */
	void addAnnotation(@Nonnull BaseValue<?> value) {
		annotationValues.put(value.getName(), value);
	}

	/**
	 * Checks if a this annotation has a parameter of the given type with the
	 * given name
	 * 
	 * @param expectedType
	 *            The type of parameter (a subclass of {@code BaseValue})
	 * @param name
	 *            The name of the parameter
	 * @return {@code true} if the annotation has the parameter. {@code false}
	 *         otherwise
	 * @see com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue
	 */
	public <T extends BaseValue<?>> boolean hasValueOfType(
			@Nonnull Class<T> expectedType, @Nonnull String name) {
		if (annotationValues.containsKey(name)) {
			BaseValue<?> value = annotationValues.get(name);
			if (expectedType.isAssignableFrom(value.getClass())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param expectedType
	 *            The type of parameter (a subclass of {@code BaseValue})
	 * @param name
	 *            The name of the parameter
	 * @return The parameter matching the given type and name, if it exists, or
	 *         {@code null} otherwise
	 * @see com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue
	 */
	@SuppressWarnings("unchecked")
	@CheckForNull
	public <T extends BaseValue<?>> T getValue(@Nonnull Class<T> expectedType,
			@Nonnull String name) {
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

	/**
	 * Checks if there are any values/parameters defined for this annotation
	 * 
	 * @return {@code true} if there are parameters to this annotation,
	 *         {@code false} otherwise
	 */
	public boolean hasValues() {
		return !annotationValues.isEmpty();
	}

	/**
	 * Checks if the annotation has parentheses
	 * 
	 * @return {@code true} if the annotation has parentheses, {@code false}
	 *         otherwise
	 */
	public boolean hasParentheses() {
		return hasParentheses;
	}

	/**
	 * Sets whether or not this annotation has parentheses
	 * 
	 * @param hasParentheses
	 *            {@code true} if the annotation has parentheses, {@code false}
	 *            otherwise
	 */
	void setHasParentheses(boolean hasParentheses) {
		this.hasParentheses = hasParentheses;
	}

	/**
	 * Determines if there is an annotation value with the given name
	 * 
	 * @param name
	 *            The name of the value
	 * @return {@code true} if there is a value with the given name,
	 *         {@code false} otherwise
	 */
	public boolean hasValueNamed(@Nonnull final String name) {
		return annotationValues.containsKey(name);
	}

	/**
	 * Returns the type of the annotation value with the given name
	 * 
	 * @param name
	 *            The name of the annotation value
	 * @return The type of the value, or {@code null} if there is no value with
	 *         the given name
	 */
	@CheckForNull
	public String getValueType(@Nonnull final String name) {
		if (hasValueNamed(name)) {
			return annotationValues.get(name).getClass().getName();
		}

		return null;
	}

	/**
	 * Renders this annotation as a Java String
	 * 
	 * @return A String representation of this annotation
	 */
	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append("@");
		java.append(type);
		if (!annotationValues.isEmpty()) {
			java.append("(");
			java.append(annotationValues.entrySet().stream()
					.map(ENTRY_TO_JAVASTRING_FUNCTION)
					.collect(Collectors.joining(", ")));
			java.append(")");
		}

		return java.toString();
	}

	public static final Function<Entry<String, BaseValue<?>>, String> ENTRY_TO_JAVASTRING_FUNCTION = input -> String
			.format("%s=%s", input.getKey(), input.getValue().toJavaString());;

	/**
	 * Returns the raw value with the given name
	 * 
	 * @param name
	 *            The name of the value to find
	 * @return The value, if it is present
	 * @throws NullPointerException
	 *             If there is no value with the given name
	 */
	public Object getValueRaw(@Nonnull String name) {
		return this.annotationValues.get(name).getValue();
	}
}
