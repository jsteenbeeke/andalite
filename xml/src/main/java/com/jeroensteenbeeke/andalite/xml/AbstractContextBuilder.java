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

import javax.annotation.Nonnull;


public abstract class AbstractContextBuilder<T extends IXMLOperation> implements IStepCollector, IOperationReceiver<T> {
	private final IStepCollector collector;
	
	private final IXMLNavigation navigation;

	protected AbstractContextBuilder(@Nonnull IStepCollector collector, @Nonnull IXMLNavigation navigation) {
		super();
		this.collector = collector;
		this.navigation = navigation;
	}
	
	@Override
	public void ensure(@Nonnull T operation) {
		addStep(new XMLRecipeStep(navigation, operation));
	}
	
	@Nonnull 
	public IXMLNavigation getNavigation() {
		return navigation;
	}
	
	@Override
	public void addStep(@Nonnull XMLRecipeStep step) {
		collector.addStep(step);
	}
}
