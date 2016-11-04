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
package com.jeroensteenbeeke.andalite.forge.ui.actions;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;

/**
 * A CompoundAction is a set of one or more actions that will be executed in
 * sequence
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class CompoundAction extends AbstractCompoundableAction {
	private final PerformableAction[] actions;

	/**
	 * Create a new CompoundAction with the given sub-actions
	 * 
	 * @param actions
	 *            An array of actions to perform in sequence
	 */
	CompoundAction(PerformableAction... actions) {
		this.actions = actions;
	}

	@Override
	public ActionResult perform() {
		for (PerformableAction action : actions) {
			ActionResult result = action.perform();
			if (!result.isOk()) {
				return result;
			}
		}

		return ActionResult.ok();
	}
}
