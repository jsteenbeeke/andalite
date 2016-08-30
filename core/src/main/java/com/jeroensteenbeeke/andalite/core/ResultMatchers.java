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
package com.jeroensteenbeeke.andalite.core;

import java.util.List;

import javax.annotation.Nonnull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Lists;

/**
 * Convenience class containing a number of Hamcrest-style matchers for use in
 * unit tests
 * 
 * @author Jeroen Steenbeeke
 *
 */
public final class ResultMatchers {

	/**
	 * Ensures people don't instantiate this class
	 */
	private ResultMatchers() {

	}

	/**
	 * Creates Matcher for {@code ActionResult} objects that are marked as being
	 * ok
	 * 
	 * @return A Matcher that matches {@code ActionResult.isOk() == true}
	 *         results
	 */
	public static Matcher<ActionResult> isOk() {
		return new TypeSafeDiagnosingMatcher<ActionResult>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("is ok");
			}

			@Override
			protected boolean matchesSafely(ActionResult item,
					Description mismatchDescription) {
				boolean matches = item.isOk();

				if (!matches) {
					mismatchDescription.appendText("is not ok: ");
					mismatchDescription.appendText(item.getMessage());

				}

				return matches;
			}
		};
	}

	/**
	 * Creates Matcher for {@code ActionResult} objects that are not marked as
	 * being
	 * ok, with the indicated error message
	 * 
	 * @param message
	 *            The expected error message
	 * @return A Matcher that matches {@code ActionResult.isOk() == true}
	 *         results
	 */
	public static Matcher<ActionResult> hasError(
			@Nonnull final String message) {
		return new TypeSafeDiagnosingMatcher<ActionResult>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("is not ok, with error ")
						.appendText(message);
			}

			@Override
			protected boolean matchesSafely(ActionResult item,
					Description mismatchDescription) {
				boolean ok = item.isOk();
				boolean messageEqual = message.equals(item.getMessage());
				boolean matches = !ok && messageEqual;

				// The result deviates from our expectations, let's figure out
				// how so we can give a meaningful error
				if (!matches) {
					List<String> messages = Lists.newLinkedList();

					// Action did not yield an error
					if (ok) {
						messages.add("is ok");
					}

					// Deviating error message
					if (!messageEqual) {
						if (item.getMessage() != null) {
							// Error message present, but wrong value, report
							messages.add(String.format("error is %s",
									item.getMessage()));
						} else {
							// No error message, usually coincides with
							// unexpected OK value
							messages.add("no error");
						}
					}

					mismatchDescription.appendValueList("", ", ", "", messages);
				}

				return matches;

			}
		};
	}
}
