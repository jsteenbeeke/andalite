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

package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.transformation.navigation.InnerClassNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.IClassOperation;

public class ClassScopeOperationBuilder extends
		AbstractOperationBuilder<AnalyzedClass, IClassOperation> {

	ClassScopeOperationBuilder(IStepCollector collector,
			INavigation<AnalyzedClass> navigation) {
		super(collector, navigation);
	}

	public FieldOperationBuilder forField(String name) {
		return new FieldOperationBuilder(getCollector(), getNavigation(), name);
	}

	public ClassScopeOperationBuilder forInnerClass(String name) {
		return new ClassScopeOperationBuilder(getCollector(),
				new InnerClassNavigation(getNavigation(), name));
	}

	public AnnotatableOperationBuilder<AnalyzedClass> forAnnotation(String type) {
		return new AnnotatableOperationBuilder<AnalyzedClass>(getCollector(),
				getNavigation(), type);
	}

	public MethodLocator forMethod() {
		return new MethodLocator(getCollector(), getNavigation());
	}
}
