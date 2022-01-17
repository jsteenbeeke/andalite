package com.jeroensteenbeeke.andalite.java.analyzer;

import org.jetbrains.annotations.NotNull;

public interface IAnnotationAddable<T extends IAnnotationAddable<T>> {
	T addAnnotation(@NotNull AnalyzedAnnotation annotation);
}
