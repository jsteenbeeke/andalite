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

import java.io.File;

import com.jeroensteenbeeke.andalite.core.ActionResult;

public final class EnsureDirectoryExists extends AbstractCompoundableAction {
	private final File file;

	public EnsureDirectoryExists(File directory) {
		super();
		this.file = directory;
	}

	@Override
	public ActionResult perform() {
		if (file.exists()) {
			if (!file.isDirectory()) {
				return ActionResult.error("File exists but is not a directory");
			}

			return ActionResult.ok();
		} else {
			if (file.mkdirs()) {
				return ActionResult.ok();
			} else {
				return ActionResult
						.error("Failed to create one or more directories");
			}
		}

	}
}
