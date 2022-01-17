package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.SimpleQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class SimpleQuestionTemplate
	extends QuestionTemplate<SimpleQuestionTemplate, SimpleQuestion> {
	private static final String MATCH_ALL = "^.*$";

	private final String pattern; // Matches all

	private final Set<String> disallowedWords;

	private final Boolean disallowCaseSensitive;

	SimpleQuestionTemplate(@NotNull String key, @NotNull String question) {
		super(key, question);
		this.pattern = MATCH_ALL;
		this.disallowCaseSensitive = null;
		this.disallowedWords = ImmutableSet.of();
	}

	private SimpleQuestionTemplate(String key, String question,
		ImmutableList<QuestionTemplate.FollowUp> followupQuestion,
		String pattern, CheckedBiFunction<Answers, String, String> formatter,
		Boolean disallowCaseSensitive, Set<String> disallowedWords) {
		super(key, question, followupQuestion, formatter);
		this.pattern = pattern;
		this.disallowCaseSensitive = disallowCaseSensitive;
		this.disallowedWords = ImmutableSet.copyOf(disallowedWords);
	}

	public SimpleQuestionTemplate matchingPattern(@NotNull String pattern) {
		return new SimpleQuestionTemplate(getKey(), getQuestion(),
			getFollowupQuestion(), pattern, getFormatter(), disallowCaseSensitive, disallowedWords);
	}

	public SimpleQuestionTemplate asJavaIdentifier() {
		return matchingPattern(
			"\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");
	}

	@Override
	protected SimpleQuestionTemplate newInstance(@NotNull String key,
		@NotNull String question,
		@NotNull ImmutableList<FollowUp> followupQuestions,
		@NotNull CheckedBiFunction<Answers, String, String> formatter) {
		return new SimpleQuestionTemplate(key, question, followupQuestions,
			pattern, formatter, disallowCaseSensitive, disallowedWords);
	}

	public SimpleQuestionTemplate withDisallowedWords(
		@NotNull Set<String> disallowedWords) {
		return new SimpleQuestionTemplate(getKey(), getQuestion(),
			getFollowupQuestion(), pattern, getFormatter(), false, disallowedWords);
	}

	public SimpleQuestionTemplate withCaseSensitiveDisallowedWords(
		@NotNull Set<String> disallowedWords) {
		return new SimpleQuestionTemplate(getKey(), getQuestion(),
			getFollowupQuestion(), pattern, getFormatter(), true, disallowedWords);
	}

	@Override
	public SimpleQuestion toQuestion(@NotNull Answers answers)
		throws ForgeException {
		return new SimpleQuestion(getKey(), getFormattedQuestion(answers))
			.matching(pattern).withDisallowedWords(disallowCaseSensitive, disallowedWords);
	}
}
