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

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import com.jeroensteenbeeke.andalite.java.analyzer.*;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipeBuilder;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static com.jeroensteenbeeke.andalite.java.analyzer.matchers.AndaliteMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class IfStatementTest extends DummyAwareTest {
	@Test
	public void testIfBlockWithEmptyStatement() throws IOException {
		File file = getDummy(BaseDummies.IfStatements);
		ClassAnalyzer analyzer = new ClassAnalyzer(file);
		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat("File can be parsed", result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasName("IfStatements"));
		assertThat(analyzedClass, hasMethod().named("emptyIf"));

		AnalyzedMethod emptyIf = analyzedClass.getMethod().named("emptyIf");

		assertThat(emptyIf, notNullValue());

		List<AnalyzedStatement<?,?>> statements = emptyIf.getStatements();
		assertThat(statements, notNullValue());
		assertThat(statements.size(), is(1));

		AnalyzedStatement<?,?> first = statements.get(0);
		assertThat(first, instanceOf(IfStatement.class));
		IfStatement ifStatement = (IfStatement) first;

		assertThat(ifStatement.getThenStatement(),
				instanceOf(BlockStatement.class));

		assertThat(ifStatement.getElseStatement(), nullValue());

		JavaRecipeBuilder java = new JavaRecipeBuilder();
		java.inPublicClass().forMethod().named("emptyIf").inBody()
			.inIfStatement().withExpression("1 == 2").thenStatement()
			.body().ensureStatement("System.out.println()");

		java.build().applyTo(file);

		verifyBlockContents(file);

	}

	private void verifyBlockContents(File file) {
		ClassAnalyzer analyzer = new ClassAnalyzer(file);
		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat("File can be parsed", result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasName("IfStatements"));
		assertThat(analyzedClass, hasMethod().named("emptyIf"));

		AnalyzedMethod emptyIf = analyzedClass.getMethod().named("emptyIf");

		assertThat(emptyIf, notNullValue());

		List<AnalyzedStatement<?,?>> statements = emptyIf.getStatements();
		assertThat(statements, notNullValue());
		assertThat(statements.size(), is(1));

		AnalyzedStatement<?,?> first = statements.get(0);
		assertThat(first, instanceOf(IfStatement.class));
		IfStatement ifStatement = (IfStatement) first;

		assertThat(ifStatement.getThenStatement(),
				instanceOf(BlockStatement.class));

		BlockStatement block = (BlockStatement) ifStatement.getThenStatement();

		List<AnalyzedStatement<?,?>> thenStatements = block.getStatements();
		assertThat(thenStatements, notNullValue());

		assertThat(thenStatements.size(), is(1));
	}

	@Test
	public void testIfElseBlockWithEmptyStatement() throws IOException {
		File file = getDummy(BaseDummies.IfStatements);
		ClassAnalyzer analyzer = new ClassAnalyzer(file);
		TypedResult<AnalyzedSourceFile> result = analyzer.analyze();

		assertThat("File can be parsed", result, isOk());

		AnalyzedSourceFile sourceFile = result.getObject();

		assertThat(sourceFile, hasClasses(1));

		AnalyzedClass analyzedClass = sourceFile.getClasses().get(0);

		assertThat(analyzedClass, hasName("IfStatements"));
		assertThat(analyzedClass, hasMethod().named("emptyIfElse"));

		AnalyzedMethod emptyIfElse = analyzedClass.getMethod()
				.named("emptyIfElse");

		assertThat(emptyIfElse, notNullValue());

		List<AnalyzedStatement<?,?>> statements = emptyIfElse.getStatements();
		assertThat(statements, notNullValue());
		assertThat(statements.size(), is(1));

		AnalyzedStatement<?,?> first = statements.get(0);
		assertThat(first, instanceOf(IfStatement.class));

		IfStatement ifElseStatement = (IfStatement) first;

		assertThat(ifElseStatement.getThenStatement(),
				instanceOf(BlockStatement.class));
		assertThat(ifElseStatement.getElseStatement(),
				instanceOf(BlockStatement.class));

	}
}
