package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.ClassScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.NamedReturnType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class PropertyTemplate implements ClassElementTemplate {
	private final TypeReference type;

	private final String name;

	private final boolean nullable;

	private final boolean fluent;

	private final ImmutableList<PropertyElementTemplate> templates;

	private final ImmutableList<String> extraImports;

	PropertyTemplate(TypeReference type, String name) {
		this(type, name, type.nullable(), false, ImmutableList.of(), ImmutableList.of());
	}

	private PropertyTemplate(
		TypeReference type, String name, boolean nullable, boolean fluent, ImmutableList<PropertyElementTemplate> templates,
		ImmutableList<String> extraImports) {
		this.type = type;
		this.name = name;
		this.nullable = nullable;
		this.templates = templates;
		this.extraImports = extraImports;
		this.fluent = fluent;
	}

	public PropertyTemplate nullable(boolean nullable) {
		if (nullable == this.nullable) {
			return this;
		}

		if (nullable && !type.nullable()) {
			return this;
		}

		return new PropertyTemplate(type, name, nullable, fluent, templates, extraImports);
	}

	public PropertyTemplate nonNull() {
		if (!type.nullable()) {
			return this;
		}

		return new PropertyTemplate(type, name, false, fluent, templates, extraImports);
	}

	public PropertyTemplate fluentSetter() {
		if (fluent) {
			return this;
		}

		return new PropertyTemplate(type, name, nullable, true, templates, extraImports);
	}


	public PropertyTemplate with(PropertyElementTemplate... templates) {
		return new PropertyTemplate(
			type, name, nullable, fluent,
			ImmutableList.<PropertyElementTemplate>builder()
				.addAll(this.templates)
				.addAll(ImmutableList.copyOf(templates))
				.build()
			, extraImports
		);
	}

	public ConditionalInclusion ifMatched(@NotNull Predicate<PropertyTemplate> condition) {
		return templates -> {
			if (!condition.test(this)) {
				return this;
			}

			return with(templates);
		};
	}

	public ConditionalInclusion ifNotMatched(@NotNull Predicate<PropertyTemplate> condition) {
		return ifMatched(condition.negate());
	}

	public PropertyTemplate whichRequiresImport(String... imports) {
		return new PropertyTemplate(
			type, name, nullable, fluent, templates,
			ImmutableList.<String>builder()
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
	public void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder inClass) {
		type.importStatement().ifPresent(builder::ensureImport);
		inClass.ensureField(name).typed(type.name()).withAccess(AccessModifier.PRIVATE);
		extraImports.forEach(builder::ensureImport);

		templates.forEach(t -> t.onField(builder, inClass.forField(name)));

		final String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		final String setter = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

		inClass
			.ensureMethod()
			.withReturnType(type.toMethodReturnType())
			.withModifier(AccessModifier.PUBLIC)
			.named(getter);
		MethodOperationBuilder getterOperationBuilder = inClass
			.forMethod()
			.withReturnType(type.toMethodReturnType())
			.withModifier(AccessModifier.PUBLIC)
			.named(getter);
		getterOperationBuilder
			.inBody()
			.ensureReturnAsLastStatement(name);

		templates.forEach(t -> t.onGetter(builder, getterOperationBuilder));

		if (nullable) {
			builder.ensureImport("java.util.Optional");
			inClass.ensureMethod().withReturnType(new NamedReturnType(String.format("Optional<%s>", type.name())))
				.withModifier(AccessModifier.PUBLIC).named(name);
			MethodOperationBuilder optionalGetterOperationBuilder = inClass
				.forMethod()
				.withReturnType(new NamedReturnType(String.format("Optional<%s>", type.name())))
				.withModifier(AccessModifier.PUBLIC)
				.named(name);
			optionalGetterOperationBuilder
				.inBody().ensureReturnAsLastStatement("Optional.ofNullable(" + name + ")");

			templates.forEach(t -> t.onOptionalGetter(builder, optionalGetterOperationBuilder));
		}

		if (fluent) {
			inClass
				.ensureMethod()
				.withFluentReturnType()
				.withParameter(name)
				.ofType(type.name())
				.withModifier(AccessModifier.PUBLIC)
				.named(setter);
		} else {
			inClass
				.ensureMethod()
				.withParameter(name)
				.ofType(type.name())
				.withModifier(AccessModifier.PUBLIC)
				.named(setter);
		}

		MethodOperationBuilder setterOperationBuilder = fluent ? inClass
			.forMethod()
			.withFluentReturnType()
			.withParameter(name)
			.ofType(type.name())
			.withModifier(AccessModifier.PUBLIC)
			.named(setter) : inClass
			.forMethod()
			.withParameter(name)
			.ofType(type.name())
			.withModifier(AccessModifier.PUBLIC)
			.named(setter);
		setterOperationBuilder
			.inBody()
			.ensureStatement(String.format("this.%1$s = %1$s", this.name));
		if (fluent) {
			setterOperationBuilder
				.inBody()
				.ensureStatement("return this");
		}
		templates.forEach(t -> t.onSetter(builder, setterOperationBuilder));

	}

	@FunctionalInterface
	public interface ConditionalInclusion {
		@NotNull
		PropertyTemplate include(@NotNull PropertyElementTemplate... templates);
	}
}
