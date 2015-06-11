package com.jeroensteenbeeke.andalite.xml.navigation;

import com.jeroensteenbeeke.andalite.xml.IXMLNavigation;

public class SubElementNavigation implements IXMLNavigation {
	private final IXMLNavigation parentNavigation;

	private final String elementName;

	public SubElementNavigation(IXMLNavigation parentNavigation,
			String elementName) {
		super();
		this.parentNavigation = parentNavigation;
		this.elementName = elementName;
	}

	@Override
	public String getXPathExpression() {
		return String.format("%s/%s", parentNavigation.getXPathExpression(),
				elementName);
	}

	@Override
	public String getDescription() {
		return String.format("%s with child element <%s> ",
				parentNavigation.getDescription(), elementName);
	}

}
