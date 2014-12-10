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
package com.jeroensteenbeeke.andalite.analyzer.matchers;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifiable;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedImport;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;

public final class AndaliteMatchers {
	private AndaliteMatchers() {

	}

	public static Matcher<AnalyzedClass> hasName(@Nonnull final String className) {
		return new ClassNameMatcher(className);
	}

	public static Matcher<AnalyzedClass> extendsClass(
			@Nonnull final String className) {
		return new SuperClassNameMatcher(className);
	}

	public static Matcher<AnalyzedClass> implementsInterface(
			@Nonnull final String interfaceName) {
		return new InterfaceNameMatcher(interfaceName);
	}

	public static Matcher<AnalyzedSourceFile> inPackage(
			@Nonnull final String packageName) {
		return new PackageMatcher(packageName);
	}

	public static Matcher<AccessModifiable> hasModifier(
			@Nonnull final AccessModifier modifier) {
		return new AccessMatcher(modifier);
	}

	public static Matcher<AnalyzedSourceFile> hasNoClasses() {
		Matcher<List<AnalyzedClass>> delegate = isEmpty();

		return new ByPropertyMatcher<AnalyzedSourceFile, List<AnalyzedClass>>(
				delegate) {

			@Override
			protected List<AnalyzedClass> transform(AnalyzedSourceFile item) {
				return item.getClasses();
			}

			@Override
			protected String getProperty() {
				return "classes";
			}

		};
	}

	public static Matcher<AnalyzedSourceFile> hasImports() {
		return CoreMatchers.not(hasNoImports());
	}

	public static Matcher<AnalyzedSourceFile> hasClasses() {
		return CoreMatchers.not(hasNoClasses());
	}

	public static Matcher<AnalyzedSourceFile> hasNoImports() {
		Matcher<List<AnalyzedImport>> delegate = isEmpty();

		return new ByPropertyMatcher<AnalyzedSourceFile, List<AnalyzedImport>>(
				delegate) {

			@Override
			protected List<AnalyzedImport> transform(AnalyzedSourceFile item) {
				return item.getImports();
			}

			@Override
			protected String getProperty() {
				return "imports";
			}

		};
	}

	public static <C extends Collection<?>> Matcher<C> isEmpty() {
		return new TypeSafeDiagnosingMatcher<C>() {

			@Override
			protected boolean matchesSafely(C item,
					Description mismatchDescription) {
				if (item.isEmpty()) {
					return true;
				}

				mismatchDescription.appendText("is not empty");

				return false;

			}

			@Override
			public void describeTo(Description description) {
				description.appendText("an empty collection ");
			}

		};
	}

}
