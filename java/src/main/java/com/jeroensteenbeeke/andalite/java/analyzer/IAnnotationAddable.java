package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.Nonnull;

public interface IAnnotationAddable<T extends IAnnotationAddable<T>> {
	T addAnnotation(@Nonnull AnalyzedAnnotation annotation);
}
