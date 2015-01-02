package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import java.util.List;

import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;

public class MatchesAllCondition implements InnerAnnotationCondition {
	private final List<InnerAnnotationCondition> conditions;

	public MatchesAllCondition(List<InnerAnnotationCondition> conditions) {
		super();
		this.conditions = conditions;
	}

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		for (InnerAnnotationCondition condition : conditions) {
			if (!condition.isSatisfiedBy(value)) {
				return false;
			}
		}

		return true;
	}

}
