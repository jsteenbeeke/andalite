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

public class IndexNavigation implements IElementNavigation {
	private final IXMLNavigation<?> parentNavigation;

	private final int index;

	public IndexNavigation(IXMLNavigation<?> parentNavigation,
			int index) {
		super();
		this.parentNavigation = parentNavigation;
		this.index = index;
	}

	@Override
	public String getXPathExpression() {
		return String.format("(%s)[%d]", parentNavigation.getXPathExpression(),
				index);
	}

	@Override
	public String getDescription() {
		return String.format("%s with index %d ",
				parentNavigation.getDescription(), index);
	}

}
