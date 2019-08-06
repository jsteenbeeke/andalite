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
package com.jeroensteenbeeke.andalite;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeroensteenbeeke.andalite.java.analyzer.*;
import org.junit.Test;

import com.jeroensteenbeeke.lux.TypedResult;
import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.core.test.IDummyDescriptor;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

public class AnalyzerTest extends DummyAwareTest {
	private static final String DUMMY_PACKAGE = "com.jeroensteenbeeke.andalite.dummy";

	@Test
	public void testAnalyzer() {
		TypedResult<AnalyzedSourceFile> result = analyzeAndVerify("src/test/java/com/jeroensteenbeeke/andalite/AnalyzerTest.java");

		AnalyzedSourceFile source = result.getObject();

		assertTrue(source
					   .hasImport("com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer"));
	}

	@Test
	public void testAnnotationType() {
		TypedResult<AnalyzedSourceFile> result = analyzeAndVerify("src/test/java/com/jeroensteenbeeke/andalite/analyzer/TestType.java");
	}

	@Test
	public void testInterface() {
		TypedResult<AnalyzedSourceFile> result = analyzeAndVerify("src/test/java/com/jeroensteenbeeke/andalite/analyzer/TestInterface.java");

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedInterface> interfaces = sourceFile.getInterfaces();
		assertThat(interfaces.size(), equalTo(1));

		AnalyzedInterface anInterface = interfaces.get(0);

		List<AnalyzedMethod> methods = anInterface.getMethods();

		assertThat(methods.size(), equalTo(5));

		Map<String, AnalyzedMethod> methodsByName = methods
			.stream()
			.collect(Collectors.toMap(AnalyzedMethod::getName, Function.identity()));

		assertTrue(methodsByName.containsKey("a"));
		assertTrue(methodsByName.containsKey("b"));
		assertTrue(methodsByName.containsKey("c"));
		assertTrue(methodsByName.containsKey("d"));
		assertTrue(methodsByName.containsKey("e"));

		assertThat(methodsByName.get("a").getAccessModifier(), equalTo(AccessModifier.PUBLIC));
		assertThat(methodsByName.get("b").getAccessModifier(), equalTo(AccessModifier.DEFAULT));
		assertThat(methodsByName.get("c").getAccessModifier(), equalTo(AccessModifier.PRIVATE));
		assertTrue(methodsByName.get("d").isStatic());
		assertThat(methodsByName.get("d").getAccessModifier(), equalTo(AccessModifier.PRIVATE));
		assertTrue(methodsByName.get("e").isStatic());
		assertThat(methodsByName.get("e").getAccessModifier(), equalTo(AccessModifier.PUBLIC));
	}

	@Test
	public void testEnum() {
		TypedResult<AnalyzedSourceFile> result = analyzeAndVerify("src/test/java/com/jeroensteenbeeke/andalite/analyzer/TestEnum.java");

		AnalyzedSourceFile sourceFile = result.getObject();

		List<AnalyzedEnum> enums = sourceFile.getEnums();

		assertThat(enums.size(), equalTo(1));

		AnalyzedEnum analyzedEnum = enums.get(0);

		List<AnalyzedEnumConstant> constants = analyzedEnum.getConstants();

		assertThat(constants.size(), equalTo(3));

		List<AnalyzedMethod> baseMethods = analyzedEnum.getMethods();

		assertThat(baseMethods.size(), equalTo(2));

		Map<String,AnalyzedMethod> baseMethodsByName = baseMethods.stream().collect(Collectors.toMap(AnalyzedMethod::getName, Function
			.identity()));

		assertTrue(baseMethodsByName.containsKey("bar"));
		assertTrue(baseMethodsByName.containsKey("baz"));

		assertThat(baseMethodsByName.get("bar").getAccessModifier(), equalTo(AccessModifier.PROTECTED));
		assertThat(baseMethodsByName.get("baz").getAccessModifier(), equalTo(AccessModifier.DEFAULT));

		Map<String, AnalyzedEnumConstant> constantsByName = constants
			.stream()
			.collect(Collectors.toMap(AnalyzedEnumConstant::getDenominationName, Function.identity()));

		assertTrue(constantsByName.containsKey("A"));
		assertTrue(constantsByName.containsKey("B"));
		assertTrue(constantsByName.containsKey("C"));

		Map<String, AnalyzedMethod> aMethodsByName = constantsByName
			.get("A")
			.getMethods()
			.stream()
			.collect(Collectors.toMap(AnalyzedMethod::getName, Function
				.identity()));
		Map<String, AnalyzedMethod> bMethodsByName = constantsByName
			.get("B")
			.getMethods()
			.stream()
			.collect(Collectors.toMap(AnalyzedMethod::getName, Function
				.identity()));
		Map<String, AnalyzedMethod> cMethodsByName = constantsByName
			.get("C")
			.getMethods()
			.stream()
			.collect(Collectors.toMap(AnalyzedMethod::getName, Function
				.identity()));

		assertThat(aMethodsByName.size(), equalTo(1));
		assertThat(bMethodsByName.size(), equalTo(1));
		assertThat(cMethodsByName.size(), equalTo(2));

		assertTrue(aMethodsByName.containsKey("baz"));
		assertTrue(bMethodsByName.containsKey("baz"));
		assertTrue(cMethodsByName.containsKey("baz"));
		assertTrue(cMethodsByName.containsKey("fb"));

		assertThat(aMethodsByName.get("baz").getAccessModifier(), equalTo(AccessModifier.PROTECTED));
		assertThat(bMethodsByName.get("baz").getAccessModifier(), equalTo(AccessModifier.PROTECTED));
		assertThat(cMethodsByName.get("baz").getAccessModifier(), equalTo(AccessModifier.PROTECTED));
		assertThat(cMethodsByName.get("fb").getAccessModifier(), equalTo(AccessModifier.DEFAULT));

	}

