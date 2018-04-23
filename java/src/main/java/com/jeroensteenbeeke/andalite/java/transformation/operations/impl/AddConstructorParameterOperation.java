package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IConstructorOperation;

public class AddConstructorParameterOperation implements IConstructorOperation {
	private final String identifier;

	private final String type;

	public AddConstructorParameterOperation(String identifier, String type) {
		this.identifier = identifier;
		this.type = type;
	}

	@Override
	public List<Transformation> perform(AnalyzedConstructor input)
			throws OperationException {
		if (!hasParameter(input)) {
			List<AnalyzedParameter> parameters = input.getParameters();
			if (parameters.isEmpty()) {
				return ImmutableList.of(Transformation.insertAfter(
						input.getParametersStartLocation(),
						String.format("%s %s", type, identifier)));
			} else {
				AnalyzedParameter lastParam = parameters
						.get(parameters.size() - 1);

				return ImmutableList.of(Transformation.insertAfter(lastParam,
						String.format(", %s %s", type, identifier)));
			}
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(AnalyzedConstructor input) {
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
