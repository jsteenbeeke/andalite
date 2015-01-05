package com.jeroensteenbeeke.andalite.analyzer.types;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class Wildcard extends AnalyzedType {
	private final Reference superReference;

	private final Reference extendsReference;

	public Wildcard(@Nonnull Location location,
			@Nullable Reference superReference,
			@Nullable Reference extendsReference) {
		super(location);
		this.superReference = superReference;
		this.extendsReference = extendsReference;
	}

	@CheckForNull
	public Reference getExtendsReference() {
		return extendsReference;
	}

	@CheckForNull
	public Reference getSuperReference() {
		return superReference;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		sb.append("?");
		if (superReference != null) {
			sb.append(" super ");
			sb.append(superReference.toJavaString());
		}
		if (extendsReference != null) {
			sb.append(" extends ");
			sb.append(extendsReference.toJavaString());
		}

		return sb.toString();
	}
}
