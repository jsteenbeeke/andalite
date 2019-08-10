package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public class EnumTemplate {
	private final ImmutableList<TypeReference> interfaces;

	private final ImmutableList<EnumElementTemplate> templates;

	EnumTemplate(ImmutableList<TypeReference> interfaces,
		ImmutableList<EnumElementTemplate> templates) {
		this.interfaces = interfaces;
		this.templates = templates;
	}

	public EnumTemplate withImplementedInterface(@Nonnull String fqdn) {
		return new EnumTemplate(
			ImmutableList.<TypeReference>builder().addAll(interfaces)
				.add(TypeReference.of(fqdn)).build(), templates);
	}

	public EnumTemplate with(EnumElementTemplate... templates) {
		return new EnumTemplate(interfaces,
			ImmutableList.<EnumElementTemplate>builder().addAll(this.templates)
				.addAll(ImmutableList.copyOf(templates)).build());

	}

	public JavaRecipe toRecipe() {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicEnum();

		for (TypeReference iface : interfaces) {
			iface.importStatement().ifPresent(builder::ensureImport);
			builder.inPublicEnum().ensureImplementedInterface(iface.name());
		}

		templates.forEach(t -> t.onEnum(builder, builder.inPublicEnum()));

		return builder.build();
	}
}
