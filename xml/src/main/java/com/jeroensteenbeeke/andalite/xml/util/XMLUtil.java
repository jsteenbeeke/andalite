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
package com.jeroensteenbeeke.andalite.xml.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.jeroensteenbeeke.lux.TypedResult;

public final class XMLUtil {
	private static final DocumentBuilderFactory documentBuilderFactory = createDocumentBuilderFactory();

	private static final TransformerFactory transformerFactory = createTransformerFactory();

	private XMLUtil() {

	}

	private static TransformerFactory createTransformerFactory() {
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setErrorListener(new AndaliteErrorListener());

		return factory;
	}

	public static TypedResult<Document> readFile(File file) {
		try {
			DocumentBuilder builder = createBuilder();
			return TypedResult.ok(builder.parse(file));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			return TypedResult.fail(e.getMessage());

		}
	}

	protected static DocumentBuilder createBuilder()
			throws ParserConfigurationException {
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		builder.setErrorHandler(new AndaliteReadXMLErrorHandler());
		return builder;
	}

	private static DocumentBuilderFactory createDocumentBuilderFactory() {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		// Assume default processing is too strict
		f.setValidating(false);
		f.setIgnoringComments(true);
		f.setNamespaceAware(true);

		return f;
	}

	public static TypedResult<Transformer> createTransformer(
			String xpathExpression, String transformation) {
		final String stylesheet = createXSLT(xpathExpression, transformation);

		StreamSource source = new StreamSource(new StringReader(
				stylesheet.toString()));
		try {
			return TypedResult.ok(transformerFactory
					.newTransformer(source));
		} catch (TransformerConfigurationException e) {
			return TypedResult.fail(e.getMessage());
		}
	}

	public static String createXSLT(String xpathExpression,
			String transformation) {
		StringBuilder stylesheet = new StringBuilder();

		stylesheet.append("<?xml version='1.0'?>\n");
		stylesheet.append("<xsl:stylesheet version=\"1.0\"\n");
		stylesheet
				.append("\txmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" >\n");
		stylesheet
				.append("\t<xsl:output omit-xml-declaration=\"no\" indent=\"yes\"/>\n\n");

		stylesheet.append("\t<xsl:template match=\"@* | node()\">\n");
		stylesheet.append("\t\t<xsl:copy>\n");
		stylesheet
				.append("\t\t\t<xsl:apply-templates select=\"@* | node()\" />\n");
		stylesheet.append("\t\t</xsl:copy>\n");
		stylesheet.append("\t</xsl:template>\n");
		stylesheet.append("\t<xsl:template match=\"").append(xpathExpression)
				.append("\">\n");

		stylesheet.append(transformation);

		stylesheet.append("\t</xsl:template>\n");

		stylesheet.append("</xsl:stylesheet>\n");
		return stylesheet.toString();
	}
}
