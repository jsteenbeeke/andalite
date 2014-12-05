package com.jeroensteenbeeke.andalite.analyzer.annotation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.OutputCallback;

public class AnnotationValue extends BaseValue<AnalyzedAnnotation> {

	public AnnotationValue(String name, AnalyzedAnnotation value) {
		super(name, value);
	}

	@Override
	public void output(OutputCallback callback) {
		getValue().output(callback);
	}
}
