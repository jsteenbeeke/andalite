package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.EnumScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.HasEnumConstantBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

public class EnumConstantTemplate implements EnumElementTemplate {
	private final String name;

	private final ImmutableList<String> parameterExpressions;

	private final ImmutableList<EnumConstantElementTemplate> templates;

	EnumConstantTemplate(String name) {
		this.name = name;
		this.parameterExpressions = ImmutableList.of();
		this.templates = ImmutableList.of();
	}

	private EnumConstantTemplate(String name, ImmutableList<String> parameterExpressions,
		ImmutableList<EnumConstantElementTemplate> templates) {
		this.name = name;
		this.parameterExpressions = parameterExpressions;
		this.templates = templates;
	}

	public EnumConstantTemplate withParameterExpression(@Nonnull String expression) {
		return new EnumConstantTemplate(name,
			ImmutableList.<String>builder().addAll(parameterExpressions)
				.add(expression).build(), templates);
	}

	public EnumConstantTemplate withStringParameterExpression(@Nonnull String expression) {
		return new EnumConstantTemplate(name,
			ImmutableList.<String>builder().addAll(parameterExpressions)
				.add(String.format("\"%s\"", expression)).build(), templates);
	}

	public EnumConstantTemplate with(EnumConstantElementTemplate... templates) {
		return new EnumConstantTemplate(name, parameterExpressions, ImmutableList.<EnumConstantElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build());

	}

	@Override
	public void onEnum(JavaRecipeBuilder builder,
		EnumScopeOperationBuilder enumBuilder) {
		HasEnumConstantBuilder enumConstantBuilder = enumBuilder
			.ensureEnumConstant();

		for (String parameter : parameterExpressions) {
			enumConstantBuilder = enumConstantBuilder.withParameterExpression(parameter);
		}

		enumConstantBuilder.named(name);

		templates.forEach(t -> t.onEnumConstant(builder, enumBuilder.forConstant(name)));
	}

}
