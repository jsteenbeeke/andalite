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
package com.jeroensteenbeeke.andalite.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.TempFileCleaningTest;

public class XMLTest extends TempFileCleaningTest {
	private static final XPath xpath = XPathFactory.newInstance().newXPath();
	private static final DocumentBuilder DOCUMENT_BUILDER = createDocumentBuilder();

	protected List<Node> extractNodes(File xmlFile, String xpathExpression)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		Document doc = DOCUMENT_BUILDER.parse(xmlFile);
		XPathExpression expression = xpath.compile(xpathExpression);
		NodeList nodes = (NodeList) expression.evaluate(doc,
				XPathConstants.NODESET);

		List<Node> rv = Lists.newArrayListWithExpectedSize(nodes.getLength());

		for (int i = 0; i < nodes.getLength(); i++) {
			rv.add(nodes.item(i));
		}

		return rv;
	}

	protected static DocumentBuilder createDocumentBuilder() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ErrorHandler() {

				@Override
				public void warning(SAXParseException exception)
						throws SAXException {
					exception.printStackTrace();

				}

				@Override
				public void fatalError(SAXParseException exception)
						throws SAXException {
					exception.printStackTrace();

				}

				@Override
				public void error(SAXParseException exception)
						throws SAXException {
					exception.printStackTrace();

				}
			});

			return builder;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
}
