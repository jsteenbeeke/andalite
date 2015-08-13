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

import static com.jeroensteenbeeke.andalite.xml.XMLOperations.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.w3c.dom.Node;

import com.jeroensteenbeeke.andalite.core.ActionResult;

public class TransformationTest extends XMLTest {

	@Test
	public void testAddAttribute() throws Exception {
		File inputOutput = createCopy("src/test/resources/test.xml");

		String widgetExpression = "//widget[@id='1']";

		XMLRecipeBuilder builder = new XMLRecipeBuilder();

		builder.forAnyElement("widget").withAttribute("id", "1")
				.ensure(hasAttribute("scope").withValue("test"));

		XMLRecipe recipe = builder.build();

		ActionResult result = recipe.applyTo(inputOutput);

		assertTrue(result.isOk());

		Node node = extractNode(inputOutput, widgetExpression);

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("scope");

		assertEquals("test", scopeAttribute.getNodeValue());

	}

	@Test
	public void testRepeatedAddAttribute() throws Exception {
		File inputOutput = createCopy("src/test/resources/test.xml");

		String widgetExpression = "//widget[@id='1']";
		XMLRecipeBuilder builder = new XMLRecipeBuilder();

		builder.forAnyElement("widget").withAttribute("id", "1")
				.ensure(hasAttribute("scope").withValue("test"));

		XMLRecipe recipe = builder.build();

		ActionResult result = recipe.applyTo(inputOutput);

		assertTrue(result.isOk());

		result = recipe.applyTo(inputOutput);

		assertTrue(result.isOk());

		Node node = extractNode(inputOutput, widgetExpression);

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("scope");

		assertEquals("test", scopeAttribute.getNodeValue());

	}

	@Test
	public void testAddElement() throws Exception {
		File inputOutput = createCopy("src/test/resources/test.xml");

		XMLRecipeBuilder builder = new XMLRecipeBuilder();

		builder.forAnyElement("widget")
				.withAttribute("id", "1")
				.ensure(hasElement("component").withInitialAttribute("id")
						.withValue("engine").andNothingElse());

		XMLRecipe recipe = builder.build();

		ActionResult result = recipe.applyTo(inputOutput);

		assertTrue(result.isOk());

		Node node = extractNode(inputOutput, "//widget[@id='1']/component");

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("id");

		assertEquals("engine", scopeAttribute.getNodeValue());

	}

	@Test
	public void testRepeatedAddElement() throws Exception {
		File inputOutput = createCopy("src/test/resources/test.xml");

		XMLRecipeBuilder builder = new XMLRecipeBuilder();

		builder.forAnyElement("widget")
				.withAttribute("id", "1")
				.ensure(hasElement("component").withInitialAttribute("id")
						.withValue("engine").andNothingElse());

		XMLRecipe recipe = builder.build();

		ActionResult result = recipe.applyTo(inputOutput);

		assertTrue(result.isOk());

		result = recipe.applyTo(inputOutput);

		assertTrue(result.isOk());

		Node node = extractNode(inputOutput, "//widget[@id='1']/component");

		assertNotNull(node);

		Node scopeAttribute = node.getAttributes().getNamedItem("id");

		assertEquals("engine", scopeAttribute.getNodeValue());

	}
}
