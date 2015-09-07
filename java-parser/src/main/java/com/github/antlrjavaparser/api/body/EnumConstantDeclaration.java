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
package com.github.antlrjavaparser.api.body;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.antlrjavaparser.api.expr.AnnotationExpr;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.visitor.GenericVisitor;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class EnumConstantDeclaration extends BodyDeclaration {

	private TerminalNode name;

	private List<Expression> args;

	private List<BodyDeclaration> classBody;

	public EnumConstantDeclaration() {
	}

	public EnumConstantDeclaration(TerminalNode name) {
		this.name = name;
	}

	public EnumConstantDeclaration(JavadocComment javaDoc,
			List<AnnotationExpr> annotations, TerminalNode name,
			List<Expression> args, List<BodyDeclaration> classBody) {
		super(annotations, javaDoc);
		this.name = name;
		this.args = args;
		this.classBody = classBody;
	}

	public EnumConstantDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, JavadocComment javaDoc,
			List<AnnotationExpr> annotations, TerminalNode name,
			List<Expression> args, List<BodyDeclaration> classBody) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.name = name;
		this.args = args;
		this.classBody = classBody;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public List<Expression> getArgs() {
		return args;
	}

	public List<BodyDeclaration> getClassBody() {
		return classBody;
	}

	public TerminalNode getName() {
		return name;
	}

	public void setArgs(List<Expression> args) {
		this.args = args;
	}

	public void setClassBody(List<BodyDeclaration> classBody) {
		this.classBody = classBody;
	}

	public void setName(TerminalNode name) {
		this.name = name;
	}
}
