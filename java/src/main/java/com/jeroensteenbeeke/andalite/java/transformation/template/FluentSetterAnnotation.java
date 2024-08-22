package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;
import org.jetbrains.annotations.NotNull;

public class FluentSetterAnnotation extends AnnotationAdditionTemplate<FluentSetterAnnotation> implements PropertyElementTemplate {
	private final TypeReference type;

	FluentSetterAnnotation(TypeReference type) {
		this(type, ImmutableList.of());
	}

	private FluentSetterAnnotation(TypeReference type, ImmutableList<AnnotationValueTemplate<?>> templates) {
		super(templates);
		this.type = type;
	}

	@NotNull
	@Override
	protected FluentSetterAnnotation newInstance(@NotNull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new FluentSetterAnnotation(type, templates);
	}

	@Override
	public void onFluentSetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		methodBuilder.ensureAnnotation(type.name());

		applyValues(builder, methodBuilder.forAnnotation(type.name()));
	}
}
