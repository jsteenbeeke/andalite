package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.ClassScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureField;

import javax.annotation.Nonnull;

public class FieldTemplate implements ClassElementTemplate {
	private final TypeReference type;

	private final String name;

	private final AccessModifier modifier;

	private final boolean shouldBeStatic;

	private final boolean shouldBeFinal;

	private final ImmutableList<FieldElementTemplate> templates;

	FieldTemplate(@Nonnull TypeReference type, @Nonnull String name) {
		this(type, name, AccessModifier.PRIVATE, false, false, ImmutableList.of());
	}

	private FieldTemplate(TypeReference type, String name, AccessModifier modifier, boolean shouldBeStatic, boolean shouldBeFinal, ImmutableList<FieldElementTemplate> templates) {
		this.type = type;
		this.name = name;
		this.modifier = modifier;
		this.shouldBeStatic = shouldBeStatic;
		this.shouldBeFinal = shouldBeFinal;
		this.templates = templates;
	}

	public FieldTemplate withAccessModifier(@Nonnull AccessModifier modifier) {
		return new FieldTemplate(type, name, modifier, shouldBeStatic, shouldBeFinal, templates);
	}

	public FieldTemplate whichIsStatic() {
		return new FieldTemplate(type, name, modifier, true, shouldBeFinal, templates);
	}

	public FieldTemplate whichIsFinal() {
		return new FieldTemplate(type, name, modifier, shouldBeStatic, true, templates);
	}

	public FieldTemplate with(@Nonnull FieldElementTemplate... templates) {
		return new FieldTemplate(type, name, modifier, shouldBeStatic, shouldBeFinal, ImmutableList.<FieldElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build());
	}

	@Override
	public void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder classScopeOperationBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		EnsureField ensureField = classScopeOperationBuilder
			.ensureField(name)
			.typed(type.name())
			.withAccess(modifier);

		if (shouldBeStatic) {
			ensureField.shouldBeStatic();
		}

		if (shouldBeFinal) {
			ensureField.shouldBeFinal();
		}

		templates.forEach(t -> t.onField(builder, classScopeOperationBuilder.forField(name)));
	}
}
