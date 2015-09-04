/*
 * Copyright (C) 2015 Julio Vilmar Gesser and Mike DeHaan
 *
 * This file is part of antlr-java-parser.
 *
 * antlr-java-parser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * antlr-java-parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with antlr-java-parser.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.github.antlrjavaparser.api.body;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.antlrjavaparser.api.expr.AnnotationExpr;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class TypeDeclaration extends BodyDeclaration {

	private TerminalNode name;

	private int modifiers;

	private List<BodyDeclaration> members;

	public TypeDeclaration() {
	}

	public TypeDeclaration(int modifiers, TerminalNode name) {
		this.name = name;
		this.modifiers = modifiers;
	}

	public TypeDeclaration(List<AnnotationExpr> annotations,
			JavadocComment javaDoc, int modifiers, TerminalNode name,
			List<BodyDeclaration> members) {
		super(annotations, javaDoc);
		this.name = name;
		this.modifiers = modifiers;
		this.members = members;
	}

	public TypeDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, List<AnnotationExpr> annotations,
			JavadocComment javaDoc, int modifiers, TerminalNode name,
			List<BodyDeclaration> members) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.name = name;
		this.modifiers = modifiers;
		this.members = members;
	}

	public final List<BodyDeclaration> getMembers() {
		return members;
	}

	/**
	 * Return the modifiers of this type declaration.
	 *
	 * @see ModifierSet
	 * @return modifiers
	 */
	public final int getModifiers() {
		return modifiers;
	}

	public final TerminalNode getName() {
		return name;
	}

	public final String getNameAsString() {
		return name.getText();
	}

	public void setMembers(List<BodyDeclaration> members) {
		this.members = members;
	}

	public final void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public final void setName(TerminalNode name) {
		this.name = name;
	}
}
