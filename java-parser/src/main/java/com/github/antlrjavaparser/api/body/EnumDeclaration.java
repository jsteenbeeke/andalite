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
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;
import com.github.antlrjavaparser.api.visitor.GenericVisitor;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class EnumDeclaration extends TypeDeclaration {

	private List<ClassOrInterfaceType> implementsList;

	private List<EnumConstantDeclaration> entries;
	
	private TerminalNode constantAndBodySeparator;

	public EnumDeclaration() {
	}

	public EnumDeclaration(int modifiers, TerminalNode name) {
		super(modifiers, name);
	}

	public EnumDeclaration(JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, TerminalNode name,
			List<ClassOrInterfaceType> implementsList,
			List<EnumConstantDeclaration> entries, List<BodyDeclaration> members) {
		super(annotations, javaDoc, modifiers, name, members);
		this.implementsList = implementsList;
		this.entries = entries;
	}

	public EnumDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, TerminalNode name,
			List<ClassOrInterfaceType> implementsList,
			List<EnumConstantDeclaration> entries, List<BodyDeclaration> members) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc,
				modifiers, name, members);
		this.implementsList = implementsList;
		this.entries = entries;
	}
	
	public TerminalNode getConstantAndBodySeparator() {
		return constantAndBodySeparator;
	}
	
	public void setConstantAndBodySeparator(
			TerminalNode constantAndBodySeparator) {
		this.constantAndBodySeparator = constantAndBodySeparator;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public List<EnumConstantDeclaration> getEntries() {
		return entries;
	}

	public List<ClassOrInterfaceType> getImplements() {
		return implementsList;
	}

	public void setEntries(List<EnumConstantDeclaration> entries) {
		this.entries = entries;
	}

	public void setImplements(List<ClassOrInterfaceType> implementsList) {
		this.implementsList = implementsList;
	}
}
