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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.xml.util.XMLUtil;

public class XMLRecipeStep<N extends Node> {
	private final IXMLNavigation<N> navigation;

	private final IXMLOperation<N> operation;

	public XMLRecipeStep(IXMLNavigation<N> navigation,
			IXMLOperation<N> operation) {
		super();
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult perform(File file) {
		TypedActionResult<Document> documentResult = XMLUtil.readFile(file);
		if (!documentResult.isOk()) {
			return documentResult;
		}

		XMLTransformation<? extends Node> transformation = XMLTransformation
				.create(navigation, operation);

		return transformation.applyTo(file);
	}

	@Override
	public String toString() {
		return String.format("Go to %s and then ensure %s",
				navigation.getDescription(), operation.getDescription());
	}
}
