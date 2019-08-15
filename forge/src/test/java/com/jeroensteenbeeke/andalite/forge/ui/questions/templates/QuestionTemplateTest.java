package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.jeroensteenbeeke.andalite.forge.ui.renderer.ScriptedQuestionRenderer;
import com.jeroensteenbeeke.andalite.forge.util.JUnitFeedbackHandler;
import org.junit.Test;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.hasError;
import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static com.jeroensteenbeeke.andalite.forge.ui.questions.templates.Questions.*;
import static org.junit.Assert.assertThat;

public class QuestionTemplateTest {
	private static final String KEY_NAME = "name";

	private static final String KEY_LAST = "last";

	private static final String KEY_GO_DEEPER = "go-deeper";

	private static final String KEY_HIPPO = "hippo";

	private static final String KEY_ELEPHANT = "elephant";

	private static final String KEY_GO_DEEPER_STILL = "go-deeper-still";

	private static final String KEY_MAKES_SENSE = "makes-sense";


	@Test
	public void testSimpleQuestionTemplate() {
		SimpleQuestionTemplate initial = simple("What is your name?").withKey(KEY_NAME)
																	 .whenAnswer("Jeroen")
																	 .thenAsk(
																		 simple("And your last name?").withKey(KEY_LAST)
																	 );

		assertThat(ScriptedQuestionRenderer
					   .forAnswers("Jeroen", "Steenbeeke")
					   .withHandler(new JUnitFeedbackHandler())
					   .execute(new TestRecipe(initial)
									.expecting(KEY_NAME, "Jeroen")
									.expecting(KEY_LAST, "Steenbeeke")), isOk());

		assertThat(ScriptedQuestionRenderer
					   .forAnswers("Piet")
					   .withHandler(new JUnitFeedbackHandler())
					   .execute(new TestRecipe(initial)
									.expecting(KEY_NAME, "Piet")
									.notExpecting(KEY_LAST)), isOk());

	}

	@Test
	public void testMultipleChoiceQuestions() {
		MultipleChoiceQuestionTemplate initial = multipleChoice("What is your name?")
			.withChoices("Steve", "Bob")
			.withKey(KEY_NAME).whenAnswer("Bob")
			.thenAsk(yesNo("Are you sure?").withKey(KEY_GO_DEEPER));

		assertThat(ScriptedQuestionRenderer
					   .forAnswers("Jeroen", true)
					   .withHandler(new JUnitFeedbackHandler())
					   .execute(new TestRecipe(initial)
									.expecting(KEY_NAME, "Jeroen")), hasError("Jeroen is not a valid answer"));

		assertThat(ScriptedQuestionRenderer
					   .forAnswers("Steve")
					   .withHandler(new JUnitFeedbackHandler())
					   .execute(new TestRecipe(initial)
									.expecting(KEY_NAME, "Steve")
									.notExpecting(KEY_GO_DEEPER)), isOk());
		assertThat(ScriptedQuestionRenderer
					   .forAnswers("Bob", true)
					   .withHandler(new JUnitFeedbackHandler())
					   .execute(new TestRecipe(initial)
									.expecting(KEY_NAME, "Bob")
									.expecting(KEY_GO_DEEPER, true)), isOk());

	}

	@Test
	public void nestedChainTest() {
		YesNoQuestionTemplate initial = yesNo("Do you want to go deeper?")
			.withKey(KEY_GO_DEEPER)
			.whenAnswer(true)
			.thenAsk(
				yesNo("Are you a hippopotamus?").withKey(KEY_HIPPO),
				yesNo("Are you an elephant?").withKey(KEY_ELEPHANT),
				yesNo("Do you want to go deeper still?").withKey(KEY_GO_DEEPER_STILL)
														.andThen(
															simple("Are these questions making sense?").withKey(KEY_MAKES_SENSE)
														)
			);

		assertThat(ScriptedQuestionRenderer
					   .forAnswers(true, false, false, true, "Banana")
					   .withHandler(new JUnitFeedbackHandler())
					   .execute(new TestRecipe(initial)
									.expecting(KEY_GO_DEEPER, true)
									.expecting(KEY_HIPPO, false)
									.expecting(KEY_ELEPHANT, false)
									.expecting(KEY_GO_DEEPER_STILL, true)
									.expecting(KEY_MAKES_SENSE, "Banana")
					   ), isOk());
	}

}
