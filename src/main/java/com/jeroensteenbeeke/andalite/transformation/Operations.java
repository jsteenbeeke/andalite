package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.EnsureImports;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.EnsurePublicClass;

public class Operations {
	private Operations() {

	}

	public static CompilationUnitOperation hasPublicClass() {
		return new EnsurePublicClass();
	}

	public static CompilationUnitOperation imports(String fqdn) {
		return new EnsureImports(fqdn);
	}

	public static ClassOperation hasAnnotation(String annotation) {
		return null;
	}

}
