package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.*;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public class ConstructorTemplate
	implements ClassElementTemplate, EnumElementTemplate {

	private final AccessModifier accessModifier;

	private final boolean shouldBeFinal;

	private final ImmutableList<ConstructorElementTemplate> templates;

	private final ImmutableList<ParameterTemplate> parameters;

	ConstructorTemplate() {
		this(AccessModifier.PUBLIC, false, ImmutableList.of(),
			ImmutableList.of());
	}

	private ConstructorTemplate(AccessModifier accessModifier,
		boolean shouldBeFinal,
		ImmutableList<ConstructorElementTemplate> templates,
		ImmutableList<ParameterTemplate> parameters) {
		this.accessModifier = accessModifier;
		this.shouldBeFinal = shouldBeFinal;
		this.templates = templates;
		this.parameters = parameters;
	}

	public ConstructorTemplate withAccessModifier(
		@Nonnull AccessModifier accessModifier) {
		return new ConstructorTemplate(accessModifier, shouldBeFinal, templates,
			parameters);
	}

	public ConstructorTemplate withParameters(ParameterTemplate... parameters) {
		return new ConstructorTemplate(accessModifier, shouldBeFinal, templates,
			ImmutableList.<ParameterTemplate>builder().addAll(this.parameters)
				.addAll(ImmutableList.copyOf(parameters)).build());

	}

	public ConstructorTemplate whichIsFinal() {
		return new ConstructorTemplate(accessModifier, true, templates,
			parameters);
	}

	public ConstructorTemplate with(ConstructorElementTemplate... templates) {
		return new ConstructorTemplate(accessModifier, shouldBeFinal,
			ImmutableList.<ConstructorElementTemplate>builder()
				.addAll(this.templates).addAll(ImmutableList.copyOf(templates))
				.build(), parameters);

	}

	@Override
	public void onEnum(JavaRecipeBuilder builder,
		EnumScopeOperationBuilder enumBuilder) {
		HasConstructorBuilder constructorBuilder = enumBuilder
			.ensureConstructor();

		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement()
				.ifPresent(builder::ensureImport);
			constructorBuilder = constructorBuilder
				.withParameter(parameter.getName())
				.ofType(parameter.getType().name());
		}

		constructorBuilder.withAccessModifier(accessModifier);

		EnumConstructorLocator constructorLocator = enumBuilder
			.forConstructor();

		ParameterAcceptor<EnumConstructorLocator> locator = (EnumConstructorLocator l, String name, String type) -> l
			.withParameter(name).ofType(type);

		handleParametersAndTemplates(builder, constructorLocator, locator,
			(enumConstructorLocator, accessModifier) -> enumConstructorLocator
				.withModifier(accessModifier).get());
	}

	private <T> void handleParametersAndTemplates(JavaRecipeBuilder builder,
		T constructorLocator, ParameterAcceptor<T> acceptor,
		BiFunction<T, AccessModifier, ConstructorOperationBuilder> createOperation) {
		for (ParameterTemplate parameter : parameters) {
			constructorLocator = acceptor
				.withParameter(constructorLocator, parameter.getName(),
					parameter.getType().name());
		}

		ConstructorOperationBuilder constructorOperationBuilder = createOperation
			.apply(constructorLocator, accessModifier);

		if (constructorOperationBuilder != null) {
			int i = 0;
			for (ParameterTemplate parameter : parameters) {
				ParameterScopeOperationBuilder parameterScopeOperationBuilder = constructorOperationBuilder
					.forParameterAtIndex(i++);

				parameter.getTemplates().forEach(e -> e
					.onParameter(builder, parameterScopeOperationBuilder));
			}

			templates.forEach(
				t -> t.onConstructor(builder, constructorOperationBuilder));
		}
	}

	@Override
	public void onClass(JavaRecipeBuilder builder,
		ClassScopeOperationBuilder classScopeOperationBuilder) {
		HasConstructorBuilder constructorBuilder = classScopeOperationBuilder
			.ensureConstructor();

		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement()
				.ifPresent(builder::ensureImport);
			constructorBuilder = constructorBuilder
				.withParameter(parameter.getName())
				.ofType(parameter.getType().name());
		}

		constructorBuilder.withAccessModifier(accessModifier);

		ClassConstructorLocator constructorLocator = classScopeOperationBuilder
			.forConstructor();

		ParameterAcceptor<ClassConstructorLocator> locator = (ClassConstructorLocator l, String name, String type) -> l
			.withParameter(name).ofType(type);

		BiFunction<ClassConstructorLocator, AccessModifier, ConstructorOperationBuilder> finalizer = (loc, mod) -> loc
			.withModifier(mod).get();
		handleParametersAndTemplates(builder, constructorLocator, locator,
			finalizer);

	}

	private interface ParameterAcceptor<T> {
		T withParameter(@Nonnull T base, @Nonnull String name,
			@Nonnull String type);
	}
}

