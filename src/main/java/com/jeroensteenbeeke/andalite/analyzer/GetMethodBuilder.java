package com.jeroensteenbeeke.andalite.analyzer;

import com.jeroensteenbeeke.andalite.transformation.AbstractMethodBuilder;
import com.jeroensteenbeeke.andalite.util.AnalyzeUtil;

public class GetMethodBuilder extends AbstractMethodBuilder<AnalyzedMethod> {
	private final ContainingDenomination containingDenomination;

	public GetMethodBuilder(ContainingDenomination containingDenomination) {
		super("void", AccessModifier.PUBLIC);
		this.containingDenomination = containingDenomination;
	}

	@Override
	public AnalyzedMethod named(String name) {
		for (AnalyzedMethod analyzedMethod : containingDenomination
				.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod,
						getDescriptors())) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType != null ? returnType
							.toJavaString() : "void";

					if (!getType().equals(returnTypeAsString)) {
						continue;
					}

					AccessModifier modifier = getModifier();
					if (!modifier.equals(analyzedMethod.getAccessModifier())) {
						continue;
					}

					return analyzedMethod;
				}
			}
		}

		return null;
	}
}
