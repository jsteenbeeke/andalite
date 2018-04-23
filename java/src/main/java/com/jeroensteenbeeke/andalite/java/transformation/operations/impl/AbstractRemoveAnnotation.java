package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
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
				transformations.add(Transformation
						.replace(getLocation(analyzedAnnotation), ""));
			}
		}

		return transformations.build();
	}

	public Location getLocation(AnalyzedAnnotation annotation) {
		return annotation.getLocation();
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
