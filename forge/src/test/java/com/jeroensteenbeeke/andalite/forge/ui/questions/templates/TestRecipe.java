package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.lux.ActionResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class TestRecipe implements ForgeRecipe {
	private final QuestionTemplate<?,?> template;

	private final List<RecipeAssertion> assertions = new ArrayList<>();

	public TestRecipe(QuestionTemplate<?, ?> template) {
		this.template = template;
	}

	public TestRecipe expecting(@Nonnull String key, @Nonnull String answer) {
		assertions.add(new AnswerAssertion<>(key, answer, Answers::getString));
		return this;
	}

	public TestRecipe expecting(@Nonnull String key, @Nonnull Integer answer) {
		assertions.add(new AnswerAssertion<>(key, answer, Answers::getInteger));
		return this;
	}

	public TestRecipe expecting(@Nonnull String key, @Nonnull Boolean answer) {
		assertions.add(new AnswerAssertion<>(key, answer, Answers::getBoolean));
		return this;
	}

	public TestRecipe notExpecting(@Nonnull String key) {
		assertions.add(new NoAnswerAssertion(key));
		return this;
	}

	@Nonnull
	@Override
	public ActionResult checkCorrectlyConfigured() {
		return ActionResult.ok();
	}

	@Nonnull
	@Override
	public String getName() {
		return "Test recipe";
	}

	@Nonnull
	@Override
	public QuestionTemplate<?, ?> getInitialQuestion() {
		return template;
	}

	@Nonnull
	@Override
	public Action createAction(@Nonnull Answers answers) {
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
			assertFalse("Unexpected answer for key "+ key, answers.hasAnswer(key));
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
			assertTrue("No answer with key "+ key, answers.hasAnswer(key));
			assertThat("Answer is not valid", getAnswer.apply(answers, key), equalTo(expectedAnswer));
		}
	}
}
