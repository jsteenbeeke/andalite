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

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.lux.Result;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.annotation.Nonnull;
import java.util.List;

public final class ResultMatchers {

	private ResultMatchers() {

	}

	public static Matcher<Result<?,?>> isOk() {
		return new TypeSafeDiagnosingMatcher<Result<?,?>>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("is ok");
			}

			@Override
			protected boolean matchesSafely(Result<?,?> item,
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

	public static Matcher<Result<?,?>> hasError(@Nonnull final String message) {
		return new TypeSafeDiagnosingMatcher<>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("is not ok, with error ").appendValue(message);
			}

			@Override
			protected boolean matchesSafely(Result<?, ?> item, Description mismatchDescription) {
				boolean ok = item.isOk();
				boolean messageEqual = message.equals(item.getMessage());
				boolean matches = !ok && messageEqual;

				if (!matches) {
					List<String> messages = Lists.newLinkedList();

					if (ok) {
						messages.add("is ok");
					}
					if (!messageEqual) {
						if (item.getMessage() != null) {
							messages.add(String.format("error is \"%s\"", item.getMessage()));
						} else {
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
