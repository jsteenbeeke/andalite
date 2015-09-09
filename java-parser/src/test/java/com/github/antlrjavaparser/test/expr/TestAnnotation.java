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
package com.github.antlrjavaparser.test.expr;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.expr.AnnotationExpr;
import com.github.antlrjavaparser.api.expr.ClassExpr;
import com.github.antlrjavaparser.api.expr.NormalAnnotationExpr;
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;
import com.github.antlrjavaparser.api.type.ReferenceType;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 1/3/13 Time: 3:35 PM
 */
public class TestAnnotation {
	@Test
	public void testNormalAnnotation() throws Exception {
		String javaSource = "@RooWebScaffold(formBackingObject = Timer.class)\n"
				+ "public class TimerController {\n" + "}";

		CompilationUnit compilationUnit = JavaParser.parse(javaSource);
		List<AnnotationExpr> annotationExprList = compilationUnit.getTypes()
				.get(0).getAnnotations();

		assertTrue(annotationExprList.size() == 1);
		assertTrue(annotationExprList.get(0) instanceof NormalAnnotationExpr);

		NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) annotationExprList
				.get(0);
		assertTrue(normalAnnotationExpr.getPairs().size() == 1);
		assertEquals("formBackingObject", normalAnnotationExpr.getPairs()
				.get(0).getName());

		assertTrue(normalAnnotationExpr.getPairs().get(0).getValue() instanceof ClassExpr);
		ClassExpr classExpr = (ClassExpr) normalAnnotationExpr.getPairs()
				.get(0).getValue();

		assertTrue(classExpr.getType() instanceof ReferenceType);

		ReferenceType referenceType = (ReferenceType) classExpr.getType();
		assertTrue(referenceType.getType() instanceof ClassOrInterfaceType);

		ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) referenceType
				.getType();
		assertEquals("Timer", classOrInterfaceType.getNameAsString());
		assertNull(classOrInterfaceType.getScope());

	}
}