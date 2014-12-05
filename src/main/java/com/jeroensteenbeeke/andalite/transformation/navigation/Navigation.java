package com.jeroensteenbeeke.andalite.transformation.navigation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;

public interface Navigation<T extends Locatable> {
	@Nonnull
	T navigate(@Nonnull AnalyzedSourceFile file) throws NavigationException;

	@Nonnull
	String getDescription();

}
