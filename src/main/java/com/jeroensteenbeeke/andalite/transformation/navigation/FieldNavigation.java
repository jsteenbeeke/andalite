package com.jeroensteenbeeke.andalite.transformation.navigation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedField;

public class FieldNavigation extends
		ChainedNavigation<AnalyzedClass, AnalyzedField> {
	private final String fieldName;

	public FieldNavigation(@Nonnull Navigation<AnalyzedClass> chained,
			@Nonnull String fieldName) {
		super(chained);
		this.fieldName = fieldName;
	}

	@Override
	@Nonnull
	public AnalyzedField navigate(@Nonnull AnalyzedClass chainedTarget)
			throws NavigationException {
		AnalyzedField field = chainedTarget.getField(fieldName);

		if (field != null) {
			return field;
		}

		throw new NavigationException(String.format(
				"Class %s does not have field %s",
				chainedTarget.getClassName(), fieldName));
	}

	@Nonnull
	@Override
	public String getDescription() {
		return String.format("Field named %s", fieldName);
	}
}
