package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class EnsureEnumField extends EnsureField<AnalyzedEnum, AnalyzedEnum.EnumInsertionPoint, EnsureEnumField> implements
	IEnumOperation {
	public EnsureEnumField(@NotNull String name, @NotNull String type,
		@NotNull AccessModifier modifier) {
		super(name, type, modifier);
	}

	@Override
	protected AnalyzedEnum.EnumInsertionPoint getLastFieldLocation() {
		return AnalyzedEnum.EnumInsertionPoint.START_OF_IMPLEMENTATION;
	}

	@Override
	public Optional<Location> getBodyLocation(AnalyzedEnum input) {
		return input.getBodyLocation();
	}

	@Override
	protected AnalyzedField getField(AnalyzedEnum input, String name) {
		return input.getField(name);
	}
}
