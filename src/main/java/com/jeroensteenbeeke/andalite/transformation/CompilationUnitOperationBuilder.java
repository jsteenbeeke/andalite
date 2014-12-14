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

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.transformation.navigation.CompilationUnitNavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;

public class CompilationUnitOperationBuilder implements
		ScopedOperationBuilder<AnalyzedSourceFile, CompilationUnitOperation> {
	private final StepCollector collector;

	CompilationUnitOperationBuilder(StepCollector collector) {
		super();
		this.collector = collector;
	}

	public void ensure(CompilationUnitOperation compilationUnitOperation) {
		collector.addStep(CompilationUnitNavigation.getInstance(),
				compilationUnitOperation);
	}
}
