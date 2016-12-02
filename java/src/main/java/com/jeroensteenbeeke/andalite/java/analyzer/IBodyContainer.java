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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.ILocatable;

/**
 * Element that can have a body
 * 
 * @author Jeroen Steenbeeke
 */
public interface IBodyContainer extends ILocatable {
	/**
	 * Get a list of statements within the body
	 * 
	 * @return The list of statements, possibly immutable
	 */
	@Nonnull
	List<AnalyzedStatement> getStatements();

	/**
	 * Check if this body container is abstract (i.e. it could have a body but
	 * doesn't)
	 * 
	 * @return {@code true} if the element is abstract, {@code false} otherwise
	 */
	boolean isAbstract();
}
