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

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.xml.navigation.AnyElementNamedNavigation;
import com.jeroensteenbeeke.andalite.xml.navigation.RootElementNavigation;

public class XMLRecipeBuilder implements IStepCollector {
	private final List<XMLRecipeStep> steps;
	
	public XMLRecipeBuilder() {
		this.steps = Lists.newLinkedList();
	}
	
	public XMLElementContextBuilder atRoot() {
		return new XMLElementContextBuilder(this, new RootElementNavigation());
	}
	
	public XMLElementContextBuilder forAnyElement(String elementName) {
		return new XMLElementContextBuilder(this, new AnyElementNamedNavigation(elementName));
	}
	
	@Override
	public void addStep(@Nonnull XMLRecipeStep step) {
		this.steps.add(step);
	}
}
