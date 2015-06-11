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

public class EnsureAttribute implements IElementOperation {
	private final String attributeName;

	private final String value;

	public EnsureAttribute(String attributeName, String value) {
		super();
		this.attributeName = attributeName;
		this.value = value;
	}

	@Override
	public String toXSLTTemplate() {
		StringBuilder xslt = new StringBuilder();
		xslt.append("\t\t\t<xsl:copy>\n");
		xslt.append("\t\t\t\t<xsl:if test=\"not(@").append(attributeName)
				.append(")\">\n");

		xslt.append("\t\t\t\t\t<xsl:attribute name=\"").append(attributeName)
				.append("\">\n");
		xslt.append("\t\t\t\t\t\t<xsl:value-of select=\"'").append(value)
				.append("'\"/>\n");
		xslt.append("\t\t\t\t\t</xsl:attribute>\n");
		xslt.append("\t\t\t\t</xsl:if>\n");
		xslt.append("\t\t\t<xsl:apply-templates select=\"@* | node()\"/>\n");
		xslt.append("\t\t</xsl:copy>\n");

		return xslt.toString();
	}

	@Override
	public String getDescription() {
		return String.format("Ensure that attribute %s is %s", attributeName,
				value);
	}

}
