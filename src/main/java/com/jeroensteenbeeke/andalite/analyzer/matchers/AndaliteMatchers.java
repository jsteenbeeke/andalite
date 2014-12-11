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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

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
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedImport;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedMethod;
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

	public static Matcher<AnalyzedClass> hasNoSuperClass() {
		Matcher<Object> nullValue = nullValue();
		return matchSuperClass(nullValue);
	}

	public static Matcher<AnalyzedClass> implementsInterface(
			@Nonnull final String interfaceName) {
		return new InterfaceNameMatcher(interfaceName);
	}

	public static Matcher<AnalyzedClass> doesNotImplementInterface(
			@Nonnull final String interfaceName) {
		return not(implementsInterface(interfaceName));
	}

	public static Matcher<AnalyzedSourceFile> inPackage(
			@Nonnull final String packageName) {
		return new PackageMatcher(packageName);
	}

	public static Matcher<AccessModifiable> hasModifier(
			@Nonnull final AccessModifier modifier) {
		return new AccessMatcher(modifier);
	}

	public static Matcher<AnalyzedSourceFile> hasClasses(int expectedSize) {
		return new FileClassesMatcher(
				new SizeMatcher<AnalyzedClass, List<AnalyzedClass>>(
						expectedSize));
	}

	public static Matcher<AnalyzedSourceFile> hasClasses() {
		return CoreMatchers.not(hasNoClasses());
	}

	public static Matcher<AnalyzedSourceFile> hasNoClasses() {
		Matcher<List<AnalyzedClass>> delegate = isEmpty();

		return new FileClassesMatcher(delegate);
	}

	public static Matcher<AnalyzedSourceFile> importsClass(
			@Nonnull final String className) {
		Matcher<AnalyzedImport> importMatcher = new ImportMatcher(className);
		Matcher<List<AnalyzedImport>> hasItem = contains(importMatcher);

		return new FileImportsMatcher(hasItem);
	}

	public static Matcher<AnalyzedSourceFile> hasImports() {
		return CoreMatchers.not(hasNoImports());
	}

	public static Matcher<AnalyzedSourceFile> hasImports(int expectedAmount) {
		return new FileImportsMatcher(
				new SizeMatcher<AnalyzedImport, List<AnalyzedImport>>(
						expectedAmount));
	}

	public static Matcher<AnalyzedSourceFile> hasNoImports() {
		Matcher<List<AnalyzedImport>> delegate = isEmpty();

		return new FileImportsMatcher(delegate);
	}

	public static Matcher<AnalyzedClass> hasFields() {
		return CoreMatchers.not(hasNoFields());
	}

	public static Matcher<AnalyzedClass> hasNoFields() {
		Matcher<List<AnalyzedField>> delegate = isEmpty();

		return new ClassFieldsMatcher(delegate);
	}

	public static Matcher<AnalyzedClass> hasMethods() {
		return CoreMatchers.not(hasNoMethods());
	}

	public static Matcher<AnalyzedClass> hasMethods(int expectedAmount) {
		return new ClassMethodsMatcher(
				new SizeMatcher<AnalyzedMethod, List<AnalyzedMethod>>(
						expectedAmount));
	}

	public static Matcher<AnalyzedClass> hasNoMethods() {
		Matcher<List<AnalyzedMethod>> delegate = isEmpty();

		return new ClassMethodsMatcher(delegate);
	}

	public static Matcher<AnalyzedClass> hasConstructors() {
		return CoreMatchers.not(hasNoConstructors());
	}

	public static Matcher<AnalyzedClass> hasNoConstructors() {
		Matcher<List<AnalyzedConstructor>> delegate = isEmpty();

		return new ClassConstructorsMatchers(delegate);
	}

	public static Matcher<AnalyzedClass> hasInnerClasses() {
		return CoreMatchers.not(hasNoInnerClasses());
	}

	public static Matcher<AnalyzedClass> hasInnerClasses(int expectedAmount) {
		return new InnerClassesMatcher(
				new SizeMatcher<AnalyzedClass, List<AnalyzedClass>>(
						expectedAmount));
	}

	public static Matcher<AnalyzedClass> hasNoInnerClasses() {
		Matcher<List<AnalyzedClass>> delegate = isEmpty();

		return new InnerClassesMatcher(delegate);
	}

	public static Matcher<AnalyzedClass> hasInterfaces() {
		return CoreMatchers.not(hasNoInterfaces());
	}

	public static Matcher<AnalyzedClass> hasInterfaces(int expectedAmount) {
		return new ClassInterfacesMatcher(
				new SizeMatcher<String, List<String>>(expectedAmount));
	}

	public static Matcher<AnalyzedClass> hasNoInterfaces() {
		Matcher<List<String>> delegate = isEmpty();

		return new ClassInterfacesMatcher(delegate);
	}

	public static <I, C extends Collection<I>> Matcher<C> contains(
			Matcher<I> matcher) {
		return new CollectionItemMatcher<I, C>(matcher);
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

	private static ByPropertyMatcher<AnalyzedClass, Object> matchSuperClass(
			Matcher<Object> matcher) {
		return new ByPropertyMatcher<AnalyzedClass, Object>(matcher) {
			@Override
			protected String getProperty() {
				return "superClass";
			}

			@Override
			protected String transform(AnalyzedClass item) {
				return item.getSuperClass();
			}
		};
	}

}
