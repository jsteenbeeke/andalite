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
package com.jeroensteenbeeke.andalite.analyzer;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.*;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.DummyAwareTest;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.java.transformation.ClassLocator;
import com.jeroensteenbeeke.andalite.java.transformation.Operations;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;

public class IfStatementTest extends DummyAwareTest {
	@Test
	public void testIfBlockWithEmptyStatement() throws IOException {
		File file = getDummy("IfStatements");
		ClassAnalyzer analyzer = new ClassAnalyzer(file);
		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat("File can be parsed", result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasName("IfStatements"));
		assertThat(analyzedClass, hasMethod().named("emptyIf"));

		AnalyzedMethod emptyIf = analyzedClass.getMethod().named("emptyIf");

		assertThat(emptyIf, notNullValue());

		List<AnalyzedStatement> statements = emptyIf.getStatements();
		assertThat(statements, notNullValue());
		assertThat(statements.size(), is(1));

		AnalyzedStatement first = statements.get(0);
		assertThat(first, instanceOf(IfStatement.class));
		IfStatement ifStatement = (IfStatement) first;

		assertThat(ifStatement.getThenStatement(),
				instanceOf(BlockStatement.class));

		assertThat(ifStatement.getElseStatement(), nullValue());

		JavaRecipeBuilder builder = new JavaRecipeBuilder();
		builder.inClass(ClassLocator.publicClass()).forMethod()
				.named("emptyIf").inBody().inIfExpression()
				.withExpression("1 == 2").thenStatement().body()
				.ensure(Operations.hasStatement("System.out.println()"));

		builder.build().applyTo(file);

		verifyBlockContents(file);

	}

	private void verifyBlockContents(File file) {
		ClassAnalyzer analyzer = new ClassAnalyzer(file);
		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat("File can be parsed", result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasName("IfStatements"));
		assertThat(analyzedClass, hasMethod().named("emptyIf"));

		AnalyzedMethod emptyIf = analyzedClass.getMethod().named("emptyIf");

		assertThat(emptyIf, notNullValue());

		List<AnalyzedStatement> statements = emptyIf.getStatements();
		assertThat(statements, notNullValue());
		assertThat(statements.size(), is(1));

		AnalyzedStatement first = statements.get(0);
		assertThat(first, instanceOf(IfStatement.class));
		IfStatement ifStatement = (IfStatement) first;

		assertThat(ifStatement.getThenStatement(),
				instanceOf(BlockStatement.class));

		BlockStatement block = (BlockStatement) ifStatement.getThenStatement();

		List<AnalyzedStatement> thenStatements = block.getStatements();
		assertThat(thenStatements, notNullValue());

		assertThat(thenStatements.size(), is(1));
	}

	@Test
	public void testIfElseBlockWithEmptyStatement() throws IOException {
		File file = getDummy("IfStatements");
		ClassAnalyzer analyzer = new ClassAnalyzer(file);
		TypedActionResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat("File can be parsed", result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasName("IfStatements"));
		assertThat(analyzedClass, hasMethod().named("emptyIfElse"));

		AnalyzedMethod emptyIfElse = analyzedClass.getMethod().named(
				"emptyIfElse");

		assertThat(emptyIfElse, notNullValue());

		List<AnalyzedStatement> statements = emptyIfElse.getStatements();
		assertThat(statements, notNullValue());
		assertThat(statements.size(), is(1));

		AnalyzedStatement first = statements.get(0);
		assertThat(first, instanceOf(IfStatement.class));

		IfStatement ifElseStatement = (IfStatement) first;

		assertThat(ifElseStatement.getThenStatement(),
				instanceOf(BlockStatement.class));
		assertThat(ifElseStatement.getElseStatement(),
				instanceOf(BlockStatement.class));

	}
}
