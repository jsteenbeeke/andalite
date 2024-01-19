package com.jeroensteenbeeke.andalite.forge.ui.questions;

import org.jetbrains.annotations.Nullable;

public class NoQuestion extends AbstractQuestion
{
	public NoQuestion(String key, String question)
	{
		super(key, question);
	}

	@Override
	public boolean isValidAnswer(@Nullable Object answer)
	{
		return true;
	}
}
