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
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;

public class ClassScopeOperationBuilder implements
		ScopedOperationBuilder<AnalyzedClass, ClassOperation> {

	private final StepCollector collector;

	private final Navigation<AnalyzedClass> navigation;

	ClassScopeOperationBuilder(StepCollector collector,
			Navigation<AnalyzedClass> navigation) {
		this.collector = collector;
		this.navigation = navigation;
	}

	@Override
	public void ensure(ClassOperation operation) {
		collector.addStep(navigation, operation);
	}

	public FieldOperationBuilder forField(String name) {
		return new FieldOperationBuilder(collector, navigation, name);
	}

	public ClassScopeOperationBuilder forInnerClass(String name) {
		return new ClassScopeOperationBuilder(collector,
				new InnerClassNavigation(navigation, name));
	}
}
