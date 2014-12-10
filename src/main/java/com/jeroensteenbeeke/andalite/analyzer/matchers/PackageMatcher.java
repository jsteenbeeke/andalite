package com.jeroensteenbeeke.andalite.analyzer.matchers;

import javax.annotation.Nonnull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;

class PackageMatcher extends TypeSafeMatcher<AnalyzedSourceFile> {
	private final String expectedName;

	public PackageMatcher(@Nonnull String expectedName) {
		this.expectedName = expectedName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" in package ").appendText(expectedName);
	}

	@Override
	protected boolean matchesSafely(AnalyzedSourceFile item) {
		return expectedName.equals(item.getPackageName());
	}

}
