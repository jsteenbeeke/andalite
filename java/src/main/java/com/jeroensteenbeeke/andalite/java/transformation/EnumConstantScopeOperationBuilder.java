package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnumConstant;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumConstantOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumConstantField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureField;

import org.jetbrains.annotations.NotNull;

public class EnumConstantScopeOperationBuilder extends AbstractOperationBuilder<AnalyzedEnumConstant, IEnumConstantOperation> {
	protected EnumConstantScopeOperationBuilder(IStepCollector collector, IJavaNavigation<AnalyzedEnumConstant> navigation) {
		super(collector, navigation);


	}

	public EnsureEnumConstantMethodBuilder ensureMethod() {
		return new EnsureEnumConstantMethodBuilder(this::ensure);
	}

	@NotNull
	public EnumConstantMethodLocator forMethod() {
		return new EnumConstantMethodLocator(getCollector(), getNavigation());
	}

	@NotNull
	public ClassScopeOperationBuilder.WithType ensureField(@NotNull String name) {
		return type -> accessModifier -> {
			EnsureEnumConstantField operation = new EnsureEnumConstantField(name, type, accessModifier);
			ensure(operation);
			return operation;
		};
	}

	@NotNull
	public FieldOperationBuilder forField(String name) {
		return new FieldOperationBuilder(getCollector(), getNavigation(), name);
	}
}
