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
package com.github.antlrjavaparser.test.body;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.ImportDeclaration;
import com.github.antlrjavaparser.api.expr.QualifiedNameExpr;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 1/3/13
 * Time: 11:28 AM
 */
public class TestImports {
    @Test
    public void testImport() throws Exception {
        CompilationUnit cu = JavaParser.parse("import com.github.antlrjavaparser;");

        assertTrue("Expected 1 import statement", cu.getImports().size() == 1);

        ImportDeclaration importDeclaration = cu.getImports().get(0);
        assertTrue("Expected name to be an instance of QualifiedNameExpr", importDeclaration.getName() instanceof QualifiedNameExpr);

        QualifiedNameExpr qualifiedNameExpr = (QualifiedNameExpr)importDeclaration.getName();

        assertTrue(qualifiedNameExpr.getName().equals("antlrjavaparser"));

        assertTrue("Expected qualifier to be github.  Found \"" + qualifiedNameExpr.getQualifier().getName() + "\" instead.",
                qualifiedNameExpr.getQualifier().getName().equals("github"));

    }

    @Test
    public void testStarImport() throws Exception {
        CompilationUnit cu = JavaParser.parse("import com.github.antlrjavaparser.*;");

        assertTrue("Expected 1 import statement", cu.getImports().size() == 1);

        ImportDeclaration importDeclaration = cu.getImports().get(0);
        assertTrue("Expected name to be an instance of QualifiedNameExpr", importDeclaration.getName() instanceof QualifiedNameExpr);

        QualifiedNameExpr qualifiedNameExpr = (QualifiedNameExpr)importDeclaration.getName();

        assertTrue(qualifiedNameExpr.getName().equals("antlrjavaparser"));

        assertTrue("Expected qualifier to be github.  Found \"" + qualifiedNameExpr.getQualifier().getName() + "\" instead.",
                qualifiedNameExpr.getQualifier().getName().equals("github"));

        assertTrue("Expected asterisk property to be set.", importDeclaration.isAsterisk());
    }

}