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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;

public class Questions {
	private Questions() {

	}

	public static QuestionStage forKey(String key) {
		return new QuestionStage(key);
	}

	public static class Answers {
		private final Map<String, Object> answers;

		private Answers(Map<String, Object> answers) {
			this.answers = Maps.newLinkedHashMap();
		}

		@SuppressWarnings("unchecked")
		public <T> T getAnswer(Class<T> type, String key) {
			if (answers.containsKey(key)) {
				Object object = answers.get(key);

				if (!type.isAssignableFrom(object.getClass())) {
					throw new IllegalStateException("Answer with key " + key
							+ " not of type " + type.getName());
				}

				return (T) object;
			}

			throw new IllegalArgumentException("No answer registered with key "
					+ key);
		}

		private void register(String key, Object answer) {
			answers.put(key, answer);

		}
	}

	public static class QuestionStage {
		private final String key;

		private final List<QuestionInitiator> initiators;

		private QuestionStage(String key) {
			this.key = key;
			this.initiators = Lists.newArrayList();
		}

		private QuestionStage(String key, List<QuestionInitiator> initiators) {
			this.key = key;
			this.initiators = initiators;
		}

		public TypeStage ask(String question) {
			return new TypeStage(initiators, key, question);
		}
	}

	public static class TypeStage {

		private final List<QuestionInitiator> initiators;

		private final String key;

		private final String question;

		private TypeStage(List<QuestionInitiator> initiators, String key,
				String question) {
			this.initiators = initiators;
			this.key = key;
			this.question = question;
		}

		public FinalizerStage withSimpleAnswer() {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new SimpleQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {
						return s.apply(answer);
					}
				};
			});
		}

		public FinalizerStage withSimpleAnswerMatching(String pattern) {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new SimpleQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {
						if (!answer.matches(pattern)) {
							handler.error("Invalid input");
							return this;
						}

						return s.apply(answer);
					}
				};
			});
		}

		public FinalizerStage withYesNoAnswer() {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new YesNoQuestion(q) {

					@Override
					public Action onAnswer(Boolean answer,
							FeedbackHandler handler) throws ForgeException {
						return s.apply(answer);
					}
				};
			});
		}

		public FinalizerStage withMultipleChoiceAnswer(
				Function<Answers, List<String>> choices) {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new MultipleChoiceQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {
						return s.apply(answer);
					}

					@Override
					public List<String> getChoices() {
						return choices.apply(a);
					}
				};
			});
		}

		public FinalizerStage withMultipleChoiceAnswer(List<String> choices) {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new MultipleChoiceQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {
						return s.apply(answer);
					}

					@Override
					public List<String> getChoices() {
						return choices;
					}
				};
			});
		}

		public FinalizerStage withCustomAnswer(QuestionFactory nextQuestion) {
			return new FinalizerStage(initiators, key, question, nextQuestion);
		}
	}

	public static class FinalizerStage {
		private final List<QuestionInitiator> initiators;

		private FinalizerStage(String key, String question,
				QuestionFactory nextQuestion) {
			initiators = Lists.newArrayList();
			initiators.add(new QuestionInitiator(key, question, nextQuestion));
		}

		private FinalizerStage(List<QuestionInitiator> initiators, String key,
				String question, QuestionFactory nextQuestion) {
			this.initiators = initiators;
			initiators.add(new QuestionInitiator(key, question, nextQuestion));
		}

		public QuestionStage andForKey(String key) {
			return new QuestionStage(key, initiators);
		}

		public Action andThen(Function<Answers, Action> answersToAction) {
			QuestionInitiator initiator = initiators.remove(0);

			return initiator.initiate(initiators);
		}
	}

	private static class QuestionInitiator {
		private final String key;

		private final String question;

		private final QuestionFactory questionMaker;

		private QuestionInitiator(String key, String question,
				QuestionFactory questionMaker) {
			this.key = key;
			this.question = question;
			this.questionMaker = questionMaker;
		}

		public Action initiate(List<QuestionInitiator> next) {
			return initiate(new Answers(Maps.newLinkedHashMap()), next);
		}

		private Action initiate(Answers answers, List<QuestionInitiator> next) {
			return questionMaker.initialize(question, answers, s -> {
				answers.register(key, s);

				QuestionInitiator initiator = next.remove(0);

				return initiator.initiate(next);

			});
		}

	}

	@FunctionalInterface
	public interface QuestionFactory {
		Action initialize(String question, Answers previousAnswers,
				Function<Object, Action> chainFinalizer);
	}
}
