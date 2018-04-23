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
import java.io.IOException;
import java.nio.file.Files;

import com.jeroensteenbeeke.hyperion.util.ActionResult;

public final class DeleteFile extends AbstractCompoundableAction {
	private final File file;

	public DeleteFile(File file) {
		super();
		this.file = file;
	}

	@Override
	public ActionResult perform() {
		if (!file.exists()) {
			return ActionResult.ok();
		} else {
			try {
				Files.delete(file.toPath());

				return ActionResult.ok();
			} catch (IOException e) {
				return ActionResult.error(e.getMessage());
			}
		}

	}
}
