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

import com.jeroensteenbeeke.andalite.analyzer.ILocatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.IOperation;

public class AbstractOperationBuilder<T extends ILocatable, O extends IOperation<T>>
		implements IScopedOperationBuilder<T, O> {
	private final IStepCollector collector;

	private final INavigation<T> navigation;

	protected AbstractOperationBuilder(IStepCollector collector,
			INavigation<T> navigation) {
		super();
		this.collector = collector;
		this.navigation = navigation;
	}

	@Override
	public final void ensure(O operation) {
		collector.addStep(navigation, operation);
	}

	protected final IStepCollector getCollector() {
		return collector;
	}

	protected final INavigation<T> getNavigation() {
		return navigation;
	}

}
