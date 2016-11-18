/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.forge.ui.questions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;

/**
 * Utility class for quickly constructing a chain of questions that trigger
 * recipes
 * 
 * @author Jeroen Steenbeeke
 */
public class Questions {
	private Questions() {

	}

	/**
	 * Start creating a new question, whose answer will be tied to the given key
	 * 
	 * @param key
	 *            The key to tie the answer to
	 * @return A builder for creating a question
	 */
	@Nonnull
	public static QuestionStage forKey(@Nonnull String key) {
		return new QuestionStage(key);
	}

	/**
	 * Answer storage class. Essentially a wrapper around java.util.Map that
	 * allows
	 * for extra type-checking.
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class Answers {
		private final Map<String, Object> answers;

		private Answers() {
			this.answers = Maps.newLinkedHashMap();
		}

		/**
		 * Return the answer with the given key
		 * 
		 * @param type
		 *            The expected type of the answer
		 * @param key
		 *            The key the answer is tied to
		 * @return The given answer, cast to the given type
		 * @throws IllegalStateException
		 *             If the answer is not of the given type
		 * @throws IllegalArgumentException
		 *             If no answer is registered with the given key
		 */
		@SuppressWarnings("unchecked")
		@Nonnull
		public <T> T getAnswer(@Nonnull Class<T> type, @Nonnull String key) {
			if (answers.containsKey(key)) {
				Object object = answers.get(key);

				if (!type.isAssignableFrom(object.getClass())) {
					throw new IllegalStateException("Answer with key " + key
							+ " not of type " + type.getName() + " but of type "
							+ object.getClass().getName());
				}

				return (T) object;
			}

			throw new IllegalArgumentException(
					"No answer registered with key " + key);
		}

