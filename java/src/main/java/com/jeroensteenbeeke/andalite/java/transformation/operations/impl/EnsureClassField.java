package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class EnsureClassField extends EnsureField<AnalyzedClass, AnalyzedClass.ClassInsertionPoint, EnsureClassField> implements
	IClassOperation {
	public EnsureClassField(@NotNull String name, @NotNull String type,
		@NotNull AccessModifier modifier) {
		super(name, type, modifier);
	}

	@Override
	protected AnalyzedClass.ClassInsertionPoint getLastFieldLocation() {
		return AnalyzedClass.ClassInsertionPoint.AFTER_LAST_FIELD;
	}

	@Override
	public Optional<Location> getBodyLocation(AnalyzedClass input) {
		return input.getBodyLocation();
	}

	@Override
	protected AnalyzedField getField(AnalyzedClass input, String name) {
		return input.getField(name);
	}
}
