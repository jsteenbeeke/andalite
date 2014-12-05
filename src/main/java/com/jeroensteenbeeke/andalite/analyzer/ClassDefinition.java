package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

final class ClassDefinition {
	private final String packageName;

	private final String className;

	ClassDefinition(@Nonnull String packageName, @Nonnull String className) {
		super();
		this.packageName = packageName;
		this.className = className;
	}

	@Nonnull
	public String getFullName() {
		StringBuilder sb = new StringBuilder();
		sb.append(packageName);
		if (!packageName.isEmpty()) {
			sb.append('.');
		}
		sb.append(className);

		return sb.toString();
	}

	@Nonnull
	public String getClassName() {
		return className;
	}

	@Nonnull
	public String getPackageName() {
		return packageName;
	}

	@Nonnull
	public static ClassDefinition fromFQDN(@Nonnull String fqdn) {
		if (fqdn.isEmpty()) {
			return new ClassDefinition("", "");
		} else {
			final int dotIndex = fqdn.lastIndexOf('.');

			if (dotIndex == -1) {
				return new ClassDefinition("", fqdn);
			} else {
				final String packageName = fqdn.substring(0, dotIndex);
				final String className = fqdn.substring(dotIndex + 1);

				return new ClassDefinition(packageName, className);
			}
		}
	}
}
