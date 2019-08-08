package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

public class PropertyTemplate implements ClassElementTemplate {
	private final TypeReference type;

	private final String name;

	private final boolean nullable;

	private final ImmutableSet<PropertyElementTemplate> templates;

	private final ImmutableSet<String> extraImports;

	PropertyTemplate(TypeReference type, String name) {
		this(type, name, type.nullable(), ImmutableSet.of(), ImmutableSet.of());
	}

	private PropertyTemplate(TypeReference type, String name, boolean nullable, ImmutableSet<PropertyElementTemplate> templates, ImmutableSet<String> extraImports) {
		this.type = type;
		this.name = name;
		this.nullable = nullable;
		this.templates = templates;
		this.extraImports = extraImports;
	}

	public PropertyTemplate nonNull() {
		if (!type.nullable()) {
			return this;
		}

		return new PropertyTemplate(type, name, false, templates, extraImports);
	}

	public PropertyTemplate with(PropertyElementTemplate... templates) {
		return new PropertyTemplate(
			type, name, nullable,
			ImmutableSet.<PropertyElementTemplate>builder()
				.addAll(this.templates)
				.addAll(ImmutableList.copyOf(templates))
				.build()
			, extraImports
		);
	}

	public PropertyTemplate whichRequiresImport(String... imports) {
		return new PropertyTemplate(
			type, name, nullable, templates,
			ImmutableSet.<String>builder()
				.addAll(this.extraImports)
				.addAll(ImmutableList.copyOf(imports))
				.build()
		);
	}

	public TypeReference getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isNullable() {
		return nullable;
	}

	@Override
	public void applyTo(JavaRecipeBuilder builder) {
		type.importStatement().ifPresent(builder::ensureImport);
		builder.inPublicClass().ensureField(name).typed(type.name()).withAccess(AccessModifier.PRIVATE);
		extraImports.forEach(builder::ensureImport);

		templates.forEach(t -> t.onField(builder, builder.inPublicClass().forField(name)));

		final String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		final String setter = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

		builder
			.inPublicClass()
			.ensureMethod()
			.withReturnType(type.name())
			.withModifier(AccessModifier.PUBLIC)
			.named(getter);
		MethodOperationBuilder getterOperationBuilder = builder
			.inPublicClass()
			.forMethod()
			.withReturnType(type.name())
			.withModifier(AccessModifier.PUBLIC)
			.named(getter);
		getterOperationBuilder
			.inBody()
			.ensureReturnAsLastStatement(name);

		templates.forEach(t -> t.onGetter(builder, getterOperationBuilder));

		if (nullable) {
			builder.ensureImport("java.util.Optional");
			builder.inPublicClass().ensureMethod().withReturnType(String.format("Optional<%s>", type.name()))
				   .withModifier(AccessModifier.PUBLIC).named(name);
			MethodOperationBuilder optionalGetterOperationBuilder = builder
				.inPublicClass()
				.forMethod()
				.withReturnType(String.format("Optional<%s>", type.name()))
				.withModifier(AccessModifier.PUBLIC)
				.named(name);
			optionalGetterOperationBuilder
				.inBody().ensureReturnAsLastStatement("Optional.ofNullable(" + name + ")");

			templates.forEach(t -> t.onOptionalGetter(builder, optionalGetterOperationBuilder));
		}

		builder
			.inPublicClass()
			.ensureMethod()
			.withParameter("name")
			.ofType(type.name())
			.withModifier(AccessModifier.PUBLIC)
			.named(setter);

		MethodOperationBuilder setterOperationBuilder = builder
			.inPublicClass()
			.forMethod()
			.withParameter("name")
			.ofType(type.name())
			.withModifier(AccessModifier.PUBLIC)
			.named(setter);
		setterOperationBuilder
			.inBody()
			.ensureStatement(String.format("this.%1$s = %1$s", this.name));
		templates.forEach(t -> t.onSetter(builder, setterOperationBuilder));

	}
}
