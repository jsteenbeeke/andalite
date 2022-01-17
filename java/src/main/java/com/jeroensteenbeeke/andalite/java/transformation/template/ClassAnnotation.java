package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.ClassScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import org.jetbrains.annotations.NotNull;

public class ClassAnnotation extends AnnotationAdditionTemplate<ClassAnnotation> implements ClassElementTemplate {
	private final TypeReference type;

	ClassAnnotation(TypeReference type, ImmutableList<AnnotationValueTemplate<?>> templates) {
		super(templates);
		this.type = type;
	}

	@NotNull
	@Override
	protected ClassAnnotation newInstance(@NotNull ImmutableList<AnnotationValueTemplate<?>> templates) {
		return new ClassAnnotation(type, templates);
	}

	@Override
	public void onClass(JavaRecipeBuilder builder, ClassScopeOperationBuilder classScopeOperationBuilder) {
		type.importStatement().ifPresent(builder::ensureImport);
		classScopeOperationBuilder.ensureAnnotation(type.name());

		applyValues(builder, classScopeOperationBuilder.forAnnotation(type.name()));
	}
}
