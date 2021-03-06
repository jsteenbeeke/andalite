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
package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ConstructableDenominationConstructorNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ConstructableDenominationConstructorOfAnySignatureNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;

public class ClassConstructorLocator
		extends
		AbstractParameterizedBuilder<ConstructorOperationBuilder, ClassConstructorLocator> {

	private final IStepCollector collector;

	private final IJavaNavigation<AnalyzedClass> parentNavigation;

	ClassConstructorLocator(IStepCollector collector,
			IJavaNavigation<AnalyzedClass> parentNavigation) {
		super(AccessModifier.PUBLIC);
		this.collector = collector;
		this.parentNavigation = parentNavigation;

	}

	public ConstructorOperationBuilder get() {
		return new ConstructorOperationBuilder(collector,
				new ConstructableDenominationConstructorNavigation<>(parentNavigation, getModifier(),
						getDescriptors()));
	}

	public ConstructorOperationBuilder andAnySignature() {
		return new ConstructorOperationBuilder(collector,
				new ConstructableDenominationConstructorOfAnySignatureNavigation<>(parentNavigation,
						getModifier()));
	}

}
