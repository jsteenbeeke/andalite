package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.*;

import javax.annotation.Nonnull;

public class ConstructorTemplate implements ClassElementTemplate {

	private final AccessModifier accessModifier;

	private final boolean shouldBeFinal;

	private final ImmutableList<ConstructorElementTemplate> templates;

	private final ImmutableList<ParameterTemplate> parameters;

	ConstructorTemplate() {
		this(AccessModifier.PUBLIC, false, ImmutableList.of(), ImmutableList.of());
	}

	private ConstructorTemplate(AccessModifier accessModifier, boolean shouldBeFinal, ImmutableList<ConstructorElementTemplate> templates, ImmutableList<ParameterTemplate> parameters) {
		this.accessModifier = accessModifier;
		this.shouldBeFinal = shouldBeFinal;
		this.templates = templates;
		this.parameters = parameters;
	}

	public ConstructorTemplate withAccessModifier(@Nonnull AccessModifier accessModifier) {
		return new ConstructorTemplate(accessModifier, shouldBeFinal, templates, parameters);
	}

	public ConstructorTemplate withParameters(ParameterTemplate... parameters) {
		return new ConstructorTemplate(accessModifier, shouldBeFinal, templates, ImmutableList.<ParameterTemplate>builder()
			.addAll(this.parameters)
			.addAll(ImmutableList.copyOf(parameters))
			.build());

	}

	public ConstructorTemplate whichIsFinal() {
		return new ConstructorTemplate(accessModifier, true, templates, parameters);
	}

	public ConstructorTemplate with(ConstructorElementTemplate... templates) {
		return new ConstructorTemplate(accessModifier, shouldBeFinal, ImmutableList.<ConstructorElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build(), parameters);

	}

	@Override
	public void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder classScopeOperationBuilder) {
		HasConstructorBuilder constructorBuilder = classScopeOperationBuilder.ensureConstructor();

		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement().ifPresent(builder::ensureImport);
			constructorBuilder = constructorBuilder.withParameter(parameter.getName()).ofType(parameter.getType().name());
		}

		constructorBuilder.withAccessModifier(accessModifier);

		ClassConstructorLocator constructorLocator = classScopeOperationBuilder.forConstructor();

		for (ParameterTemplate parameter : parameters) {
			constructorLocator = constructorLocator
				.withParameter(parameter.getName())
				.ofType(parameter.getType().name());
		}

		ConstructorOperationBuilder constructorOperationBuilder = constructorLocator.withModifier(accessModifier).get();


		if (constructorOperationBuilder != null) {
			int i = 0;
			for (ParameterTemplate parameter : parameters) {
				ParameterScopeOperationBuilder parameterScopeOperationBuilder = constructorOperationBuilder
					.forParameterAtIndex(i++);

				parameter.getTemplates().forEach(e -> e.onParameter(builder, parameterScopeOperationBuilder));
			}

			templates.forEach(t -> t.onConstructor(builder, constructorOperationBuilder));
		}
	}
}

