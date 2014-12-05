package com.jeroensteenbeeke.andalite.transformation.navigation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;

public class CompilationUnitNavigation implements
		Navigation<AnalyzedSourceFile> {
	private static final CompilationUnitNavigation instance = new CompilationUnitNavigation();

	private CompilationUnitNavigation() {
	}

	public static CompilationUnitNavigation getInstance() {
		return instance;
	}

	@Override
	public AnalyzedSourceFile navigate(AnalyzedSourceFile file) {
		return file;
	}

	@Override
	public String getDescription() {
		return "Java File";
	}
}
