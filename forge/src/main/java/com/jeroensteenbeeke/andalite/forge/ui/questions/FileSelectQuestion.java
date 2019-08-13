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
package com.jeroensteenbeeke.andalite.forge.ui.questions;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public abstract class FileSelectQuestion extends AbstractQuestion<File> {

	public FileSelectQuestion(String key, String question) {
		super(key, question);
	}

	public abstract List<File> getChoices();

	@Override
	public boolean isValidAnswer(@Nullable File answer) {


		return answer != null && getChoices().contains(answer);
	}

	public static class SimpleFileSelectQuestion extends FileSelectQuestion {
		private final List<File> choices;

		public SimpleFileSelectQuestion(String key, String question,
											List<File> choices) {
			super(key, question);
			this.choices = choices;
		}

		@Override
		public List<File> getChoices() {
			return choices;
		}
	}
}
