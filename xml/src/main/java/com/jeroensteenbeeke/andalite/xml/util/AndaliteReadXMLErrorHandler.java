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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class AndaliteReadXMLErrorHandler implements ErrorHandler {
	private static final Logger log = LoggerFactory
			.getLogger(AndaliteReadXMLErrorHandler.class);

	@Override
	public void warning(SAXParseException exception) {
		log.warn("SAX Warning: {}", exception.getMessage());

	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		log.warn("SAX Error: {}", exception.getMessage());

	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		throw exception;
	}

}
