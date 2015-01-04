package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface IAnnotatable extends ILocatable {
	@Nonnull
	List<AnalyzedAnnotation> getAnnotations();

	boolean hasAnnotation(@Nonnull String type);

	@CheckForNull
	AnalyzedAnnotation getAnnotation(@Nonnull String type);
}
