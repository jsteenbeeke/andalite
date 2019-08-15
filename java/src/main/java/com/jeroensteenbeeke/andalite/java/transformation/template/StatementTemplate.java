package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.ConstructorOperationBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.MethodOperationBuilder;

import javax.annotation.Nonnull;

public class StatementTemplate implements ConstructorElementTemplate, MethodElementTemplate{
	private final String statement;

	private final boolean verification;

	StatementTemplate(String statement) {
		this(statement, true);
	}

	private StatementTemplate(String statement, boolean verification) {
		this.statement = statement;
		this.verification = verification;
	}

	public StatementTemplate withoutVerification() {
		return new StatementTemplate(statement, false);
	}

	@Override
	public void onConstructor(JavaRecipeBuilder builder, ConstructorOperationBuilder constructorOperationBuilder) {
		if (verification) {
			constructorOperationBuilder.inBody().ensureStatement(statement);
		} else {
			constructorOperationBuilder.inBody().ensureStatement(statement).withoutVerification();
		}
	}

	@Override
	public void onMethod(@Nonnull JavaRecipeBuilder builder, @Nonnull MethodOperationBuilder methodBuilder) {
		if (verification) {
			methodBuilder.inBody().ensureStatement(statement);
		} else {
			methodBuilder.inBody().ensureStatement(statement).withoutVerification();
		}
	}
}
