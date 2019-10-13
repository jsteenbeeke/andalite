/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.forge.ui.questions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class SimpleQuestion extends AbstractQuestion {
	private static final String MATCH_ALL = "^.*$";

	public SimpleQuestion(String key, String question) {
		super(key, question);
	}

	@Override
	public boolean isValidAnswer(@Nullable Object answer) {
		return answer instanceof String && !((String) answer).isEmpty();
	}

	public SimpleQuestion matching(@Nonnull final String pattern) {
		if (pattern.equals(MATCH_ALL)) {
			return this;
		}

		return new SimpleQuestion(getKey(), getQuestion()) {
			@Override
			public boolean isValidAnswer(@Nullable Object answer) {
				return SimpleQuestion.this.isValidAnswer(answer)
					&& answer instanceof String && ((String) answer)
					.matches(pattern);
			}
		};
	}

	public SimpleQuestion withDisallowedWords(Boolean caseSensitive,
		@Nonnull Set<String> disallowedWords) {
		if (caseSensitive == null) {
			return this;
		}

		return new SimpleQuestion(getKey(), getQuestion()) {
			@Override
			public boolean isValidAnswer(@Nullable Object answer) {
				return SimpleQuestion.this.isValidAnswer(answer)
					&& answer instanceof String && caseSensitive ?
					!disallowedWords.contains(answer) :
					answer instanceof String && disallowedWords.stream()
						.noneMatch(((String) answer)::equalsIgnoreCase);
			}
		};
	}
}
