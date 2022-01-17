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

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class AndaliteErrorListener implements ErrorListener {
	private static final Logger log = LoggerFactory
			.getLogger(AndaliteErrorListener.class);

	@Override
	public void warning(TransformerException exception) {
		log.warn("Transformer Warning: {}", exception.getMessage());

	}

	@Override
	public void error(TransformerException exception)
			throws TransformerException {
		log.warn("Transformer Error: {}", exception.getMessage());
	}

	@Override
	public void fatalError(TransformerException exception)
			throws TransformerException {
		throw exception;
	}

}
