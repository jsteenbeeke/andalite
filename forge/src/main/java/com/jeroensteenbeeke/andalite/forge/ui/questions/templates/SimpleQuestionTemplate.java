package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.SimpleQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import javax.annotation.Nonnull;

public class SimpleQuestionTemplate extends QuestionTemplate<SimpleQuestionTemplate, SimpleQuestion> {
	private static final String MATCH_ALL = "^.*$";

	private final String pattern; // Matches all

	SimpleQuestionTemplate(@Nonnull String key, @Nonnull String question) {
		super(key, question);
		this.pattern = MATCH_ALL;
	}

	private SimpleQuestionTemplate(String key, String question, ImmutableList<QuestionTemplate.FollowUp> followupQuestion, String pattern, CheckedBiFunction<Answers,String,String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.pattern = pattern;
	}

	public SimpleQuestionTemplate matchingPattern(@Nonnull String pattern) {
		return new SimpleQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), pattern, getFormatter());
	}

	public SimpleQuestionTemplate asJavaIdentifier() {
		return matchingPattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");
	}

	@Override
	protected SimpleQuestionTemplate newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter) {
		return new SimpleQuestionTemplate(key, question, followupQuestions, pattern, formatter);
	}



	@Override
	public SimpleQuestion toQuestion(@Nonnull Answers answers) throws ForgeException {
		return new SimpleQuestion(getKey(), getFormattedQuestion(answers)).matching(pattern);
	}
}
