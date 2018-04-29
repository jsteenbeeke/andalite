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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.jeroensteenbeeke.lux.TypedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;

public class XMLTransformation<T extends Node> {
	private static final DocumentBuilderFactory dbFactory = createDocumentBuilderFactory();

	private static final TransformerFactory tfFactory = createTransformerFactory();

	private static final XPathFactory xpFactory = createXPathFactory();
	
	private static final Logger log = LoggerFactory.getLogger(XMLTransformation.class);

	private final IXMLNavigation<T> navigation;

	private final IXMLOperation<T> operation;

	private XMLTransformation(IXMLNavigation<T> navigation,
			IXMLOperation<T> operation) {
		this.navigation = navigation;
		this.operation = operation;
	}

	public TypedResult<Document> applyTo(File file) {
		try {
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			Document document = builder.parse(file);

			XPath xpath = xpFactory.newXPath();
			NodeList results = (NodeList) xpath.evaluate(
					navigation.getXPathExpression(), document,
					XPathConstants.NODESET);
			
			if (results.getLength() == 0) {
				log.warn("XML Transformation does not match any elements: {}", navigation.getXPathExpression());
			}

			for (int i = 0; i < results.getLength(); i++) {
				T node = navigation.castNode(results.item(i));
				operation.transform(node);
			}

			// Document already changed at this point, this is just the most
			// convenient way
			// to write a Document to a file

			Transformer transformer = tfFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(new DOMSource(document), new StreamResult(
					file));

			return TypedResult.ok(document);
		} catch (OperationException | ParserConfigurationException | SAXException | IOException
				| XPathExpressionException | TransformerException cause) {
			cause.printStackTrace(System.err);
			return TypedResult.fail("Could not perform transformation: %s",
					cause.getMessage());
		}

	}

	public static <T extends Node> XMLTransformation<T> create(
			IXMLNavigation<T> navigation, IXMLOperation<T> operation) {
		return new XMLTransformation<T>(navigation, operation);
	}

	private static DocumentBuilderFactory createDocumentBuilderFactory() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);

		return factory;
	}

	private static TransformerFactory createTransformerFactory() {
		TransformerFactory factory = TransformerFactory.newInstance();

		return factory;
	}

	private static XPathFactory createXPathFactory() {
		XPathFactory xpf = XPathFactory.newInstance();

		return xpf;
	}

}
