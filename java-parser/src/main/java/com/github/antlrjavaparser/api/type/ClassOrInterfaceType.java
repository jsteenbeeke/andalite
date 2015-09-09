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
package com.github.antlrjavaparser.api.type;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.antlrjavaparser.api.visitor.GenericVisitor;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ClassOrInterfaceType extends Type {

	private ClassOrInterfaceType scope;

	private TerminalNode name;

	private List<Type> typeArgs;

	public ClassOrInterfaceType() {
	}

	public ClassOrInterfaceType(TerminalNode name) {
		this.name = name;
	}

	public ClassOrInterfaceType(ClassOrInterfaceType scope, TerminalNode name) {
		this.scope = scope;
		this.name = name;
	}

	public ClassOrInterfaceType(int beginLine, int beginColumn, int endLine,
			int endColumn, ClassOrInterfaceType scope, TerminalNode name,
			List<Type> typeArgs) {
		super(beginLine, beginColumn, endLine, endColumn);
		this.scope = scope;
		this.name = name;
		this.typeArgs = typeArgs;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public TerminalNode getName() {
		return name;
	}

	public String getNameAsString() {
		return name.getText();
	}

	public ClassOrInterfaceType getScope() {
		return scope;
	}

	public List<Type> getTypeArgs() {
		return typeArgs;
	}

	public void setName(TerminalNode name) {
		this.name = name;
	}

	public void setScope(ClassOrInterfaceType scope) {
		this.scope = scope;
	}

	public void setTypeArgs(List<Type> typeArgs) {
		this.typeArgs = typeArgs;
	}
}