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

import com.github.antlrjavaparser.api.TypeParameter;
import com.github.antlrjavaparser.api.expr.AnnotationExpr;
import com.github.antlrjavaparser.api.expr.NameExpr;
import com.github.antlrjavaparser.api.stmt.BlockStmt;
import com.github.antlrjavaparser.api.visitor.GenericVisitor;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ConstructorDeclaration extends BodyDeclaration {

	private int modifiers;

	private List<TypeParameter> typeParameters;

	private String name;

	private List<Parameter> parameters;

	private List<NameExpr> throws_;

	private BlockStmt block;

	private TerminalNode parametersStart;

	public ConstructorDeclaration() {
	}

	public ConstructorDeclaration(int modifiers, String name) {
		this.modifiers = modifiers;
		this.name = name;
	}

	public ConstructorDeclaration(JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations,
			List<TypeParameter> typeParameters, String name,
			List<Parameter> parameters, List<NameExpr> throws_, BlockStmt block) {
		super(annotations, javaDoc);
		this.modifiers = modifiers;
		this.typeParameters = typeParameters;
		this.name = name;
		this.parameters = parameters;
		this.throws_ = throws_;
		this.block = block;
	}

	public ConstructorDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations,
			List<TypeParameter> typeParameters, String name,
			List<Parameter> parameters, List<NameExpr> throws_, BlockStmt block) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.modifiers = modifiers;
		this.typeParameters = typeParameters;
		this.name = name;
		this.parameters = parameters;
		this.throws_ = throws_;
		this.block = block;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public BlockStmt getBlock() {
		return block;
	}

	/**
	 * Return the modifiers of this member declaration.
	 *
	 * @see ModifierSet
	 * @return modifiers
	 */
	public int getModifiers() {
		return modifiers;
	}

	public String getName() {
		return name;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<NameExpr> getThrows() {
		return throws_;
	}

	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}

	public void setBlock(BlockStmt block) {
		this.block = block;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setThrows(List<NameExpr> throws_) {
		this.throws_ = throws_;
	}

	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}

	public TerminalNode getParametersStart() {
		return parametersStart;
	}

	public void setParametersStart(TerminalNode parametersStart) {
		this.parametersStart = parametersStart;
	}

}
