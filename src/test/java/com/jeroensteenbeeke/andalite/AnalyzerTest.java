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

import static com.jeroensteenbeeke.andalite.analyzer.matchers.AndaliteMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.analyzer.*;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;

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
				.hasImport("com.jeroensteenbeeke.andalite.analyzer.ClassAnalyzer"));
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
		ClassAnalyzer analyzer = analyzeDummy("Empty");
		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();

		assertThat(file, notNullValue());

		assertThat(file, inPackage(DUMMY_PACKAGE));

		assertThat(file, hasNoClasses());
		assertThat(file, hasNoImports());
	}

	@Test
	public void testBareJava() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy("BareClass");

		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();

		assertThat(file, notNullValue());

		assertThat(file, inPackage(DUMMY_PACKAGE));

		assertThat(file, hasNoImports());
		assertThat(file, hasClasses());

		assertThat(file.getClasses().size(), is(1));
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

	@Test
	public void testReverseIntComparator() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy("ReverseIntComparator");

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
		assertThat(analyzedClass, implementsInterface("Comparator"));

		assertThat(analyzedClass.getSuperClass(), nullValue());

	}
}