	@Test
	public void testInnerClasses() {
		TypedResult<AnalyzedSourceFile> result = analyzeAndVerify("src/test/java/com/jeroensteenbeeke/andalite/analyzer/TestInnerClasses.java");


	}

	@Test
	public void parseAllAndaliteFiles() {
		List<File> allFiles = new ArrayList<>();
		allFiles.addAll(searchJava(new File("src/main/java/com/jeroensteenbeeke/andalite")));
		allFiles.addAll(searchJava(new File("src/test/java/com/jeroensteenbeeke/andalite")));

		allFiles.forEach(this::analyzeAndVerify);
	}


	@Test
	public void testAnnotationScan() {
		ClassAnalyzer classAnalyzer = new ClassAnalyzer(
			new File(
				"src/test/java/com/jeroensteenbeeke/andalite/analyzer/LolCat.java"));

		TypedResult<AnalyzedSourceFile> result = classAnalyzer.analyze();
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

		for (BaseValue<?, ?, ?> baseValue : array.getValue()) {
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
		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();

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

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();
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

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();
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

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();
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
	public void testBarestEnum() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.BarestEnum);

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();
		assertThat(file, notNullValue());
		assertThat(file, inPackage(DUMMY_PACKAGE));
		assertThat(file, hasNoImports());
		assertThat(file, hasClasses(0));

		AnalyzedEnum analyzedEnum = file.getEnums().get(0);
		assertThat(analyzedEnum, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedEnum, hasName("BarestEnum"));
		assertThat(analyzedEnum, hasNoFields());
		assertThat(analyzedEnum, hasNoMethods());
		assertThat(analyzedEnum, hasNoConstructors());
		assertThat(analyzedEnum, hasNoInnerClasses());
		assertThat(analyzedEnum, hasNoInterfaces());

	}

	@Test
	public void testEnumWithValuesPresent() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.EnumWithValuesPresent);

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();
		assertThat(file, notNullValue());
		assertThat(file, inPackage(DUMMY_PACKAGE));
		assertThat(file, hasNoImports());
		assertThat(file, hasClasses(0));

		AnalyzedEnum analyzedEnum = file.getEnums().get(0);
		assertThat(analyzedEnum, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedEnum, hasName("EnumWithValuesPresent"));
		assertThat(analyzedEnum, hasNoFields());
		assertThat(analyzedEnum, hasNoMethods());
		assertThat(analyzedEnum, hasNoConstructors());
		assertThat(analyzedEnum, hasNoInnerClasses());
		assertThat(analyzedEnum, hasNoInterfaces());

	}

	@Test
	public void testEnumWithValuesPresentNoSemi() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.EnumWithValuesPresentNoSemi);

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();
		assertTrue(result.isOk());

		AnalyzedSourceFile file = result.getObject();
		assertThat(file, notNullValue());
		assertThat(file, inPackage(DUMMY_PACKAGE));
		assertThat(file, hasNoImports());
		assertThat(file, hasClasses(0));

		AnalyzedEnum analyzedEnum = file.getEnums().get(0);
		assertThat(analyzedEnum, hasModifier(AccessModifier.PUBLIC));
		assertThat(analyzedEnum, hasName("EnumWithValuesPresentNoSemi"));
		assertThat(analyzedEnum, hasNoFields());
		assertThat(analyzedEnum, hasNoMethods());
		assertThat(analyzedEnum, hasNoConstructors());
		assertThat(analyzedEnum, hasNoInnerClasses());
		assertThat(analyzedEnum, hasNoInterfaces());

	}

	@Test
	public void testReverseIntComparator() throws IOException {
		ClassAnalyzer analyzer = analyzeDummy(BaseDummies.ReverseIntComparator);

		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat(result, isOk());

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


	private TypedResult<AnalyzedSourceFile> analyzeAndVerify(String pathname) {
		return analyzeAndVerify(new File(pathname));
	}

	private TypedResult<AnalyzedSourceFile> analyzeAndVerify(File file) {

		ClassAnalyzer classAnalyzer = new ClassAnalyzer(
			file);

		TypedResult<AnalyzedSourceFile> result = classAnalyzer.analyze();
		assertTrue(result.isOk());

		StringBuilder builder = new StringBuilder();
		result.getObject().output(new StringBuilderCallback(builder));
		assertTrue(builder.length() > 0);
		result.getObject().output(new PrintStreamCallback(System.out));

		return result;
	}

	private List<File> searchJava(File root) {
		if (root.isDirectory()) {
			return Arrays
				.stream(Objects.requireNonNull(root.listFiles()))
				.flatMap(f -> searchJava(f).stream())
				.collect(Collectors.toList());
		} else if (root.getName().endsWith(".java")) {
			return List.of(root);
		} else {
			return List.of();
		}
	}
}
