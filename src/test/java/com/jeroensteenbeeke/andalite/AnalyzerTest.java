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

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.analyzer.*;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;

public class AnalyzerTest {
	@Test
	public void testRewriter() {
		ClassAnalyzer classAnalyzer = new ClassAnalyzer(
				new File(
						"src/test/java/com/jeroensteenbeeke/andalite/RewriterTest.java"));
		TypedActionResult<AnalyzedSourceFile> result = classAnalyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile source = result.getObject();

		List<AnalyzedClass> classes = source.getClasses();
		assertEquals(1, classes.size());
		AnalyzedClass analyzedClass = classes.get(0);

		assertEquals("com.jeroensteenbeeke.andalite",
				analyzedClass.getPackageName());
		assertEquals("RewriterTest", analyzedClass.getClassName());
		assertTrue(analyzedClass.hasAnnotation("Ignore"));
		AnalyzedAnnotation annotation = analyzedClass.getAnnotation("Ignore");
		assertFalse(annotation == null);

		List<AnalyzedMethod> methods = analyzedClass.getMethods();
		assertEquals(1, methods.size());
		AnalyzedMethod method = methods.get(0);
		assertNotNull(method);

		assertTrue(method.hasAnnotation("Test"));
	}

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
		assertEquals("LolCat", lolcat.getClassName());

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
	public void outputTest() {
		ClassAnalyzer classAnalyzer = new ClassAnalyzer(
				new File(
						"src/test/java/com/jeroensteenbeeke/andalite/analyzer/LolCat.java"));

		TypedActionResult<AnalyzedSourceFile> result = classAnalyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile source = result.getObject();
		source.output(new PrintStreamCallback(System.out));
	}
}