		/**
		 * Add an answer with the given key
		 * 
		 * @param key
		 *            The key with which to find the answer
		 * @param answer
		 *            The answer to add
		 * @throws IllegalArgumentException
		 *             If there is already an answer registered to the given key
		 */
		private void register(@Nonnull String key, @Nonnull Object answer) {
			if (answers.containsKey(key)) {
				throw new IllegalArgumentException("Duplicate key " + key);
			}
			answers.put(key, answer);

		}
	}

	/**
	 * DSL builder stage for adding a question to a given key
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class QuestionStage {
		private final String key;

		private final List<QuestionInitializer> initializers;

		/**
		 * Create the stage for the given key
		 * 
		 * @param key
		 *            The key to tie the answer to
		 */
		private QuestionStage(@Nonnull String key) {
			this.key = key;
			this.initializers = Lists.newArrayList();
		}

		/**
		 * Create the stage for the given key, with the given initiators for
		 * previous questions
		 * 
		 * @param key
		 *            The key to tie the answer to
		 * @param initializers
		 *            The list of questions that have already been defined
		 */
		private QuestionStage(@Nonnull String key,
				@Nonnull List<QuestionInitializer> initializers) {
			this.key = key;
			this.initializers = initializers;
		}

		/**
		 * Set the question to ask for the given key
		 * 
		 * @param question
		 *            The key
		 * @return A builder object for specifying the type of question
		 */
		@Nonnull
		public TypeStage ask(@Nonnull String question) {
			return new TypeStage(initializers, key, question);
		}
	}

	/**
	 * DSL builder stage for specifying the question type
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class TypeStage {

		private final List<QuestionInitializer> initializers;

		private final String key;

		private final String question;

		/**
		 * Create a new type stage for the given key and question, with optional
		 * list
		 * of previous questions
		 * 
		 * @param initializers
		 *            Initiators for previous questions
		 * @param key
		 *            The key this question's answer is tied to
		 * @param question
		 *            The question to ask
		 */
		private TypeStage(@Nonnull List<QuestionInitializer> initializers,
				@Nonnull String key, @Nonnull String question) {
			this.initializers = initializers;
			this.key = key;
			this.question = question;
		}

		/**
		 * Create a question with a simple (String) answer
		 * 
		 * @return The next builder stage
		 */
		@Nonnull
		public FinalizerStage withSimpleAnswer() {
			return new FinalizerStage(initializers, key, question,
					(q, a, s) -> {
						return new SimpleQuestion(q) {

							@Override
							public Action onAnswer(String answer,
									FeedbackHandler handler)
									throws ForgeException {

								return s.apply(answer);
							}
						};
					});
		}

		/**
		 * Create a question with a String answer that must match the given
		 * pattern
		 *
		 * @param pattern
		 *            The pattern to which the question must adhere
		 * @return The next builder stage
		 * @see java.util.regex.Pattern
		 */
		@Nonnull
		public FinalizerStage withSimpleAnswerMatching(
				@Nonnull String pattern) {
			return new FinalizerStage(initializers, key, question,
					(q, a, s) -> {
						return new SimpleQuestion(q) {

							@Override
							public Action onAnswer(String answer,
									FeedbackHandler handler)
									throws ForgeException {
								if (!answer.matches(pattern)) {
									handler.error("Invalid input");
									return this;
								}

								return s.apply(answer);
							}
						};
					});
		}

		/**
		 * Create a question that has either yes or no as an answer
		 * 
		 * @return The next builder stage
		 */
		@Nonnull
		public FinalizerStage withYesNoAnswer() {
			return new FinalizerStage(initializers, key, question,
					(q, a, cf) -> {
						return new YesNoQuestion(q) {

							@Override
							public Action onAnswer(Boolean answer,
									FeedbackHandler handler)
									throws ForgeException {

								return cf.apply(answer);
							}
						};
					});
		}

		/**
		 * Create a multiple choice question with a dynamic set of available
		 * choices
		 * 
		 * @param choices
		 *            A function that determines the list of available choices
		 *            based on earlier answers
		 * @return The next builder stage
		 */
		@Nonnull
		public FinalizerStage withMultipleChoiceAnswer(
				@Nonnull Function<Answers, List<String>> choices) {
			return new FinalizerStage(initializers, key, question,
					(q, a, cf) -> {
						return new MultipleChoiceQuestion(q) {

							@Override
							public Action onAnswer(String answer,
									FeedbackHandler handler)
									throws ForgeException {

								return cf.apply(answer);
							}

							@Override
							public List<String> getChoices() {
								return choices.apply(a);
							}
						};
					});
		}

		/**
		 * Create a multiple choice question with a predetermined set of
		 * available
		 * choices
		 * 
		 * @param choices
		 *            The available choices
		 * @return The next builder stage
		 */
		@Nonnull
		public FinalizerStage withMultipleChoiceAnswer(
				@Nonnull List<String> choices) {
			return new FinalizerStage(initializers, key, question,
					(q, a, cf) -> {
						return new MultipleChoiceQuestion(q) {

							@Override
							public Action onAnswer(String answer,
									FeedbackHandler handler)
									throws ForgeException {

								return cf.apply(answer);
							}

							@Override
							public List<String> getChoices() {
								return choices;
							}
						};
					});
		}

		/**
		 * Create a custom question type
		 * 
		 * @param nextQuestion
		 *            A factory for creating the next question. This is a
		 *            functional interface
		 *            so can be written as a Lambda-expression
		 * @return The next builder stage
		 */
		@Nonnull
		public FinalizerStage withCustomAnswer(
				@Nonnull QuestionFactory nextQuestion) {
			return new FinalizerStage(initializers, key, question,
					nextQuestion);
		}
	}

	/**
	 * DSL builder stage for either building the set of questions, or adding
	 * another question
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	public static class FinalizerStage {
		private final List<QuestionInitializer> initializers;

		/**
		 * Create a new finalizer stage, with the given set of question
		 * initiators, and new question
		 * descriptor
		 * 
		 * @param initializers
		 *            The list of previous questions
		 * @param key
		 *            The key to tie to the current question's answer
		 * @param question
		 *            The question to ask
		 * @param nextQuestionFactory
		 *            The factory to instantiate the next question
		 */
		private FinalizerStage(@Nonnull List<QuestionInitializer> initializers,
				@Nonnull String key, @Nonnull String question,
				@Nonnull QuestionFactory nextQuestionFactory) {
			this.initializers = initializers;
			initializers.add(new QuestionInitializer(key, question,
					nextQuestionFactory));
		}

		/**
		 * Add another question, with the given key
		 * 
		 * @param key
		 *            The key to tie the answer to
		 * @return A builder for creating a question
		 */
		@Nonnull
		public QuestionStage andForKey(@Nonnull String key) {
			return new QuestionStage(key, initializers);
		}

		/**
		 * Finalize the set of questions. The parameter given is a function that
		 * specifies the action to take based on the given answers once all
		 * questions have been answered. Generally
		 * speaking, this will
		 * be one or more actions that perform a transformation. For example:
		 * 
		 * <pre>
		 * {@code
		 * 		return new JavaTransformation(targetFile, javaRecipe)
		 * 			.andThen(new JavaTransformation(targetFile2, javaRecipe2))
		 * 			.andThen(CreateFile.emptyXMLFile(targetFile3).withRootElement("example"))
		 * 			.andThen(new XMLTransformation(targetFile3, javaRecipe3));
		 * }
		 * </pre>
		 * 
		 * @param onAllAnswersGiven
		 *            A function that creates the resulting action based on the
		 *            given
		 *            answers
		 * @return The first action as specified by the builder just completed
		 */
		@Nonnull
		public Action andThen(
				@Nonnull Function<Answers, Action> onAllAnswersGiven) {
			QuestionInitializer initiator = initializers.remove(0);

			return initiator.initiate(initializers, onAllAnswersGiven);
		}
	}

	/**
	 * Helper class for creating question objects
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	private static class QuestionInitializer {
		private final String key;

		private final String question;

		private final QuestionFactory questionFactory;

		/**
		 * Create a new initializer for the given key, question and factory
		 * 
		 * @param key
		 *            The key to tie the answer to
		 * @param question
		 *            The question to ask
		 * @param questionFactory
		 *            The factory to create the question instance
		 */
		private QuestionInitializer(@Nonnull String key,
				@Nonnull String question,
				@Nonnull QuestionFactory questionFactory) {
			this.key = key;
			this.question = question;
			this.questionFactory = questionFactory;
		}

		/**
		 * Create the next action based on the given list of available
		 * initializers and finalizer step
		 * 
		 * @param availableInitializers
		 *            The list of available initializers
		 * @param onAllAnswersGiven
		 *            The function to transform answers to a next action
		 * @return The next action to perform
		 */
		@Nonnull
		public Action initiate(
				@Nonnull List<QuestionInitializer> availableInitializers,
				@Nonnull Function<Answers, Action> onAllAnswersGiven) {
			return instantiate(new Answers(), availableInitializers,
					onAllAnswersGiven);
		}

		/**
		 * Create the next action based on the given list of available
		 * initializers and finalizer step
		 * 
		 * @param answers
		 *            The answers given so far
		 * @param availableInitializers
		 *            The list of available initializers
		 * @param onAllAnswersGiven
		 *            The function to transform answers to a next action
		 * @return The next action to perform
		 */
		@Nonnull
		private Action instantiate(@Nonnull Answers answers,
				@Nonnull List<QuestionInitializer> availableInitializers,
				@Nonnull Function<Answers, Action> onAllAnswersGiven) {

			return questionFactory.initialize(question, answers, s -> {
				// Register the given answer
				answers.register(key, s);

				// If there are no more initializers, then run the callback for
				// all answers given
				if (availableInitializers.isEmpty()) {
					return onAllAnswersGiven.apply(answers);
				}
				// If there are more initializers, get the next one
				QuestionInitializer initializer = availableInitializers
						.remove(0);

				// And use it to instantiate the next question
				return initializer.instantiate(answers, availableInitializers,
						onAllAnswersGiven);

			});
		}

	}

	/**
	 * Factory for the creation of questions
	 * 
	 * @author Jeroen Steenbeeke
	 */
	@FunctionalInterface
	public interface QuestionFactory {
		/**
		 * Create an action based on the given question and previous answers
		 * 
		 * @param question
		 *            The question to ask
		 * @param previousAnswers
		 *            All answers given until this point
		 * @param onAnswerCallback
		 *            A callback mechanism for registering answers and invoking
		 *            the next question. Once
		 *            a question has been properly answered its answer should be
		 *            passed to this finalizer, and its result returned as this
		 *            method return value
		 * @return In case of success: the result of
		 *         {@code chainFinalizer.apply}, otherwise: any further action
		 *         that can be taken instead (usually graceful termination by
		 *         returning
		 *         {@code com.jeroensteenbeeke.andalite.forge.ui.actions.Failure}).
		 */
		@Nonnull
		Action initialize(@Nonnull String question,
				@Nonnull Answers previousAnswers,
				@Nonnull Function<Object, Action> onAnswerCallback);
	}
}
