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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;

public class XMLTransformation<T extends Node> {
	private static final DocumentBuilderFactory dbFactory = createDocumentBuilderFactory();

	private static final TransformerFactory tfFactory = createTransformerFactory();

	private static final XPathFactory xpFactory = createXPathFactory();

	private final IXMLNavigation navigation;

	private final IXMLOperation<T> operation;

	private XMLTransformation(IXMLNavigation navigation,
			IXMLOperation<T> operation) {
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult applyTo(File file)  {
		try {
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			Document document = builder.parse(file);

			IXMLTransformationStep<T> step = operation.getTransformationStep();

			XPath xpath = xpFactory.newXPath();
			NodeList results = (NodeList) xpath.evaluate(
					navigation.getXPathExpression(), document,
					XPathConstants.NODESET);

			for (int i = 0; i < results.getLength(); i++) {
				@SuppressWarnings("unchecked")
				T node = (T) results.item(i);
				step.transform(node);
			}

			// Document already changed at this point, this is just the most
			// convenient way
			// to write a Document to a file
			Transformer transformer = tfFactory.newTransformer();
			transformer.transform(new DOMSource(document), new StreamResult(
					file));
			
			return ActionResult.ok();
		} catch (ParserConfigurationException | SAXException | IOException
				| XPathExpressionException | TransformerException cause) {
			return ActionResult.error("Could not perform transformation: %s", cause.getMessage());
		}

	}

	public static <T extends Node> XMLTransformation<T> create(
			IXMLNavigation navigation, IXMLOperation<T> operation) {
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
