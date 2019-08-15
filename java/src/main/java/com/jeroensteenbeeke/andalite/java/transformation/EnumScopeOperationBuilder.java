package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.EnumConstantNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.*;

import javax.annotation.Nonnull;

public class EnumScopeOperationBuilder
		extends AbstractOperationBuilder<AnalyzedEnum, IEnumOperation> {

	public EnumScopeOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedEnum> navigation) {
		super(collector, navigation);
	}

	public HasEnumConstantBuilder ensureEnumConstant() {
		return new HasEnumConstantBuilder(this::ensure);
	}

	public EnsureEnumMethodBuilder ensureMethod() {
		return new EnsureEnumMethodBuilder(this::ensure);
	}

	@Nonnull
	public HasConstructorBuilder<EnsureEnumConstructorOperation,AnalyzedEnum, AnalyzedEnum.EnumInsertionPoint> ensureConstructor() {
		return new HasConstructorBuilder<>(this::ensure,
			EnsureEnumConstructorOperation::new);
	}

	public void ensureImplementedInterface(@Nonnull String iface) {
		ensure(new EnsureEnumInterface(iface));
	}

	@Nonnull
	public EnumConstructorLocator forConstructor() {
		return new EnumConstructorLocator(getCollector(), getNavigation());
	}

	@Nonnull
	public EnumMethodLocator forMethod() {
		return new EnumMethodLocator(getCollector(), getNavigation());
	}

	public EnumConstantScopeOperationBuilder forConstant(@Nonnull String constantName) {
		return new EnumConstantScopeOperationBuilder(getCollector(), new EnumConstantNavigation(getNavigation(), constantName));
	}

	@Nonnull
	public ClassScopeOperationBuilder.WithType ensureField(@Nonnull String name) {
		return type -> accessModifier -> {
			EnsureEnumField operation = new EnsureEnumField(name, type, accessModifier);
			ensure(operation);
			return operation;
		};
	}

	@Nonnull
	public FieldOperationBuilder forField(String name) {
		return new FieldOperationBuilder(getCollector(), getNavigation(), name);
	}
}
