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
package com.jeroensteenbeeke.andalite.forge;

import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.QuestionTemplate;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.lux.TypedResult;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ForgeRecipe {
	@Nonnull
	ActionResult checkCorrectlyConfigured();

	@Nonnull
	String getName();

	@Nonnull
	QuestionTemplate<?, ?> getInitialQuestion() throws ForgeException;

	@Nonnull
	Action createAction(@Nonnull Answers answers);

	class ExitRecipe implements ForgeRecipe {

		private static final ExitRecipe INSTANCE = new ExitRecipe();

		public static ExitRecipe instance() {
			return INSTANCE;
		}

		@Nonnull
		@Override
		public ActionResult checkCorrectlyConfigured() {
			throw new UnsupportedOperationException("Not implemented");
		}

		@Nonnull
		@Override
		public String getName() {
			throw new UnsupportedOperationException("Not implemented");
		}

		@Nonnull
		@Override
		public QuestionTemplate<?, ?> getInitialQuestion() {
			throw new UnsupportedOperationException("Not implemented");
		}

		@Nonnull
		@Override
		public Action createAction(@Nonnull Answers answers) {
			throw new UnsupportedOperationException("Not implemented");
		}
	}
}
