/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer;

import com.jeroensteenbeeke.andalite.java.transformation.AbstractMethodBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.VoidReturnType;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;

import org.jetbrains.annotations.NotNull;

public class GetMethodBuilder
	extends AbstractMethodBuilder<AnalyzedMethod, GetMethodBuilder> {
	private final ContainingDenomination<?, ?> containingDenomination;

	public GetMethodBuilder(ContainingDenomination<?, ?> containingDenomination) {
		super(VoidReturnType.VOID, AccessModifier.PUBLIC);
		this.containingDenomination = containingDenomination;
	}

	@Override
	@NotNull
	public AnalyzedMethod named(@NotNull String name) {
		for (AnalyzedMethod analyzedMethod : containingDenomination
			.getMethods()) {
			if (name.equals(analyzedMethod.getName())) {
				if (AnalyzeUtil.matchesSignature(analyzedMethod,
					getDescriptors())) {
					AnalyzedType returnType = analyzedMethod.getReturnType();
					final String returnTypeAsString = returnType.toJavaString();

					String expectedType = getType().toJavaString(containingDenomination);

					if (!expectedType.equals(returnTypeAsString)) {
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
