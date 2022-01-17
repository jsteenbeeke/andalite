package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.FileSelectQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;

public class FileSelectionQuestionTemplate extends QuestionTemplate<FileSelectionQuestionTemplate, FileSelectQuestion> {

	private final ImmutableList<File> choices;

	private final QuestionCreator questionCreator;

	FileSelectionQuestionTemplate(@NotNull String key, @NotNull String question) {
		super(key, question);
		this.choices = ImmutableList.of();
		this.questionCreator = FileSelectQuestion.SimpleFileSelectQuestion::new;
	}

	private FileSelectionQuestionTemplate(String key,
										  String question,
										  ImmutableList<FollowUp> followupQuestion,
										  ImmutableList<File> choices,
										  QuestionCreator questionCreator, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.choices = choices;
		this.questionCreator = questionCreator;
	}

	public FileSelectionQuestionTemplate withChoices(@NotNull File... choices) {
		return new FileSelectionQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(),
												 ImmutableList.<File>builder()
													  .addAll(this.choices)
													  .addAll(ImmutableList.copyOf(choices))
													  .build(),
												 questionCreator,
												 getFormatter());
	}

	@Override
	protected FileSelectionQuestionTemplate newInstance(@NotNull String key, @NotNull String question, @NotNull ImmutableList<FollowUp> followupQuestions, @NotNull CheckedBiFunction<Answers, String, String> formatter) {
		return new FileSelectionQuestionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}

	public FileSelectionQuestionTemplate withQuestionCreator(@NotNull QuestionCreator questionCreator) {
		return new FileSelectionQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		FileSelectQuestion createQuestion(@NotNull String key, @NotNull String question, @NotNull List<File> choices);
	}

	@Override
	public FileSelectQuestion toQuestion(@NotNull Answers answers) throws ForgeException {
		return questionCreator.createQuestion(getKey(), getFormattedQuestion(answers), choices);
	}
}
