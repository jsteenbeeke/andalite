package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public class InterfaceTemplate {
	private final ImmutableList<TypeReference> interfaces;

	private final ImmutableList<InterfaceElementTemplate> templates;

	InterfaceTemplate(ImmutableList<TypeReference> interfaces, ImmutableList<InterfaceElementTemplate> templates) {
		this.interfaces = interfaces;
		this.templates = templates;
	}

	public InterfaceTemplate withExtendedInterface(@Nonnull String fqdn) {
		return new InterfaceTemplate(ImmutableList.<TypeReference>builder()
			.addAll(interfaces)
			.add(TypeReference.of(fqdn))
			.build(), templates);
	}

	public InterfaceTemplate with(InterfaceElementTemplate... templates) {
		return new InterfaceTemplate(interfaces, ImmutableList.<InterfaceElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build());

	}

	public JavaRecipe toRecipe() {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicInterface();

		for (TypeReference iface : interfaces) {
			iface.importStatement().ifPresent(builder::ensureImport);
			builder.inPublicInterface().ensureSuperInterface(iface.name());
		}

		templates.forEach(t -> t.onInterface(builder, builder.inPublicInterface()));

		return builder.build();
	}
}
