package com.jeroensteenbeeke.andalite.xml.navigation;

import com.jeroensteenbeeke.andalite.xml.IXMLNavigation;

public class RootElementNavigation implements IXMLNavigation {

	@Override
	public String getXPathExpression() {
		return "/";
	}

	@Override
	public String getDescription() {
		return "Document root";
	}

}
