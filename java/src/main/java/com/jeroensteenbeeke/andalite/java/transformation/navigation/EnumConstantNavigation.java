package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnumConstant;

public class EnumConstantNavigation extends ChainedNavigation<AnalyzedEnum, AnalyzedEnumConstant> {
	private final String constantName;

	public EnumConstantNavigation(IJavaNavigation<AnalyzedEnum> chained, String constantName) {
		super(chained);
		this.constantName = constantName;
	}

	@Override
	public AnalyzedEnumConstant navigate(AnalyzedEnum chainedTarget) throws NavigationException {
		for (AnalyzedEnumConstant constant : chainedTarget.getConstants()) {
			if (constant.getDenominationName().equals(constantName)) {
				return constant;
			}
		}

		throw new NavigationException("Enum " + chainedTarget.getDenominationName() + " has no constant " + constantName);
	}

	@Override
	public String getStepDescription() {
		return String.format(", go to constantName %s", constantName);
	}
}
