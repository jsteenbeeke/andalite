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
import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;
import com.github.antlrjavaparser.api.type.ReferenceType;
import com.github.antlrjavaparser.api.type.Type;

public class NormalClassDeclarationContextAdapter implements
		Adapter<TypeDeclaration, Java8Parser.NormalClassDeclarationContext> {
	public TypeDeclaration adapt(
			Java8Parser.NormalClassDeclarationContext context,
			AdapterParameters adapterParameters) {

		ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration();
		AdapterUtil.setModifiers(context.modifiers(),
				classOrInterfaceDeclaration, adapterParameters);
		AdapterUtil.setComments(classOrInterfaceDeclaration, context,
				adapterParameters);
		AdapterUtil.setPosition(classOrInterfaceDeclaration, context);
		classOrInterfaceDeclaration.setInterface(false);
		classOrInterfaceDeclaration.setName(context.Identifier());

		if (context.type() != null) {
			List<ClassOrInterfaceType> classOrInterfaceTypeList = new LinkedList<ClassOrInterfaceType>();

			// In this case, context.type() has to be a reference type since you
			// cannot extend from a primitive
			// Though the declaration is expecting a ClassOrInterfaceType rather
			// than a ReferenceType
			ReferenceType referenceType = (ReferenceType) Adapters
					.getTypeContextAdapter().adapt(context.type(),
							adapterParameters);

			ClassOrInterfaceType extendsClassOrInterfaceType = (ClassOrInterfaceType) referenceType
					.getType();
			extendsClassOrInterfaceType.setBeginColumn(referenceType.getBeginColumn());
			extendsClassOrInterfaceType.setBeginLine(referenceType.getBeginLine());
			extendsClassOrInterfaceType.setBeginIndex(referenceType.getBeginIndex());
			extendsClassOrInterfaceType.setEndColumn(referenceType.getEndColumn());
			extendsClassOrInterfaceType.setEndLine(referenceType.getEndLine());
			extendsClassOrInterfaceType.setEndIndex(referenceType.getEndIndex());
			
			classOrInterfaceTypeList.add(extendsClassOrInterfaceType);
			classOrInterfaceDeclaration.setExtends(classOrInterfaceTypeList);
		}

		List<Type> typeList = Adapters.getTypeListContextAdapter().adapt(
				context.typeList(), adapterParameters);
		classOrInterfaceDeclaration.setImplements(AdapterUtil
				.convertTypeList(typeList));
		classOrInterfaceDeclaration.setTypeParameters(Adapters
				.getTypeParametersContextAdapter().adapt(
						context.typeParameters(), adapterParameters));

		classOrInterfaceDeclaration.setMembers(Adapters
				.getClassBodyContextAdapter().adapt(context.classBody(),
						adapterParameters));

		return classOrInterfaceDeclaration;
	}
}
