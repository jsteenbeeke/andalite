package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.Location;

public abstract class Annotatable extends Locatable {
	private final Map<String, AnalyzedAnnotation> annotations;

	protected Annotatable(Location location) {
		super(location);
		this.annotations = Maps.newHashMap();
	}

	@Nonnull
	public final List<AnalyzedAnnotation> getAnnotations() {
		return ImmutableList.copyOf(annotations.values());
	}

	public final boolean hasAnnotation(@Nonnull String type) {
		return annotations.containsKey(type);
	}

	@CheckForNull
	public final AnalyzedAnnotation getAnnotation(@Nonnull String type) {
		return annotations.get(type);
	}

	void addAnnotation(@Nonnull AnalyzedAnnotation annotation) {
		this.annotations.put(annotation.getType(), annotation);
	}

	void addAnnotations(@Nonnull Iterable<AnalyzedAnnotation> annots) {
		for (AnalyzedAnnotation analyzedAnnotation : annots) {
			addAnnotation(analyzedAnnotation);
		}
	}

	public boolean isPrintNewlineAfterAnnotation() {
		return true;
	}

	@Override
	public void output(OutputCallback callback) {
		for (AnalyzedAnnotation analyzedAnnotation : getAnnotations()) {
			analyzedAnnotation.output(callback);
			if (isPrintNewlineAfterAnnotation()) {
				callback.newline();
			}
		}

		onOutput(callback);
	}

	public abstract void onOutput(OutputCallback callback);
}
