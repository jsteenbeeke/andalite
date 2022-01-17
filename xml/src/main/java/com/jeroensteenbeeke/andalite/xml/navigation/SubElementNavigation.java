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
package com.jeroensteenbeeke.andalite.xml.navigation;

import com.jeroensteenbeeke.andalite.xml.IXMLNavigation;
import org.jetbrains.annotations.NotNull;

public class SubElementNavigation implements IElementNavigation {
	private final IXMLNavigation<?> parentNavigation;

	private final String elementName;

	public SubElementNavigation(IXMLNavigation<?> parentNavigation,
			String elementName) {
		super();
		this.parentNavigation = parentNavigation;
		this.elementName = elementName;
	}

	@Override
	@NotNull
	public String getXPathExpression() {
		return String.format("%s/%s", parentNavigation.getXPathExpression(),
				elementName);
	}

	@Override
	@NotNull
	public String getDescription() {
		return String.format("%s with child element <%s> ",
				parentNavigation.getDescription(), elementName);
	}

}
