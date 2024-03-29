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
package com.jeroensteenbeeke.andalite.xml;

import org.jetbrains.annotations.NotNull;

import org.w3c.dom.Node;

public abstract class AbstractContextBuilder<N extends Node, T extends IXMLOperation<N>>
		implements IStepCollector, IOperationReceiver<N, T> {
	private final IStepCollector collector;

	private final IXMLNavigation<N> navigation;

	protected AbstractContextBuilder(@NotNull IStepCollector collector,
			@NotNull IXMLNavigation<N> navigation) {
		super();
		this.collector = collector;
		this.navigation = navigation;
	}

	@Override
	public void ensure(@NotNull T operation) {
		addStep(new XMLRecipeStep<N>(navigation, operation));
	}

	@NotNull
	public IXMLNavigation<N> getNavigation() {
		return navigation;
	}

	@Override
	public void addStep(@NotNull XMLRecipeStep<?> step) {
		collector.addStep(step);
	}
}
