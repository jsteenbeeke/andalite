package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.YesNoQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import javax.annotation.Nonnull;

public class YesNoQuestionTemplate extends QuestionTemplate<YesNoQuestionTemplate, YesNoQuestion> {
	YesNoQuestionTemplate(@Nonnull String key, @Nonnull String question) {
		super(key, question);
	}

	private YesNoQuestionTemplate(String key, String question, ImmutableList<FollowUp> followupQuestion, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
	}

	@Override
	protected YesNoQuestionTemplate newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter) {
		return new YesNoQuestionTemplate(key, question, followupQuestions, formatter);
	}

	@Override
	public YesNoQuestion toQuestion(@Nonnull Answers answers) throws ForgeException {
		return new YesNoQuestion(getKey(), getFormattedQuestion(answers));
	}
}
