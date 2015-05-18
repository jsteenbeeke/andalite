package com.jeroensteenbeeke.andalite.xml;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.w3c.dom.Node;

import com.google.common.collect.ImmutableMap;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.xml.operations.EnsureAttribute;
import com.jeroensteenbeeke.andalite.xml.operations.EnsureElement;
import com.jeroensteenbeeke.andalite.xml.util.XMLUtil;

public class TransformationTest extends XMLTest {

	@Test
	public void testAddAttribute() throws Exception {
		File input = createCopy("src/test/resources/test.xml");
		File output = newTempFile("test-output", ".xml");

		String widgetExpression = "//widget[@id='1']";
		TypedActionResult<Transformer> result = XMLUtil.createTransformer(
				widgetExpression,
				new EnsureAttribute("scope", "test").toXSLTTemplate());

		assertTrue(result.isOk());

		Transformer transformer = result.getObject();

		transformer
				.transform(new StreamSource(input), new StreamResult(output));

		Node node = extractNode(output, widgetExpression);

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("scope");

		assertEquals("test", scopeAttribute.getNodeValue());

	}

	@Test
	public void testRepeatedAddAttribute() throws Exception {
		File input = createCopy("src/test/resources/test.xml");
		File output = newTempFile("test-output", ".xml");
		File output2 = newTempFile("test-output2", ".xml");

		String widgetExpression = "//widget[@id='1']";
		TypedActionResult<Transformer> result = XMLUtil.createTransformer(
				widgetExpression,
				new EnsureAttribute("scope", "test").toXSLTTemplate());

		assertTrue(result.isOk());

		Transformer transformer = result.getObject();

		transformer
				.transform(new StreamSource(input), new StreamResult(output));
		transformer.transform(new StreamSource(output), new StreamResult(
				output2));

		Node node = extractNode(output2, widgetExpression);

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("scope");

		assertEquals("test", scopeAttribute.getNodeValue());

	}

	@Test
	public void testAddElement() throws Exception {
		File input = createCopy("src/test/resources/test.xml");
		File output = newTempFile("test-output", ".xml");

		String widgetExpression = "//widget[@id='1']";
		TypedActionResult<Transformer> result = XMLUtil.createTransformer(
				widgetExpression,
				new EnsureElement("component", ImmutableMap.of("id", "engine"))
						.toXSLTTemplate());

		assertTrue(result.isOk());

		Transformer transformer = result.getObject();

		transformer
				.transform(new StreamSource(input), new StreamResult(output));

		Node node = extractNode(output, "//widget[@id='1']/component");

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("id");

		assertEquals("engine", scopeAttribute.getNodeValue());

	}

	@Test
	public void testRepeatedAddElement() throws Exception {
		File input = createCopy("src/test/resources/test.xml");
		File output = newTempFile("test-output", ".xml");
		File output2 = newTempFile("test-output-2-", ".xml");

		String widgetExpression = "//widget[@id='1']";
		TypedActionResult<Transformer> result = XMLUtil.createTransformer(
				widgetExpression,
				new EnsureElement("component", ImmutableMap.of("id", "engine"))
						.toXSLTTemplate());

		assertTrue(result.isOk());

		Transformer transformer = result.getObject();

		transformer
				.transform(new StreamSource(input), new StreamResult(output));
		transformer.transform(new StreamSource(output), new StreamResult(
				output2));

		Node node = extractNode(output2, "//widget[@id='1']/component");

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("id");

		assertEquals("engine", scopeAttribute.getNodeValue());

	}
}
