package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnumConstant;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;

public class EnsureEnumConstantOperation implements IEnumOperation {
	private final String name;

	private final List<String> expectedParameterExpressions;

	public EnsureEnumConstantOperation(String name,
			List<String> expectedParameterExpressions) {
		this.name = name;
		this.expectedParameterExpressions = expectedParameterExpressions;
	}

	@Override
	public List<Transformation> perform(AnalyzedEnum input)
			throws OperationException {
		Optional<OperationException> validationError = checkEnum(input,
				OperationException::new, Optional::empty);

		if (validationError.isPresent()) {
			throw validationError.get();
		}

		List<AnalyzedEnumConstant> constants = input.getConstants();
		AnalyzedEnumConstant last = constants.isEmpty() ? null : constants
				.get(constants.size() - 1);

		Transformation t = null;

		if (last == null) {
			Location separatorLocation = input.getSeparatorLocation();
			final String code = String.format(
					"%s(%s)",
					name,
					expectedParameterExpressions.stream().collect(
							Collectors.joining(", ")));

			if (separatorLocation == null) {
				// Insert before body
				t = Transformation.insertAt(input.getBodyLocation().getStart(),
						code);
			} else {

				t = Transformation.insertBefore(separatorLocation, code);
			}
		} else {
			t = Transformation.insertAfter(last, String.format(
					", %s(%s)",
					name,
					expectedParameterExpressions.stream().collect(
							Collectors.joining(", "))));
		}

		return ImmutableList.of(t);
	}

	@Override
	public ActionResult verify(AnalyzedEnum input) {
		Optional<ActionResult> checked = checkEnum(input, ActionResult::error,
				() -> Optional.of(ActionResult.error(
						"Enum constant %s not found", name)));

		return checked.orElse(ActionResult.ok());
	}

	@Nonnull
	private <T> Optional<T> checkEnum(AnalyzedEnum input,
			Function<String, T> errorMessageToReturnType,
			Supplier<Optional<T>> notFound) {
		for (AnalyzedEnumConstant analyzedEnumConstant : input.getConstants()) {
			if (name.equals(analyzedEnumConstant.getDenominationName())) {
				// If parameters match, then done, if not, then error
				List<AnalyzedExpression> actualParameters = analyzedEnumConstant
						.getParameters();
				if (actualParameters.size() != expectedParameterExpressions
						.size()) {
					return Optional
							.of(errorMessageToReturnType.apply(String
									.format("Constant named %s exists, but has invalid number of parameters",
											name)));
				}

				for (int i = 0; i < actualParameters.size(); i++) {
					AnalyzedExpression analyzedExpression = actualParameters
							.get(i);

					String found = analyzedExpression.toJavaString();
					String expected = expectedParameterExpressions.get(i);
					if (!expected.equals(expected)) {
						return Optional
								.of(errorMessageToReturnType.apply(String
										.format("Constant named %s exist, but parameter %d is invalid (expected %s, found %s)",
												name, i, expected, found)));
					}
				}

				// OK!
				return Optional.empty();
			}
		}

		// Not found
		return notFound.get();
	}

	@Override
	public String getDescription() {
		return String.format(
				"Ensure presence of enum constant %s with parameters (%s)",
				name,
				expectedParameterExpressions.stream().collect(
						Collectors.joining(", ")));
	}

}
