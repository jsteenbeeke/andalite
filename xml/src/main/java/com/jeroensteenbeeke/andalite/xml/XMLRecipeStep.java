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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.xml.util.XMLUtil;

public class XMLRecipeStep {
	private final Logger logger = LoggerFactory.getLogger(XMLRecipeStep.class);

	private final IXMLNavigation navigation;

	private final IXMLOperation operation;

	public XMLRecipeStep(IXMLNavigation navigation, IXMLOperation operation) {
		super();
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult perform(File file) {
		try {

			TypedActionResult<Document> documentResult = XMLUtil.readFile(file);
			if (!documentResult.isOk()) {
				return documentResult;
			}

			TypedActionResult<Transformer> transformerResult = XMLUtil
					.createTransformer(navigation.getXPathExpression(),
							operation.toXSLTTemplate());
			if (!transformerResult.isOk()) {
				return transformerResult;
			}

			Transformer transformer = transformerResult.getObject();

			transformer.transform(new StreamSource(file),
					new StreamResult(file));

			return ActionResult.ok();
		} catch (TransformerException e) {
			logger.error(e.getMessage(), e);
			return ActionResult.error(String.format(
					"Navigation (%s) failed: %s", navigation.getDescription(),
					e.getMessage()));
		}
	}

	@Override
	public String toString() {
		return String.format("Go to %s and then ensure %s",
				navigation.getDescription(), operation.getDescription());
	}
}