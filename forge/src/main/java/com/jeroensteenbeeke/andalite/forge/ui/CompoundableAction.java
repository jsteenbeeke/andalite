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
package com.jeroensteenbeeke.andalite.forge.ui;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.forge.ui.actions.CompoundAction;

/**
 * A Compoundable action is a performable action that can be combined with
 * another action
 * to create a CompoundAction.
 * 
 * @author Jeroen Steenbeeke
 */
public interface CompoundableAction extends PerformableAction {
	/**
	 * Combine this action with another action
	 * 
	 * @param nextAction
	 *            The action to undertake if the current action completes
	 *            successfully
	 * @return A CompoundAction that performs both the current action and the
	 *         given action
	 */
	@Nonnull
	CompoundAction andThen(@Nonnull PerformableAction nextAction);

	/**
	 * Combine this action with another action, but only if the given condition
	 * holds
	 * 
	 * @param condition
	 *            The condition under which the current action can be followed
	 *            by the given action
	 * @param nextAction
	 *            The action to undertake if current action completes
	 *            successfully, but only if the condition holds
	 * @return A CompoundAction that performs both the current action and
	 *         (optionally) the
	 *         given action
	 */
	@Nonnull
	default CompoundableAction andThenOptionally(boolean condition,
			@Nonnull PerformableAction nextAction) {
		if (!condition) {
			return this;
		}

		return andThen(nextAction);
	}
}
