/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.xml.navigation;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.jeroensteenbeeke.andalite.xml.IXMLNavigation;

public interface IElementNavigation extends IXMLNavigation<Element> {
	@Override
	@NotNull
	default Element castNode(Node node) {
		if (node instanceof Element) {
			return (Element) node;
		}

		if (node instanceof Document doc) {
			Node child = doc.getFirstChild();

			while (child != null && !(child instanceof Element)) {
				child = child.getNextSibling();
			}

			if (child instanceof Element elem) {
				return elem;
			}
		}

		throw new IllegalArgumentException(
			"Argument node is neither an element nor a document with an element as child");
	}
}
