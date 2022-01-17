package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.YesNoQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import org.jetbrains.annotations.NotNull;

public class YesNoQuestionTemplate extends QuestionTemplate<YesNoQuestionTemplate, YesNoQuestion> {
	YesNoQuestionTemplate(@NotNull String key, @NotNull String question) {
		super(key, question);
	}

	private YesNoQuestionTemplate(String key, String question, ImmutableList<FollowUp> followupQuestion, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
	}

	@Override
	protected YesNoQuestionTemplate newInstance(@NotNull String key, @NotNull String question, @NotNull ImmutableList<FollowUp> followupQuestions, @NotNull CheckedBiFunction<Answers, String, String> formatter) {
		return new YesNoQuestionTemplate(key, question, followupQuestions, formatter);
	}

	@Override
	public YesNoQuestion toQuestion(@NotNull Answers answers) throws ForgeException {
		return new YesNoQuestion(getKey(), getFormattedQuestion(answers));
	}
}
