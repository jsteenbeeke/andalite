/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer;

import com.jeroensteenbeeke.andalite.java.transformation.AbstractMethodBuilder;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

public class GetMethodBuilder extends
		AbstractMethodBuilder<AnalyzedMethod, GetMethodBuilder> {
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
