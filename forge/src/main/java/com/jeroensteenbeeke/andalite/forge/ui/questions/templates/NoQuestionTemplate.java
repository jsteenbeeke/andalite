package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.NoQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NoQuestionTemplate extends QuestionTemplate<NoQuestionTemplate, NoQuestion>
{
	public NoQuestionTemplate()
	{
		super(randomGarbage(), randomGarbage());
	}

	@Override
	protected NoQuestionTemplate newInstance(@NotNull String key, @NotNull String question,
			@NotNull ImmutableList<FollowUp> followupQuestions,
			@NotNull CheckedBiFunction<Answers, String, String> formatter)
	{
		return new NoQuestionTemplate();
	}

	@Override
	public NoQuestion toQuestion(@NotNull Answers answers) throws ForgeException
	{
		return new NoQuestion(getKey(), getQuestion());
	}

	private static String randomGarbage()
	{
		Random random = new Random(System.currentTimeMillis());

		byte[] array = new byte[random.nextInt(24) + 12];
		new Random().nextBytes(array);
		return new String(array, StandardCharsets.UTF_8);

	}
}
