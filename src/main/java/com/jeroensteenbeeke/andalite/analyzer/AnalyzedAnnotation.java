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

	public AnalyzedAnnotation(@Nonnull Location location, @Nonnull String type) {
		super(location);
		this.type = type;
		this.annotationValues = Maps.newHashMap();
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
}
