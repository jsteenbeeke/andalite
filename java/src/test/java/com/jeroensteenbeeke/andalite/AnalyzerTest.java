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
package com.jeroensteenbeeke.andalite;

import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasClasses;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasInterfaces;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasMethods;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasModifier;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasName;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoClasses;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoConstructors;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoFields;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoImports;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoInnerClasses;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoInterfaces;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoMethods;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.hasNoSuperClass;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.implementsInterface;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.importsClass;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.inPackage;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.core.test.IDummyDescriptor;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

public class AnalyzerTest extends DummyAwareTest {
	private static final String DUMMY_PACKAGE = "com.jeroensteenbeeke.andalite.dummy";

	@Test
	public void testAnalyzer() {
		ClassAnalyzer classAnalyzer = new ClassAnalyzer(
				new File(
						"src/test/java/com/jeroensteenbeeke/andalite/AnalyzerTest.java"));
		TypedActionResult<AnalyzedSourceFile> result = classAnalyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile source = result.getObject();

		assertTrue(source
				.hasImport("com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer"));
	}

	@Test
	public void testAnnotationScan() {
		ClassAnalyzer classAnalyzer = new ClassAnalyzer(
				new File(
						"src/test/java/com/jeroensteenbeeke/andalite/analyzer/LolCat.java"));

		TypedActionResult<AnalyzedSourceFile> result = classAnalyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile source = result.getObject();
		List<AnalyzedClass> classes = source.getClasses();
		assertEquals(1, classes.size());

		AnalyzedClass lolcat = classes.get(0);
		assertThat(lolcat, hasName("LolCat"));

		assertTrue(lolcat.hasAnnotation("Entity"));
		assertTrue(lolcat.hasAnnotation("Table"));

		AnalyzedAnnotation table = lolcat.getAnnotation("Table");
		assertNotNull(table);
		assertTrue(table.hasValueOfType(AnnotationValue.class, "indexes"));
		AnnotationValue index = table
				.getValue(AnnotationValue.class, "indexes");
		assertNotNull(index);

		assertEquals("Index", index.getValue().getType());

		assertTrue(table.hasValueOfType(ArrayValue.class, "uniqueConstraints"));

		ArrayValue array = table
				.getValue(ArrayValue.class, "uniqueConstraints");
		assertNotNull(array);
		assertEquals(2, array.getValue().size());

		for (BaseValue<?> baseValue : array.getValue()) {
			assertTrue(baseValue instanceof AnnotationValue);
			AnnotationValue annotValue = (AnnotationValue) baseValue;

			AnalyzedAnnotation annotation = annotValue.getValue();
			assertEquals("UniqueConstraint", annotation.getType());
		}

		assertTrue(lolcat.hasField("id"));
		assertTrue(lolcat.hasField("code"));
		assertTrue(lolcat.hasField("handle"));
		assertTrue(lolcat.hasField("canHaz"));

		AnalyzedField id = lolcat.getField("id");
		assertNotNull(id);
		assertTrue(id.hasAnnotation("Id"));

		AnalyzedField code = lolcat.getField("code");
		assertNotNull(code);
		assertTrue(code.hasAnnotation("Column"));

		AnalyzedField handle = lolcat.getField("handle");
		assertNotNull(handle);
		assertTrue(handle.hasAnnotation("Column"));

		AnalyzedField canHaz = lolcat.getField("canHaz");
		assertNotNull(canHaz);
		assertTrue(canHaz.hasAnnotation("ManyToOne"));

	}

	@Test
	public void testEmptyJava() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.Empty);
		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();

		assertThat(file, notNullValue());

		assertThat(file, inPackage(DUMMY_PACKAGE));

		assertThat(file, hasNoClasses());
		assertThat(file, hasNoImports());
	}

	@Test
	public void testBareClass() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.BareClass);

		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();
		assertThat(file, notNullValue());
		assertThat(file, inPackage(DUMMY_PACKAGE));
		assertThat(file, hasNoImports());
		assertThat(file, hasClasses(1));

		AnalyzedClass analyzedClass = file.getClasses().get(0);
		assertThat(analyzedClass, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedClass, hasName("BareClass"));
		assertThat(analyzedClass, hasNoFields());
		assertThat(analyzedClass, hasNoMethods());
		assertThat(analyzedClass, hasNoConstructors());
		assertThat(analyzedClass, hasNoInnerClasses());
		assertThat(analyzedClass, hasNoInterfaces());
		assertThat(analyzedClass, hasNoSuperClass());

	}

	private ClassAnalyzer analyzeDummy(IDummyDescriptor descriptor)
			throws IOException {
		return new ClassAnalyzer(getDummy(descriptor));
	}

	@Test
	public void testBareInterface() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.BareInterface);

		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();
		assertThat(file, notNullValue());
		assertThat(file, inPackage(DUMMY_PACKAGE));
		assertThat(file, hasNoImports());
		assertThat(file, hasClasses(0));

		AnalyzedInterface analyzedInterface = file.getInterfaces().get(0);
		assertThat(analyzedInterface, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedInterface, hasName("BareInterface"));
		assertThat(analyzedInterface, hasNoFields());
		assertThat(analyzedInterface, hasNoMethods());
		assertThat(analyzedInterface, hasNoInnerClasses());
		assertThat(analyzedInterface, hasNoInterfaces());

	}

	@Test
	public void testBareEnum() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.BareEnum);

		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();
		assertThat(file, notNullValue());
		assertThat(file, inPackage(DUMMY_PACKAGE));
		assertThat(file, hasNoImports());
		assertThat(file, hasClasses(0));

		AnalyzedEnum analyzedEnum = file.getEnums().get(0);
		assertThat(analyzedEnum, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedEnum, hasName("BareEnum"));
		assertThat(analyzedEnum, hasNoFields());
		assertThat(analyzedEnum, hasNoMethods());
		assertThat(analyzedEnum, hasNoConstructors());
		assertThat(analyzedEnum, hasNoInnerClasses());
		assertThat(analyzedEnum, hasNoInterfaces());

	}

	@Test
	public void testReverseIntComparator() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.ReverseIntComparator);

		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();

		assertThat(file, notNullValue());

		assertThat(file, inPackage(DUMMY_PACKAGE));

		assertThat(file, importsClass("java.util.Comparator"));

		assertThat(file, hasClasses(1));

		AnalyzedClass analyzedClass = file.getClasses().get(0);

		assertThat(analyzedClass, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedClass, hasName("ReverseIntComparator"));

		assertThat(analyzedClass, hasNoFields());
		assertThat(analyzedClass, hasMethods(1));
		assertThat(analyzedClass, hasNoConstructors());
		assertThat(analyzedClass, hasNoInnerClasses());
		assertThat(analyzedClass, hasInterfaces(1));
		assertThat(analyzedClass, implementsInterface("Comparator<Integer>"));

		assertThat(analyzedClass, hasNoSuperClass());

	}
}
