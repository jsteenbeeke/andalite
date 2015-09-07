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

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
import com.github.antlrjavaparser.api.type.Type;

public class NormalInterfaceDeclarationContextAdapter
		implements
		Adapter<ClassOrInterfaceDeclaration, Java8Parser.NormalInterfaceDeclarationContext> {
	public ClassOrInterfaceDeclaration adapt(
			Java8Parser.NormalInterfaceDeclarationContext context,
			AdapterParameters adapterParameters) {

		/*
		 * normalInterfaceDeclaration : modifiers INTERFACE Identifier
		 * (typeParameters)? (EXTENDS typeList)? interfaceBody ;
		 */

		ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration();
		AdapterUtil.setModifiers(context.modifiers(),
				classOrInterfaceDeclaration, adapterParameters);
		AdapterUtil.setComments(classOrInterfaceDeclaration, context,
				adapterParameters);
		AdapterUtil.setPosition(classOrInterfaceDeclaration, context);

		classOrInterfaceDeclaration.setInterface(true);

		// All instances should be classOrInterfaceType
		if (context.typeList() != null) {
			List<Type> typeList = Adapters.getTypeListContextAdapter().adapt(
					context.typeList(), adapterParameters);
			classOrInterfaceDeclaration.setExtends(AdapterUtil
					.convertTypeList(typeList));
		}

		if (context.typeParameters() != null) {
			classOrInterfaceDeclaration.setTypeParameters(Adapters
					.getTypeParametersContextAdapter().adapt(
							context.typeParameters(), adapterParameters));
		}

		classOrInterfaceDeclaration.setName(context.Identifier());

		classOrInterfaceDeclaration.setMembers(Adapters
				.getInterfaceBodyContextAdapter().adapt(
						context.interfaceBody(), adapterParameters));

		return classOrInterfaceDeclaration;
	}

	private boolean hasModifier(TerminalNode modifier) {
		return modifier != null;
	}
}
