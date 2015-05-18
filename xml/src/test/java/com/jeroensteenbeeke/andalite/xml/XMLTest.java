package com.jeroensteenbeeke.andalite.xml;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.AfterClass;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class XMLTest {
	private static final XPath xpath = XPathFactory.newInstance().newXPath();
	private static final DocumentBuilder DOCUMENT_BUILDER = createDocumentBuilder();

	private static Set<File> createdTempFiles = Sets.newHashSet();

	protected Node extractNode(File xmlFile, String xpathExpression)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {
		Document doc = DOCUMENT_BUILDER.parse(xmlFile);
		XPathExpression expression = xpath.compile(xpathExpression);
		Node node = (Node) expression.evaluate(doc, XPathConstants.NODE);
		return node;
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

	protected File createCopy(String pathname) throws IOException {
		File temp = newTempFile("test", ".xml");
		Files.copy(new File(pathname), temp);
		return temp;
	}

	protected File newTempFile(String prefix, String suffix) throws IOException {
		File temp = File.createTempFile(prefix, suffix);
		createdTempFiles.add(temp);
		return temp;
	}

	@AfterClass
	public static void removeTempFiles() {
		for (File file : createdTempFiles) {
			file.delete();
		}
	}
}
