package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.*;

import org.jetbrains.annotations.NotNull;

public class MethodTemplate implements ClassElementTemplate, InterfaceElementTemplate, EnumElementTemplate, EnumConstantElementTemplate {
	private final TypeReference type;

	private final String name;

	private final AccessModifier accessModifier;
	private final boolean shouldBeFinal;

	private final ImmutableList<MethodElementTemplate> templates;

	private final ImmutableList<ParameterTemplate> parameters;

	MethodTemplate(TypeReference type, String name) {
		this(type, name, AccessModifier.PUBLIC, false, ImmutableList.of(), ImmutableList.of());
	}

	private MethodTemplate(TypeReference type, String name, AccessModifier accessModifier, boolean shouldBeFinal, ImmutableList<MethodElementTemplate> templates, ImmutableList<ParameterTemplate> parameters) {
		this.type = type;
		this.name = name;
		this.accessModifier = accessModifier;
		this.shouldBeFinal = shouldBeFinal;
		this.templates = templates;
		this.parameters = parameters;
	}

	public MethodTemplate withAccessModifier(@NotNull AccessModifier accessModifier) {
		return new MethodTemplate(type, name, accessModifier, shouldBeFinal, templates, parameters);
	}

	public MethodTemplate withParameters(ParameterTemplate... parameters) {
		return new MethodTemplate(type, name, accessModifier, shouldBeFinal, templates, ImmutableList.<ParameterTemplate>builder()
			.addAll(this.parameters)
			.addAll(ImmutableList.copyOf(parameters))
			.build());

	}

	public MethodTemplate whichIsFinal() {
		return new MethodTemplate(type, name, accessModifier, true, templates, parameters);
	}

	public MethodTemplate with(MethodElementTemplate... templates) {
		return new MethodTemplate(type, name, accessModifier, shouldBeFinal, ImmutableList.<MethodElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build(), parameters);

	}

	@Override
	public void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder classScopeOperationBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		EnsureClassMethodBuilder methodBuilder = classScopeOperationBuilder.ensureMethod()
																		   .withReturnType(type.name())
																		   .withModifier(accessModifier);
		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement().ifPresent(builder::ensureImport);
			methodBuilder = methodBuilder.withParameter(parameter.getName()).ofType(parameter.getType().name());
		}

		methodBuilder.named(name);

		ClassMethodLocator classMethodLocator = classScopeOperationBuilder.forMethod()
																		  .withReturnType(type.name())
																		  .withModifier(accessModifier);

		ensureParametersAndRunTemplates(builder, classMethodLocator);

	}

	@Override
	public void onInterface(JavaRecipeBuilder builder, InterfaceScopeOperationBuilder interfaceScopeOperationBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		EnsureInterfaceMethodBuilder methodBuilder = interfaceScopeOperationBuilder
			.ensureMethod()
			.withReturnType(type.name())
			.withModifier(accessModifier);

		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement().ifPresent(builder::ensureImport);
			methodBuilder = methodBuilder.withParameter(parameter.getName()).ofType(parameter.getType().name());
		}


		methodBuilder.named(name);


		InterfaceMethodLocator interfaceMethodLocator = interfaceScopeOperationBuilder.forMethod()
																					  .withReturnType(type.name())
																					  .withModifier(accessModifier);

		ensureParametersAndRunTemplates(builder, interfaceMethodLocator);
	}

	@Override
	public void onEnumConstant(JavaRecipeBuilder builder,
		EnumConstantScopeOperationBuilder enumConstantBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		EnsureEnumConstantMethodBuilder methodBuilder = enumConstantBuilder
			.ensureMethod()
			.withReturnType(type.name())
			.withModifier(accessModifier);

		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement().ifPresent(builder::ensureImport);
			methodBuilder = methodBuilder.withParameter(parameter.getName()).ofType(parameter.getType().name());
		}

		methodBuilder.named(name);

		EnumConstantMethodLocator interfaceMethodLocator = enumConstantBuilder.forMethod()
			.withReturnType(type.name())
			.withModifier(accessModifier);

		ensureParametersAndRunTemplates(builder, interfaceMethodLocator);
	}

	@Override
	public void onEnum(JavaRecipeBuilder builder,
		EnumScopeOperationBuilder enumBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);

		EnsureEnumMethodBuilder methodBuilder = enumBuilder
			.ensureMethod()
			.withReturnType(type.name())
			.withModifier(accessModifier);

		for (ParameterTemplate parameter : parameters) {
			parameter.getType().importStatement().ifPresent(builder::ensureImport);
			methodBuilder = methodBuilder.withParameter(parameter.getName()).ofType(parameter.getType().name());
		}

		methodBuilder.named(name);

		EnumMethodLocator interfaceMethodLocator = enumBuilder.forMethod()
			.withReturnType(type.name())
			.withModifier(accessModifier);

		ensureParametersAndRunTemplates(builder, interfaceMethodLocator);
	}

	private <T extends AbstractMethodBuilder<MethodOperationBuilder,T>> void ensureParametersAndRunTemplates(@NotNull JavaRecipeBuilder builder, @NotNull T methodLocator) {
		for (ParameterTemplate parameter : parameters) {
			methodLocator = methodLocator
				.withParameter(parameter.getName())
				.ofType(parameter.getType().name());
		}

		MethodOperationBuilder methodOperationBuilder = methodLocator.named(name);


		if (methodOperationBuilder != null) {
			if (shouldBeFinal) {
				methodOperationBuilder.ensureFinal();
			}

			int i = 0;
			for (ParameterTemplate parameter : parameters) {
				ParameterScopeOperationBuilder parameterScopeOperationBuilder = methodOperationBuilder.forParameterAtIndex(i++);

				parameter.getTemplates().forEach(e -> e.onParameter(builder, parameterScopeOperationBuilder));
			}

			templates.forEach(t -> t.onMethod(builder, methodOperationBuilder));
		}
	}
}

