package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.*;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureField;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public class FieldTemplate implements ClassElementTemplate, EnumElementTemplate,
	EnumConstantElementTemplate {
	private final TypeReference type;

	private final String name;

	private final AccessModifier modifier;

	private final boolean shouldBeStatic;

	private final boolean shouldBeFinal;

	private final ImmutableList<FieldElementTemplate> templates;

	FieldTemplate(@Nonnull TypeReference type, @Nonnull String name) {
		this(type, name, AccessModifier.PRIVATE, false, false,
			ImmutableList.of());
	}

	private FieldTemplate(TypeReference type, String name,
		AccessModifier modifier, boolean shouldBeStatic, boolean shouldBeFinal,
		ImmutableList<FieldElementTemplate> templates) {
		this.type = type;
		this.name = name;
		this.modifier = modifier;
		this.shouldBeStatic = shouldBeStatic;
		this.shouldBeFinal = shouldBeFinal;
		this.templates = templates;
	}

	public FieldTemplate withAccessModifier(@Nonnull AccessModifier modifier) {
		return new FieldTemplate(type, name, modifier, shouldBeStatic,
			shouldBeFinal, templates);
	}

	public FieldTemplate whichIsStatic() {
		return new FieldTemplate(type, name, modifier, true, shouldBeFinal,
			templates);
	}

	public FieldTemplate whichIsFinal() {
		return new FieldTemplate(type, name, modifier, shouldBeStatic, true,
			templates);
	}

	public FieldTemplate with(@Nonnull FieldElementTemplate... templates) {
		return new FieldTemplate(type, name, modifier, shouldBeStatic,
			shouldBeFinal,
			ImmutableList.<FieldElementTemplate>builder().addAll(this.templates)
				.addAll(ImmutableList.copyOf(templates)).build());
	}

	@Override
	public void onClass(JavaRecipeBuilder builder,
		ClassScopeOperationBuilder classScopeOperationBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		onFieldContainer(builder, classScopeOperationBuilder,
			classScopeOperationBuilder.ensureField(name).typed(type.name())
				.withAccess(modifier), ClassScopeOperationBuilder::forField);
	}

	@Override
	public void onEnumConstant(JavaRecipeBuilder builder,
		EnumConstantScopeOperationBuilder enumConstantBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		onFieldContainer(builder, enumConstantBuilder,
			enumConstantBuilder.ensureField(name).typed(type.name())
				.withAccess(modifier),
			EnumConstantScopeOperationBuilder::forField);
	}

	@Override
	public void onEnum(JavaRecipeBuilder builder,
		EnumScopeOperationBuilder enumBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		onFieldContainer(builder, enumBuilder,
			enumBuilder.ensureField(name).typed(type.name())
				.withAccess(modifier), EnumScopeOperationBuilder::forField);
	}

	public <T> void onFieldContainer(JavaRecipeBuilder builder,
		T containerBuilder, EnsureField ensureField,
		BiFunction<T, String, FieldOperationBuilder> fieldLocator) {

		if (shouldBeStatic) {
			ensureField = ensureField.shouldBeStatic();
		}

		if (shouldBeFinal) {
			ensureField.shouldBeFinal();
		}

		templates.forEach(t -> t
			.onField(builder, fieldLocator.apply(containerBuilder, name)));
	}

}
