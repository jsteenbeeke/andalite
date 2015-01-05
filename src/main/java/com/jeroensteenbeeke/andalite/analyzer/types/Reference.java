package com.jeroensteenbeeke.andalite.analyzer.types;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class Reference extends AnalyzedType {

	private final AnalyzedType referencedType;
	private final int arrayCount;

	public Reference(@Nonnull final Location location,
			@Nonnull final AnalyzedType referencedType, int arrayCount) {
		super(location);
		this.referencedType = referencedType;
		this.arrayCount = arrayCount;
	}

	public int getArrayCount() {
		return arrayCount;
	}

	public AnalyzedType getReferencedType() {
		return referencedType;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		sb.append(referencedType.toJavaString());
		for (int i = 0; i < arrayCount; i++) {
			sb.append("[]");
		}

		return sb.toString();

	}
}
