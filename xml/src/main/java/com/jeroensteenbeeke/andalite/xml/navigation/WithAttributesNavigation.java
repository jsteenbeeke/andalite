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

import java.util.Map;
import java.util.Map.Entry;

import com.jeroensteenbeeke.andalite.xml.IXMLNavigation;

public class WithAttributesNavigation implements IXMLNavigation {
	private final IXMLNavigation parentNav;

	private final Map<String, String> attributes;

	public WithAttributesNavigation(IXMLNavigation parentNav,
			Map<String, String> attributes) {
		super();
		this.parentNav = parentNav;
		this.attributes = attributes;
	}

	@Override
	public String getXPathExpression() {
		if (attributes.isEmpty()) {
			return parentNav.getXPathExpression();
		}

		StringBuilder attributeMatch = new StringBuilder();
		for (Entry<String, String> e : attributes.entrySet()) {
			if (attributeMatch.length() > 0) {
				attributeMatch.append(" and ");
			}

			attributeMatch.append("@").append(e.getKey()).append("='")
					.append(e.getValue()).append("'");
		}

		return String.format("%s[%s]", parentNav.getXPathExpression(),
				attributeMatch.toString());
	}

	@Override
	public String getDescription() {
		StringBuilder attributeMatch = new StringBuilder();
		for (Entry<String, String> e : attributes.entrySet()) {
			if (attributeMatch.length() > 0) {
				attributeMatch.append(", ");
			}

			attributeMatch.append(e.getKey()).append("=").append(e.getValue());
		}

		return String.format("%s with attributes (%s)",
				parentNav.getDescription(), attributeMatch);
	}

}
