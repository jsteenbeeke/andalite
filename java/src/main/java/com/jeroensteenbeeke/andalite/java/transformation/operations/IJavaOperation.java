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

package com.jeroensteenbeeke.andalite.java.transformation.operations;

import java.util.List;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.IOutputable;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;

public interface IJavaOperation<T extends IOutputable> {
	/**
	 * Performs the operation, giving a list of transformations to be executed
	 * 
	 * @param input
	 *            The Java file element to be transformed
	 * @return A list of operations to perform
	 * @throws OperationException
	 *             If the operation fails, for whatever reason
	 */
	List<Transformation> perform(@Nonnull T input) throws OperationException;

	/**
	 * Verifies the success of this operation, by re-analyzing the given element
	 * 
	 * @param input
	 *            The element that was supposedly transformed
	 * @return An ActionResult object, that indicated either success, or the
	 *         reason for failure
	 */
	ActionResult verify(@Nonnull T input);

	String getDescription();

	default boolean isValidJavaIdentifier(@Nonnull String candidate) {
		if (candidate.isEmpty()) {
			return false;
		}

		if (!Character.isJavaIdentifierStart(candidate.charAt(0))) {
			return false;
		}

		char[] remainingChars = candidate.substring(1).toCharArray();
		for (char c : remainingChars) {
			if (!Character.isJavaIdentifierPart(c)) {
				return false;
			}
		}

		return true;
	}
}
