package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IConstructorOperation;

import org.jetbrains.annotations.NotNull;

public class AddConstructorParameterOperation implements IConstructorOperation {
	private final String identifier;

	private final String type;

	AddConstructorParameterOperation(String identifier, String type) {
		this.identifier = identifier;
		this.type = type;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedConstructor input)
			throws OperationException {
		if (!hasParameter(input)) {
				String prefix = input.getParameters().isEmpty() ? "" : ", ";

				return ImmutableList.of(input.insertAt(AnalyzedConstructor.ConstructorInsertionPoint.AFTER_LAST_PARAMETER,
						String.format("%s%s %s", prefix, type, identifier)).whichInvalidatesNavigation());
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedConstructor input) {
		return hasParameter(input) ? ActionResult.ok() : ActionResult.error(
				"Parameter %s of type %s not present", identifier, type);
	}

	private boolean hasParameter(AnalyzedConstructor input) {
		for (AnalyzedParameter analyzedParameter : input.getParameters()) {
			if (analyzedParameter.getType().equals(type)
					&& analyzedParameter.getName().equals(identifier)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		return String.format(
				"Ensure constructor has parameter named %s of type %s",
				identifier, type);
	}

}
