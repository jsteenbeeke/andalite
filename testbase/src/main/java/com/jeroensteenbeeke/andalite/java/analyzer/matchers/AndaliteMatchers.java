/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer.matchers;

import static org.hamcrest.CoreMatchers.*;

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

public final class AndaliteMatchers {
	private AndaliteMatchers() {

	}

	public static Matcher<AnalyzedAnnotation> hasValue(
		@NotNull final String name,
		@NotNull final Class<? extends BaseValue<?,?,?>> valueClass) {
		return new AnnotationFieldPresenceMatcher(name, valueClass);
	}

	public static Matcher<Denomination<?,?>> hasName(@NotNull final String className) {
		return new DenominationNameMatcher(className);
	}

	public static Matcher<AnalyzedClass> extendsClass(
		@NotNull final String className) {
		return new SuperClassNameMatcher(className);
	}

	public static Matcher<AnalyzedClass> hasNoSuperClass() {
		Matcher<Object> nullValue = nullValue();
		return matchSuperClass(nullValue);
	}

	public static Matcher<AnalyzedClass> implementsInterface(
		@NotNull final String interfaceName) {
		return new InterfaceNameMatcher(interfaceName);
	}

	public static Matcher<AnalyzedClass> doesNotImplementInterface(
		@NotNull final String interfaceName) {
		return not(implementsInterface(interfaceName));
	}

	public static Matcher<AnalyzedSourceFile> inPackage(
		@NotNull final String packageName) {
		return new PackageMatcher(packageName);
	}

	public static Matcher<AccessModifiable<?,?>> hasModifier(
		@NotNull final AccessModifier modifier) {
		return new AccessMatcher(modifier);
	}

	public static Matcher<AnalyzedSourceFile> hasClasses(int expectedSize) {
		return new FileClassesMatcher(
			new SizeMatcher<>(
				expectedSize));
	}

	public static Matcher<AnalyzedSourceFile> hasClasses() {
		return not(hasNoClasses());
	}

	public static Matcher<AnalyzedSourceFile> hasNoClasses() {
		Matcher<List<AnalyzedClass>> delegate = isEmpty();

		return new FileClassesMatcher(delegate);
	}

	public static Matcher<AnalyzedSourceFile> importsClass(
		@NotNull final String className) {
		Matcher<AnalyzedImport> importMatcher = new ImportMatcher(className);
		Matcher<List<AnalyzedImport>> hasItem = contains(importMatcher);

		return new FileImportsMatcher(hasItem);
	}

	public static Matcher<AnalyzedSourceFile> hasImports() {
		return not(hasNoImports());
	}

	public static Matcher<AnalyzedSourceFile> hasImports(int expectedAmount) {
		return new FileImportsMatcher(
			new SizeMatcher<>(
				expectedAmount));
	}

	public static Matcher<AnalyzedSourceFile> hasNoImports() {
		Matcher<List<AnalyzedImport>> delegate = isEmpty();

		return new FileImportsMatcher(delegate);
	}

	public static Matcher<ContainingDenomination<?,?>> hasFields() {
		return not(hasNoFields());
	}

	public static Matcher<ContainingDenomination<?,?>> hasNoFields() {
		Matcher<List<AnalyzedField>> delegate = isEmpty();

		return new ContainingDenominationFieldsMatcher(delegate);
	}

	public static Matcher<ContainingDenomination<?,?>> hasMethods() {
		return not(hasNoMethods());
	}

	public static Matcher<ContainingDenomination<?,?>> hasMethods(int expectedAmount) {
		return new ContainingDenominationMethodsMatcher(
			new SizeMatcher<>(
				expectedAmount));
	}

	public static Matcher<ContainingDenomination<?,?>> hasNoMethods() {
		Matcher<List<AnalyzedMethod>> delegate = isEmpty();

		return new ContainingDenominationMethodsMatcher(delegate);
	}

	@NotNull
	public static GetMethodBuilder hasMethod() {
		return new GetMethodBuilder();
	}

	public static Matcher<ConstructableDenomination<?,?>> hasConstructors() {
		return not(hasNoConstructors());
	}

	public static Matcher<ConstructableDenomination<?,?>> hasNoConstructors() {
		Matcher<List<AnalyzedConstructor>> delegate = isEmpty();

		return new ConstructableDenominationConstructorsMatchers(delegate);
	}

	public static Matcher<ContainingDenomination<?,?>> hasInnerClasses() {
		return not(hasNoInnerClasses());
	}

	public static Matcher<ContainingDenomination<?,?>> hasInnerClasses(
		int expectedAmount) {
		return new InnerClassesMatcher(
			new SizeMatcher<>(
				expectedAmount));
	}

	public static Matcher<ContainingDenomination<?,?>> hasNoInnerClasses() {
		Matcher<List<AnalyzedClass>> delegate = isEmpty();

		return new InnerClassesMatcher(delegate);
	}

	public static Matcher<ContainingDenomination<?,?>> hasInterfaces() {
		return not(hasNoInterfaces());
	}

	public static Matcher<ContainingDenomination<?,?>> hasInterfaces(
		int expectedAmount) {
		return new ContainingDenominationInterfacesMatcher(
			new SizeMatcher<>(expectedAmount));
	}

	public static Matcher<ContainingDenomination<?,?>> hasNoInterfaces() {
		Matcher<List<String>> delegate = isEmpty();

		return new ContainingDenominationInterfacesMatcher(delegate);
	}

	public static <I, C extends Collection<I>> Matcher<C> contains(
		Matcher<I> matcher) {
		return new CollectionItemMatcher<>(matcher);
	}

	public static Matcher<Annotatable<?,?>> hasAnnotation(
		@NotNull final String annotation) {
		return new AnnotationMatcher(annotation);
	}

	public static <C extends Collection<?>> Matcher<C> isEmpty() {
		return new TypeSafeDiagnosingMatcher<>() {

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
		return new ByPropertyMatcher<>(matcher) {
			@Override
			protected String getProperty() {
				return "superClass";
			}

			@Override
			protected String transform(AnalyzedClass item) {
				return item.getSuperClass().map(GenerifiedName::getName).orElse(null);
			}
		};
	}

}
