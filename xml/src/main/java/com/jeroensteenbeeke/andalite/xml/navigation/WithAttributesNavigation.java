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
