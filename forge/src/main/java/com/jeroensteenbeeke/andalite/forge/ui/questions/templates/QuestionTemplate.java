package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.AbstractQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedPredicate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public abstract class QuestionTemplate<T extends QuestionTemplate<T, Q>, Q extends AbstractQuestion<?>> {
	private final String key;

	private final String question;

	private final ImmutableList<FollowUp> followupQuestion;

	private final CheckedBiFunction<Answers, String, String> formatter;

	protected QuestionTemplate(@Nonnull String key, @Nonnull String question) {
		this.key = key;
		this.question = question;
		this.followupQuestion = ImmutableList.of();
		this.formatter = (a,f) -> f;
	}

	protected QuestionTemplate(String key, String question, ImmutableList<FollowUp> followupQuestion, CheckedBiFunction<Answers, String, String> formatter) {
		this.key = key;
		this.question = question;
		this.followupQuestion = followupQuestion;
		this.formatter = formatter;
	}

	protected String getKey() {
		return key;
	}

	protected String getQuestion() {
		return question;
	}

	protected ImmutableList<FollowUp> getFollowupQuestion() {
		return followupQuestion;
	}

	public CheckedBiFunction<Answers, String, String> getFormatter() {
		return formatter;
	}

	protected abstract T newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter);

	public abstract Q toQuestion(@Nonnull Answers answers) throws ForgeException;

	public List<QuestionTemplate<?, ?>> getFollowUpQuestions(@Nonnull Answers answers) throws ForgeException {
		try {
			return followupQuestion
				.stream()
				.filter(f -> {
					try {
						return f.getAnswers().test(answers);
					} catch (ForgeException e) {
						throw new RuntimeException(e);
					}
				})
				.map(FollowUp::getNextQuestions)
				.flatMap(ImmutableList::stream)
				.collect(Collectors.toList());
		} catch (RuntimeException e) {
			if (e.getCause() instanceof ForgeException) {
				throw (ForgeException) e.getCause();
			} else {
				throw e;
			}
		}
	}

	public T withFormatter(@Nonnull CheckedBiFunction<Answers,String,String> formatter) {
		return newInstance(key, question, followupQuestion, formatter);
	}

	protected String getFormattedQuestion(@Nonnull Answers answers) throws ForgeException {
		return formatter.apply(answers, getQuestion());
	}

	public T andThen(QuestionTemplate<?, ?>... templates) {
		return newInstance(key, question, ImmutableList.<FollowUp>builder()
			.addAll(followupQuestion)
			.add(new FollowUp(a -> true, ImmutableList.copyOf(templates)))
			.build(), formatter);
	}

	public ThenAsk<T> whenAnswer(@Nonnull String answer) {
		return when(a -> a.hasAnswer(key) && a.getString(key).equals(answer));

	}

	public ThenAsk<T> whenAnswer(@Nonnull Integer answer) {
		return when(a -> a.hasAnswer(key) && a.getInteger(key) == answer);
	}

	public ThenAsk<T> whenAnswer(@Nonnull Boolean answer) {
		return when(a -> a.hasAnswer(key) && a.getBoolean(key) == answer);
	}

	public ThenAsk<T> when(CheckedPredicate<Answers> condition) {
		return templates -> newInstance(key, question, ImmutableList.<FollowUp>builder()
			.addAll(followupQuestion)
			.add(new FollowUp(condition, ImmutableList.copyOf(templates)))
			.build(), formatter);
	}

	public interface ThenAsk<T> {
		T thenAsk(QuestionTemplate<?, ?>... templates);
	}

	protected static final class FollowUp {
		private final CheckedPredicate<Answers> answers;

		private final ImmutableList<QuestionTemplate<?, ?>> nextQuestions;

		private FollowUp(CheckedPredicate<Answers> answers, ImmutableList<QuestionTemplate<?, ?>> nextQuestions) {
			this.answers = answers;
			this.nextQuestions = nextQuestions;
		}

		public CheckedPredicate<Answers> getAnswers() {
			return answers;
		}

		public ImmutableList<QuestionTemplate<?, ?>> getNextQuestions() {
			return nextQuestions;
		}
	}
}
