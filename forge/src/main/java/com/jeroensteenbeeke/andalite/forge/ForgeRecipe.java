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
package com.jeroensteenbeeke.andalite.forge;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.forge.ui.Action;

/**
 * A ForgeRecipe is a predefined sequence of actions that can be executed by the
 * {@code andalite:forge} Maven goal, given
 * the proper configuration
 * 
 * @author Jeroen Steenbeeke
 *
 */
public interface ForgeRecipe {
	/**
	 * Performs an integrity-check
	 * 
	 * @return An ActionResult indicating if the action was successful, and if
	 *         not, why
	 */
	@Nonnull
	ActionResult checkCorrectlyConfigured();

	/**
	 * Returns the name of the recipe
	 * 
	 * @return A String containing the name
	 */
	@Nonnull
	String getName();

	/**
	 * Returns the first action for this sequence
	 * 
	 * @return An {@code Action} object
	 * @throws ForgeException
	 *             If the {@code Action} could not be created
	 */
	@Nonnull
	Action onSelected() throws ForgeException;
}
