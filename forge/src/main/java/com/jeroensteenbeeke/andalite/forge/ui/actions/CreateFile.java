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
import java.io.PrintWriter;

import com.jeroensteenbeeke.andalite.core.ActionResult;

public final class CreateFile extends AbstractCompoundableAction {
	private final File file;

	private String initialContents = null;

	public CreateFile(File file) {
		super();
		this.file = file;
	}

	public CreateFile withInitialContents(String contents) {
		this.initialContents = contents;
		return this;
	}

	@Override
	public ActionResult perform() {
		if (file.exists()) {
			return ActionResult.ok();
		} else {
			try {
				if (file.createNewFile()) {
					// Check

					if (initialContents != null) {
						PrintWriter pw = new PrintWriter(file);
						pw.print(initialContents);
						pw.flush();
						pw.close();
					}

					return ActionResult.ok();
				} else {
					return ActionResult.error("Could not create file %s",
							file.getName());
				}
			} catch (IOException e) {
				return ActionResult.error(e.getMessage());
			}
		}

	}

}
