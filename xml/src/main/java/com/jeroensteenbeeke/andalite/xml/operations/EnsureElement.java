package com.jeroensteenbeeke.andalite.xml.operations;

import java.util.Map;
import java.util.Map.Entry;

import com.jeroensteenbeeke.andalite.xml.IElementOperation;

public class EnsureElement implements IElementOperation {
	private final String elementName;

	private final Map<String, String> initialAttributes;

	public EnsureElement(String elementName,
			Map<String, String> initialAttributes) {
		super();
		this.elementName = elementName;
		this.initialAttributes = initialAttributes;
	}

	@Override
	public String toXSLTTemplate() {
		StringBuilder xslt = new StringBuilder();

		xslt.append("\t\t\t<xsl:copy>\n");
		xslt.append("\t\t\t\t<xsl:apply-templates select=\"@* | node()\"/>\n");
		xslt.append("\t\t\t\t<xsl:if test=\"not(/").append(elementName);
		if (!initialAttributes.isEmpty()) {
			xslt.append("[");

			int i = 0;
			for (Entry<String, String> entry : initialAttributes.entrySet()) {
				if (i++ > 0) {
					xslt.append(" and ");
				}

				xslt.append(entry.getKey()).append("='")
						.append(entry.getValue()).append("'");
			}

			xslt.append("]");
		}
		xslt.append(")\">\n");

		xslt.append("\t\t\t\t\t<xsl:element name=\"").append(elementName)
				.append("\">\n");

		if (!initialAttributes.isEmpty()) {
			for (Entry<String, String> entry : initialAttributes.entrySet()) {
				xslt.append("\t\t\t\t\t\t<xsl:attribute name=\"")
						.append(entry.getKey()).append("\">")
						.append(entry.getValue()).append("</xsl:attribute>\n");
			}
		}

		xslt.append("\t\t\t\t\t</xsl:element>\n");
		xslt.append("\t\t\t\t</xsl:if>\n");
		xslt.append("\t\t</xsl:copy>\n");

		return xslt.toString();
	}

	@Override
	public String getDescription() {
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
