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
package com.jeroensteenbeeke.andalite.xml.operations;

import com.jeroensteenbeeke.andalite.xml.IElementOperation;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EnsureElement implements IElementOperation {

	public static class AddInitialAttribute {
		private final String elementName;

		private final String attributeName;

		private final Map<String, String> attr;

		private AddInitialAttribute(String elementName, String attributeName) {
			this.elementName = elementName;
			this.attributeName = attributeName;
			this.attr = new HashMap<>();
		}

		private AddInitialAttribute(String elementName, String attributeName,
				Map<String, String> attr) {
			this.elementName = elementName;
			this.attributeName = attributeName;
			this.attr = attr;
		}

		public Builder2 withValue(String value) {
			attr.put(attributeName, value);

			return new Builder2(elementName, attr);
		}
	}

	public static class Builder {
		private final String elementName;

		public Builder(String elementName) {
			this.elementName = elementName;
		}

		public AddInitialAttribute withInitialAttribute(String name) {
			return new AddInitialAttribute(elementName, name);
		}

		public EnsureElement withNoInitialAttributes() {
			return new EnsureElement(elementName, new HashMap<>());
		}
	}

	public static class Builder2 {
		private final String elementName;

		private final Map<String, String> initialAttributes;

		public Builder2(String elementName,
				Map<String, String> initialAttributes) {
			this.elementName = elementName;
			this.initialAttributes = initialAttributes;
		}

		public AddInitialAttribute withInitialAttribute(String name) {
			return new AddInitialAttribute(elementName, name, initialAttributes);
		}

		public EnsureElement andNothingElse() {
			return new EnsureElement(elementName, initialAttributes);
		}
	}

	private final String elementName;

	private final Map<String, String> initialAttributes;

	private EnsureElement(String elementName,
			Map<String, String> initialAttributes) {
		super();
		this.elementName = elementName;
		this.initialAttributes = initialAttributes;
	}

	@Override
	public void transform(Element node) {
		int c = 0;
		NodeList elementsByTagName = node.getElementsByTagName(elementName);
		elements: for (int i = 0; i < elementsByTagName.getLength(); i++) {
			Node item = elementsByTagName.item(i);
			NamedNodeMap attributes = item.getAttributes();

			for (Entry<String, String> entry : initialAttributes.entrySet()) {
				Node attributeNode = attributes.getNamedItem(entry.getKey());
				if (attributeNode == null
						|| !entry.getValue().equals(
								attributeNode.getNodeValue())) {
					continue elements;
				}
			}

			c++;
		}

		if (c == 0) {
			Element newElement = node.getOwnerDocument().createElement(
					elementName);

			node.appendChild(newElement);

			initialAttributes.forEach((k, v) -> {
				newElement.setAttribute(k, v);
			});
		}
	}

	@Override
	public @NotNull String getDescription() {
		return String.format("Ensure that there exists an element %s%s",
				elementName, formatAttributes());
	}

	private String formatAttributes() {
		if (initialAttributes.isEmpty()) {
			return "";
		}

		if (initialAttributes.size() == 1) {
			Entry<String, String> entry = initialAttributes.entrySet()
					.iterator().next();
			return String.format(" with attribute %s='%s'", entry.getKey(),
					entry.getValue());
		}

		StringBuilder attr = new StringBuilder(" with attributes ");

		int i = 0;
		for (Entry<String, String> entry : initialAttributes.entrySet()) {
			if (i++ > 0) {
				attr.append(", ");
			}

			attr.append(entry.getKey());
			attr.append("='");
			attr.append(entry.getValue());
			attr.append("'");
		}

		return attr.toString();
	}

}
