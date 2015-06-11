package com.jeroensteenbeeke.andalite.xml;

import java.util.Map;

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

	public XMLAttributeFilterableElementContextBuilder withAttribute(
			String name, String value) {
		attributes.put(name, value);
		return this;
	}

}
