package com.jeroensteenbeeke.andalite.analyzer.types;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class TypeVoid extends AnalyzedType {

	public TypeVoid(Location location) {
		super(location);
	}

	@Override
	public String toJavaString() {
		return "void";
	}

}
