package com.jeroensteenbeeke.andalite.analyzer;

import static com.jeroensteenbeeke.andalite.ResultMatchers.*;
import static com.jeroensteenbeeke.andalite.analyzer.matchers.AndaliteMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.DummyAwareTest;
import com.jeroensteenbeeke.andalite.TypedActionResult;
import com.jeroensteenbeeke.andalite.analyzer.statements.BlockStatement;
import com.jeroensteenbeeke.andalite.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.transformation.ClassLocator;
import com.jeroensteenbeeke.andalite.transformation.Operations;
import com.jeroensteenbeeke.andalite.transformation.RecipeBuilder;

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

		RecipeBuilder builder = new RecipeBuilder();
		builder.inClass(ClassLocator.publicClass()).forMethod()
				.named("emptyIf").inBody().inIfExpression()
				.withExpression("1 == 2").thenStatement().body()
				.ensure(Operations.hasStatement("System.out.println()"));

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
