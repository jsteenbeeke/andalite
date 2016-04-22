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
import com.github.antlrjavaparser.api.body.EnumDeclaration;
import com.github.antlrjavaparser.api.type.Type;

public class EnumDeclarationContextAdapter implements
		Adapter<EnumDeclaration, Java8Parser.EnumDeclarationContext> {
	public EnumDeclaration adapt(Java8Parser.EnumDeclarationContext context,
			AdapterParameters adapterParameters) {

		/*
		 * enumDeclaration : modifiers ENUM Identifier (IMPLEMENTS typeList)?
		 * enumBody ;
		 */
		EnumDeclaration enumDeclaration = new EnumDeclaration();
		AdapterUtil.setModifiers(context.modifiers(), enumDeclaration,
				adapterParameters);
		AdapterUtil.setComments(enumDeclaration, context, adapterParameters);
		AdapterUtil.setPosition(enumDeclaration, context);

		if (context.enumBody().enumBodyDeclarations() != null) {
			enumDeclaration.setConstantAndBodySeparator(context.enumBody()
					.enumBodyDeclarations().SEMI());
		}

		enumDeclaration.setName(context.Identifier());
		List<Type> typeList = Adapters.getTypeListContextAdapter().adapt(
				context.typeList(), adapterParameters);
		enumDeclaration.setImplements(AdapterUtil.convertTypeList(typeList));
		enumDeclaration.setMembers(Adapters.getEnumBodyContextAdapter().adapt(
				context.enumBody(), adapterParameters));

		// These come from enumBody

		if (context.enumBody().enumConstants() != null) {
			enumDeclaration.setEntries(Adapters
					.getEnumConstantsContextAdapter().adapt(
							context.enumBody().enumConstants(),
							adapterParameters));
		}

		return enumDeclaration;
	}
}
