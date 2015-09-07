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

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.api.body.EnumConstantDeclaration;

public class EnumConstantContextAdapter implements
		Adapter<EnumConstantDeclaration, Java8Parser.EnumConstantContext> {
	public EnumConstantDeclaration adapt(
			Java8Parser.EnumConstantContext context,
			AdapterParameters adapterParameters) {

		/*
		 * enumConstant : (annotations)? Identifier(arguments)? (classBody)? ;
		 */

		EnumConstantDeclaration enumConstantDeclaration = new EnumConstantDeclaration();
		AdapterUtil.setComments(enumConstantDeclaration, context,
				adapterParameters);
		AdapterUtil.setPosition(enumConstantDeclaration, context);

		if (context.annotations() != null) {
			enumConstantDeclaration.setAnnotations(Adapters
					.getAnnotationsContextAdapter().adapt(
							context.annotations(), adapterParameters));
		}

		enumConstantDeclaration.setName(context.Identifier());

		if (context.arguments() != null) {
			enumConstantDeclaration.setArgs(Adapters
					.getArgumentsContextAdapter().adapt(context.arguments(),
							adapterParameters));
		}

		if (context.classBody() != null) {
			enumConstantDeclaration.setClassBody(Adapters
					.getClassBodyContextAdapter().adapt(context.classBody(),
							adapterParameters));
		}

		return enumConstantDeclaration;
	}
}
