package com.jeroensteenbeeke.andalite.java.transformation.template;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.Optional;

import static com.jeroensteenbeeke.andalite.java.transformation.template.Templates.JAVA_LANG;

final class ClassReference implements TypeReference {
	private final String prefix;

	private final String name;

	private ClassReference(String prefix, String name) {
		this.prefix = prefix;
		this.name = name;
	}

	public String prefix() {
		return prefix;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean nullable() {
		return true;
	}

	@Override
	public Optional<String> importStatement() {
		if (prefix.equals(JAVA_LANG)) {
			return Optional.empty();
		}

		return Optional.of(prefix()).filter(s -> !s.isBlank())
			.map(prefix -> prefix + "." + name()).or(() -> Optional.of(name()));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClassReference that = (ClassReference) o;
		return prefix.equals(that.prefix) &&
			name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prefix, name);
	}

	public static ClassReference of(@NotNull String fqdn) {
		int lastDotIndex = fqdn.lastIndexOf('.');

		if (lastDotIndex == -1) {
			return new ClassReference("", fqdn);
		} else {
			return new ClassReference(fqdn.substring(0, lastDotIndex), fqdn.substring(lastDotIndex+1));
		}
	}
}
