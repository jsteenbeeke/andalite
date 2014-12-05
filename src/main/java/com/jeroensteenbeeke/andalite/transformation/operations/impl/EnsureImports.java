package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedImport;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;

public class EnsureImports implements CompilationUnitOperation {
	private final String fqdn;

	public EnsureImports(String fqdn) {
		super();
		this.fqdn = fqdn;
	}

	@Override
	public List<Transformation> perform(AnalyzedSourceFile input) {
		for (AnalyzedImport analyzedImport : input.getImports()) {
			if (analyzedImport.matchesClass(fqdn)) {
				return ImmutableList.of();
			}
		}

		return ImmutableList.of(Transformation.insertAfter(
				input.getPackageDefinitionLocation(),
				String.format("import %s;\n", fqdn)));
	}

}
