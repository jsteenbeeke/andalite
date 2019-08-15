package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.ClassScopeOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClassTemplate {
	private static final ClassReference JAVA_LANG_OBJECT = ClassReference.of("java.lang.Object");

	private final Function<JavaRecipeBuilder, ClassScopeOperationBuilder> classLocator;

	private final Consumer<JavaRecipeBuilder> creator;

	private final TypeReference superclass;

	private final ImmutableList<TypeReference> interfaces;

	private final ImmutableList<ClassElementTemplate> templates;

	ClassTemplate(Consumer<JavaRecipeBuilder> creator, Function<JavaRecipeBuilder, ClassScopeOperationBuilder> classLocator) {
		this(creator, classLocator, JAVA_LANG_OBJECT, ImmutableList.of(), ImmutableList.of());
	}

	private ClassTemplate(Consumer<JavaRecipeBuilder> creator, Function<JavaRecipeBuilder, ClassScopeOperationBuilder> classLocator, TypeReference superclass, ImmutableList<TypeReference> interfaces, ImmutableList<ClassElementTemplate> templates) {
		this.creator = creator;
		this.classLocator = classLocator;
		this.superclass = superclass;
		this.interfaces = interfaces;
		this.templates = templates;
	}

	public TypeReference getSuperclass() {
		return superclass;
	}

	public ImmutableList<TypeReference> getInterfaces() {
		return interfaces;
	}

	public ClassTemplate withSuperClass(String fqdn) {
		return new ClassTemplate(creator, classLocator, TypeReference.of(fqdn), interfaces, templates);
	}

	public ClassTemplate withImplementedInterface(@Nonnull String fqdn) {
		return new ClassTemplate(creator, classLocator, superclass, ImmutableList.<TypeReference>builder()
			.addAll(interfaces)
			.add(TypeReference.of(fqdn))
			.build(), templates);
	}

	public ClassTemplate with(ClassElementTemplate... templates) {
		return new ClassTemplate(creator, classLocator, superclass, interfaces, ImmutableList.<ClassElementTemplate>builder()
			.addAll(this.templates)
			.addAll(ImmutableList.copyOf(templates))
			.build());

	}

	public OptionalInclusion when(boolean condition) {
		return templates -> {
			if (condition) {
				return with(templates);
			}

			return this;
		};
	}

	@FunctionalInterface
	public interface OptionalInclusion {
		ClassTemplate include(@Nonnull ClassElementTemplate...templates);
	}

	public JavaRecipe toRecipe() {
		JavaRecipeBuilder builder = new JavaRecipeBuilder();

		creator.accept(builder);

		if (!superclass.equals(JAVA_LANG_OBJECT)) {
			superclass.importStatement().ifPresent(builder::ensureImport);
			classLocator.apply(builder).ensureSuperclass(superclass.name());
		}

		for (TypeReference iface : interfaces) {
			iface.importStatement().ifPresent(builder::ensureImport);
			classLocator.apply(builder).ensureImplements(iface.name());
		}

		templates.forEach(t -> t.onClass(builder, classLocator.apply(builder)));

		return builder.build();
	}
}
