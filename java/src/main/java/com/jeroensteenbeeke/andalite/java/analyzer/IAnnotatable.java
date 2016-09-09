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
package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.ILocatable;

/**
 * Interface representing an element that can have annotations
 * 
 * @author Jeroen Steenbeeke
 *
 */
public interface IAnnotatable extends ILocatable {
	/**
	 * Get the annotations on this element
	 * 
	 * @return A list of the annotations on this element
	 */
	@Nonnull
	List<AnalyzedAnnotation> getAnnotations();

	/**
	 * Determines whether or not this element has an annotation of the specified
	 * type
	 * 
	 * @param type
	 *            The unqualified type (without a {@literal @} prefixed)
	 */
	boolean hasAnnotation(@Nonnull String type);

	/**
	 * Returns the annotation of the given type (if it exists)
	 * 
	 * @param type
	 *            The unqualified type (without a {@literal @} prefixed)
	 */

	@CheckForNull
	AnalyzedAnnotation getAnnotation(@Nonnull String type);
}
