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

import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;

public class AbstractOperationBuilder<T extends ILocatable, O extends IJavaOperation<T>>
		implements IScopedOperationBuilder<T, O> {
	private final IStepCollector collector;

	private final IJavaNavigation<T> navigation;

	protected AbstractOperationBuilder(IStepCollector collector,
			IJavaNavigation<T> navigation) {
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

	protected final IJavaNavigation<T> getNavigation() {
		return navigation;
	}

}
