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
package com.github.antlrjavaparser.test;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.body.BodyDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;

/**
 * User: mdehaan Date: 7/26/13
 */
public class TestAstPosition {
	@Test
	public void ifStatementTest() throws Exception {

		InputStream in = getClass().getClassLoader().getResourceAsStream(
				"testFiles/TestAstPosition.java.txt");

		if (in == null) {
			System.err.println("Unable to find test file.");
			return;
		}

		CompilationUnit compilationUnit = JavaParser.parse(in);

		TypeDeclaration mainType = compilationUnit.getTypes().get(0);
		BodyDeclaration bodyDeclaration = mainType.getMembers().get(0);

		Assert.assertEquals("Method body line start is incorrect.", 6,
				bodyDeclaration.getBeginLine());
		Assert.assertEquals("Method body line end is incorrect.", 15,
				bodyDeclaration.getEndLine());

		Assert.assertEquals("Method body column start is incorrect.", 4,
				bodyDeclaration.getBeginColumn());
		Assert.assertEquals("Method body column end is incorrect.", 4,
				bodyDeclaration.getEndColumn());

		Assert.assertEquals("Method body index start is incorrect.", 75,
				bodyDeclaration.getBeginIndex());
		Assert.assertEquals("Method body index end is incorrect.", 288,
				bodyDeclaration.getEndIndex());
	}
}
