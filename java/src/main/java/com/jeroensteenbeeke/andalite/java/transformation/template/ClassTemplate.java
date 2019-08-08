package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableSet;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;

import static com.jeroensteenbeeke.andalite.java.transformation.template.Templates.JAVA_LANG;

public class ClassTemplate {
	private static final ClassReference JAVA_LANG_OBJECT = ClassReference.of("java.lang.Object");

	private final ClassReference superclass;

	private final ImmutableSet<ClassReference> interfaces;

	private final ImmutableSet<ClassElementTemplate> templates;

	ClassTemplate() {
		this(JAVA_LANG_OBJECT, ImmutableSet.of(), ImmutableSet.of());
	}

	private ClassTemplate(ClassReference superclass, ImmutableSet<ClassReference> interfaces, ImmutableSet<ClassElementTemplate> templates) {
		this.superclass = superclass;
		this.interfaces = interfaces;
		this.templates = templates;
	}

	public ClassTemplate withSuperClass(String fqdn) {
		return new ClassTemplate(ClassReference.of(fqdn), interfaces, templates);
	}

	public ClassTemplate withImplementedInterface(@Nonnull String fqdn) {
		return new ClassTemplate(superclass, ImmutableSet.<ClassReference>builder()
			.addAll(interfaces)
			.add(ClassReference.of(fqdn))
			.build(), templates);
	}

	public ClassTemplate containing(ClassElementTemplate... templates) {
		return new ClassTemplate(superclass, interfaces, ImmutableSet.<ClassElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableSet.copyOf(templates))
			.build());

	}

	public JavaRecipe toRecipe() {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		builder.ensurePublicClass();

		if (!superclass.equals(JAVA_LANG_OBJECT)) {
			superclass.importStatement().ifPresent(builder::ensureImport);
			builder.inPublicClass().ensureSuperclass(superclass.name());
		}

		for (ClassReference iface : interfaces) {
			iface.importStatement().ifPresent(builder::ensureImport);
			builder.inPublicClass().ensureImplements(iface.name());
		}

		templates.forEach(t -> t.applyTo(builder));

		return builder.build();
	}
}
