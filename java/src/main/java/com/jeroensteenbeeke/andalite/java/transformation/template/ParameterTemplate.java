package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;

public class ParameterTemplate {
	private final TypeReference type;

	private final String name;

	private final ImmutableList<ParameterElementTemplate> templates;

	ParameterTemplate(TypeReference type, String name) {
		this(type, name, ImmutableList.of());
	}

	private ParameterTemplate(TypeReference type, String name, ImmutableList<ParameterElementTemplate> templates) {
		this.type = type;
		this.name = name;
		this.templates = templates;
	}

	public TypeReference getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ImmutableList<ParameterElementTemplate> getTemplates() {
		return templates;
	}

	public ParameterTemplate with(ParameterElementTemplate... templates) {
		return new ParameterTemplate(type, name, ImmutableList.<ParameterElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build());

	}
}
