package com.jeroensteenbeeke.andalite.forge.ui.questions;

import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.Question;

public final class RetryQuestion<T> extends AbstractQuestion<T> {
	private final Question<T> originalQuestion;

	public RetryQuestion(Question<T> originalQuestion, String rejectionReason) {
		super(rejectionReason);
		this.originalQuestion = originalQuestion;
	}

	@Override
	public Action onAnswer(T answer) throws ForgeException {
		return originalQuestion;
	}
}
