package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.lux.ActionResult;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRecipe implements ForgeRecipe {
	private final QuestionTemplate<?,?> template;

	private final List<RecipeAssertion> assertions = new ArrayList<>();

	public TestRecipe(QuestionTemplate<?, ?> template) {
		this.template = template;
	}

	public TestRecipe expecting(@NotNull String key, @NotNull String answer) {
		assertions.add(new AnswerAssertion<>(key, answer, Answers::getString));
		return this;
	}

	public TestRecipe expecting(@NotNull String key, @NotNull Integer answer) {
		assertions.add(new AnswerAssertion<>(key, answer, Answers::getInteger));
		return this;
	}

	public TestRecipe expecting(@NotNull String key, @NotNull Boolean answer) {
		assertions.add(new AnswerAssertion<>(key, answer, Answers::getBoolean));
		return this;
	}

	public TestRecipe notExpecting(@NotNull String key) {
		assertions.add(new NoAnswerAssertion(key));
		return this;
	}

	@NotNull
	@Override
	public ActionResult checkCorrectlyConfigured() {
		return ActionResult.ok();
	}

	@NotNull
	@Override
	public String getName() {
		return "Test recipe";
	}

	@NotNull
	@Override
	public QuestionTemplate<?, ?> getInitialQuestion() {
		return template;
	}

	@NotNull
	@Override
	public Action createAction(@NotNull Answers answers) {
		return (PerformableAction) () -> {
			assertions.forEach(a -> a.assertMatches(answers));

			return ActionResult.ok();
		};
	}

	private interface RecipeAssertion {
		void assertMatches(Answers answers);
	}

	private static class NoAnswerAssertion implements RecipeAssertion {
		private final String key;

		private NoAnswerAssertion(String key) {
			this.key = key;
		}

		@Override
		public void assertMatches(Answers answers) {
			assertFalse(answers.hasAnswer(key), "Unexpected answer for key "+ key);
		}
	}

	private static class AnswerAssertion<T> implements RecipeAssertion {
		private final String key;

		private final T expectedAnswer;

		private final BiFunction<Answers,String,T> getAnswer;

		private AnswerAssertion(String key, T expectedAnswer, BiFunction<Answers, String, T> getAnswer) {
			this.key = key;
			this.expectedAnswer = expectedAnswer;
			this.getAnswer = getAnswer;
		}

		@Override
		public void assertMatches(Answers answers) {
			assertTrue(answers.hasAnswer(key), "No answer with key "+ key);
			assertThat("Answer is not valid", getAnswer.apply(answers, key), equalTo(expectedAnswer));
		}
	}
}
