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

import java.util.Map;

import org.w3c.dom.Element;

import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.xml.navigation.SubElementNavigation;
import com.jeroensteenbeeke.andalite.xml.navigation.WithAttributesNavigation;

public class XMLAttributeFilterableElementContextBuilder extends
		XMLElementContextBuilder {

	private final Map<String, String> attributes;

	public XMLAttributeFilterableElementContextBuilder(
			IStepCollector collector, IXMLNavigation navigation) {
		super(collector, navigation);
		this.attributes = Maps.newHashMap();
	}

	@Override
	public XMLAttributeFilterableElementContextBuilder forElement(String element) {
		if (attributes.isEmpty()) {
			return new XMLAttributeFilterableElementContextBuilder(this,
					new SubElementNavigation(getNavigation(), element));
		}

		return new XMLAttributeFilterableElementContextBuilder(this,
				new SubElementNavigation(new WithAttributesNavigation(
						getNavigation(), attributes), element));
	}

	@Override
	public void ensure(IElementOperation operation) {
		if (attributes.isEmpty()) {
			super.ensure(operation);
		} else {
			addStep(new XMLRecipeStep<Element>(new WithAttributesNavigation(
					getNavigation(), attributes), operation));
		}
	}

	public XMLAttributeFilterableElementContextBuilder withAttribute(
			String name, String value) {
		attributes.put(name, value);
		return this;
	}

}
