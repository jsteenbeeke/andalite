package com.jeroensteenbeeke.andalite.java.analyzer.types;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

public class ArrayType extends AnalyzedType{
	private AnalyzedType containedType;

	public ArrayType(Location location, AnalyzedType containedType) {
		super(location);
		this.containedType = containedType;
	}

	@Override
	public String toJavaString() {
		return containedType.toJavaString() + "[]";
	}
}
