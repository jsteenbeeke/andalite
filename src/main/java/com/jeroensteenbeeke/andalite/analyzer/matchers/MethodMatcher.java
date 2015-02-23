package com.jeroensteenbeeke.andalite.analyzer.matchers;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.analyzer.ContainingDenomination;
import com.jeroensteenbeeke.andalite.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.util.AnalyzeUtil;

public class MethodMatcher extends
		TypeSafeDiagnosingMatcher<ContainingDenomination> {
	private final AccessModifier modifier;
	private final String type;
	private final String name;
	private final List<ParameterDescriptor> descriptors;

	public MethodMatcher(AccessModifier modifier, String type, String name,
			List<ParameterDescriptor> descriptors) {
		this.modifier = modifier;
		this.type = type;
		this.name = name;
		this.descriptors = descriptors;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("denomination has method ");
		description.appendText(modifier.getOutput());
		description.appendText(" ");
		description.appendText(type);
		description.appendText(" ");
		description.appendText(name);
		description.appendText("(");
		description.appendText(Joiner.on(", ").join(
				FluentIterable.from(descriptors).transform(
						ParameterDescriptor.toStringFunction())));
		description.appendText(")");

	}

	@Override
	protected boolean matchesSafely(ContainingDenomination item,
			Description mismatchDescription) {
		int nameMatches = 0;

		for (AnalyzedMethod analyzedMethod : item.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				nameMatches++;
				if (AnalyzeUtil.matchesSignature(analyzedMethod, descriptors)) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType != null ? returnType
							.toJavaString() : "void";

					if (!type.equals(returnTypeAsString)) {
						mismatchDescription
								.appendText("method has correct signature, but expected return type ");
						mismatchDescription.appendText(type);
						mismatchDescription.appendText(", and found ");
						mismatchDescription.appendText(returnTypeAsString);

						return false;
					}

					if (!modifier.equals(analyzedMethod.getAccessModifier())) {
						mismatchDescription
								.appendText("method has correct signature, but expected access modifier ");
						mismatchDescription.appendText(modifier.getOutput());
						mismatchDescription.appendText(", and found ");
						mismatchDescription.appendText(analyzedMethod
								.getAccessModifier().getOutput());

						return false;
					}

					return true;
				}
			}
		}

		mismatchDescription.appendText("no method named ");
		mismatchDescription.appendText(name);

		if (nameMatches > 0) {
			mismatchDescription.appendText(" with expected signature");
		}

		return false;
	}
}
