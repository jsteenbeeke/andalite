package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.Annotatable;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;

public abstract class AbstractRemoveAnnotation<T extends Annotatable>
		implements IJavaOperation<T> {
	private final String annotation;

	public AbstractRemoveAnnotation(String annotation) {
		this.annotation = annotation;
	}

	@Override
	public List<Transformation> perform(T input) throws OperationException {
		ImmutableList.Builder<Transformation> transformations = ImmutableList
				.builder();

		for (AnalyzedAnnotation analyzedAnnotation : input.getAnnotations()) {
			if (analyzedAnnotation.getType().equals(annotation)) {
				transformations
						.add(Transformation.replace(analyzedAnnotation, ""));
			}
		}

		return transformations.build();
	}

	@Override
	public ActionResult verify(T input) {
		for (AnalyzedAnnotation analyzedAnnotation : input.getAnnotations()) {
			if (analyzedAnnotation.getType().equals(annotation)) {
				return ActionResult.error("Parameter still has annotation @%s",
						annotation);
			}
		}

		return ActionResult.ok();
	}

	@Override
	public String getDescription() {
		return String.format("Remove annotation @%s", annotation);
	}

}
