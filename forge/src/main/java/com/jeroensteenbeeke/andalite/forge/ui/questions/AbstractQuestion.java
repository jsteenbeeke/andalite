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

import com.jeroensteenbeeke.andalite.forge.ui.Question;

import javax.annotation.Nullable;

public abstract class AbstractQuestion<T> implements Question {
	private final String key;

	private final String question;

	protected AbstractQuestion(String key, String question) {
		this.key = key;
		this.question = question;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getQuestion() {
		return question;
	}

	public abstract boolean isValidAnswer(@Nullable T answer);
}
