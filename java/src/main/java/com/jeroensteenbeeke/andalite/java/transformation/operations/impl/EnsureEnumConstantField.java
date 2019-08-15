package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnumConstant;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumConstantOperation;

import javax.annotation.Nonnull;
import java.util.Optional;

public class EnsureEnumConstantField extends EnsureField<AnalyzedEnumConstant, AnalyzedEnumConstant.EnumConstantInsertionPoint, EnsureEnumConstantField> implements
	IEnumConstantOperation {
	public EnsureEnumConstantField(@Nonnull String name, @Nonnull String type,
		@Nonnull AccessModifier modifier) {
		super(name, type, modifier);
	}

	@Override
	protected AnalyzedEnumConstant.EnumConstantInsertionPoint getLastFieldLocation() {
		return AnalyzedEnumConstant.EnumConstantInsertionPoint.BEFORE_FIRST_MEMBER;
	}

	@Override
	public Optional<Location> getBodyLocation(AnalyzedEnumConstant input) {
		return input.getBodyLocation();
	}

	@Override
	protected AnalyzedField getField(AnalyzedEnumConstant input, String name) {
		return input.getField(name);
	}
}
