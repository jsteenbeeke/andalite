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

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ForgeRecipe {
	@NotNull
	ActionResult checkCorrectlyConfigured();

	@NotNull
	String getName();

	@NotNull
	QuestionTemplate<?, ?> getInitialQuestion() throws ForgeException;

	@NotNull
	Action createAction(@NotNull Answers answers);

	class ExitRecipe implements ForgeRecipe {

		private static final ExitRecipe INSTANCE = new ExitRecipe();

		public static ExitRecipe instance() {
			return INSTANCE;
		}

		@NotNull
		@Override
		public ActionResult checkCorrectlyConfigured() {
			throw new UnsupportedOperationException("Not implemented");
		}

		@NotNull
		@Override
		public String getName() {
			throw new UnsupportedOperationException("Not implemented");
		}

		@NotNull
		@Override
		public QuestionTemplate<?, ?> getInitialQuestion() {
			throw new UnsupportedOperationException("Not implemented");
		}

		@NotNull
		@Override
		public Action createAction(@NotNull Answers answers) {
			throw new UnsupportedOperationException("Not implemented");
		}
	}
}
