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

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;

public final class AnalyzedAnnotation extends Locatable {
	private final String type;

	private final Map<String, BaseValue<?>> annotationValues;

	private Location parametersLocation;

	public AnalyzedAnnotation(@Nonnull Location location, @Nonnull String type) {
		super(location);
		this.type = type;
		this.annotationValues = Maps.newHashMap();
	}

	@CheckForNull
	public Location getParametersLocation() {
		return parametersLocation;
	}

	public void setParametersLocation(@Nonnull Location parametersLocation) {
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
	public void output(OutputCallback callback) {
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
}
