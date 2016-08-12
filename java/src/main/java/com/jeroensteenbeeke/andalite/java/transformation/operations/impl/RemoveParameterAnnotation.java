package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IParameterOperation;

public class RemoveParameterAnnotation
		extends AbstractRemoveAnnotation<AnalyzedParameter>
		implements IParameterOperation {

	public RemoveParameterAnnotation(String annotation) {
		super(annotation);
	}

	@Override
	public Location getLocation(AnalyzedAnnotation annotation) {
		Location location = annotation.getLocation();
		return new Location(location.getStart() + 1, location.getEnd());
	}

}
