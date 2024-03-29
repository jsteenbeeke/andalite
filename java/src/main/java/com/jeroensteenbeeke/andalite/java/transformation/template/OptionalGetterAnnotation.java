package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import org.jetbrains.annotations.NotNull;

public class OptionalGetterAnnotation extends AnnotationAdditionTemplate<OptionalGetterAnnotation> implements PropertyElementTemplate {
	private final TypeReference type;

	OptionalGetterAnnotation(TypeReference type) {
		this(type, ImmutableList.of());
	}

	private OptionalGetterAnnotation(TypeReference type, ImmutableList<AnnotationValueTemplate<?>> values) {
		super(values);
		this.type = type;
	}

	@NotNull
	@Override
	protected OptionalGetterAnnotation newInstance(@NotNull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new OptionalGetterAnnotation(type, templates);
	}

	@Override
	public void onOptionalGetter(JavaRecipeBuilder builder, MethodOperationBuilder methodBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		methodBuilder.ensureAnnotation(type.name());

		applyValues(builder, methodBuilder.forAnnotation(type.name()));
	}
}
