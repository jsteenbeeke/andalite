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
package com.github.antlrjavaparser.adapter;

import java.util.LinkedList;
import java.util.List;

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;

public class ClassOrInterfaceTypeContextAdapter implements
		Adapter<ClassOrInterfaceType, Java8Parser.ClassOrInterfaceTypeContext> {
	public ClassOrInterfaceType adapt(
			Java8Parser.ClassOrInterfaceTypeContext context,
			AdapterParameters adapterParameters) {

		ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType();
		AdapterUtil.setComments(classOrInterfaceType, context,
				adapterParameters);
		AdapterUtil.setPosition(classOrInterfaceType, context);

		List<ClassOrInterfaceType> scopes = new LinkedList<ClassOrInterfaceType>();

		for (Java8Parser.IdentifierTypeArgumentContext identifierTypeArgumentContext : context
				.identifierTypeArgument()) {

			ClassOrInterfaceType scope = new ClassOrInterfaceType();
			scope.setTypeArgs(Adapters.getTypeArgumentsContextAdapter().adapt(
					identifierTypeArgumentContext.typeArguments(),
					adapterParameters));
			scope.setName(identifierTypeArgumentContext.Identifier());

			scopes.add(scope);
		}

		// This is a weird way of handling this, but should take care of the
		// scope hierarchy
		classOrInterfaceType = scopes.get(scopes.size() - 1);
		ClassOrInterfaceType currentScope = classOrInterfaceType;
		for (int i = (scopes.size() - 2); i >= 0; i--) {
			ClassOrInterfaceType scope = scopes.get(i);
			currentScope.setScope(scope);
			currentScope = scope;
		}

		return classOrInterfaceType;
	}
}
