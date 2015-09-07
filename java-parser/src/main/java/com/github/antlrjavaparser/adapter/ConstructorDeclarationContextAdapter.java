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
import com.github.antlrjavaparser.api.body.ConstructorDeclaration;

public class ConstructorDeclarationContextAdapter implements Adapter<ConstructorDeclaration, Java8Parser.ConstructorDeclarationContext> {
    @Override
    public ConstructorDeclaration adapt(Java8Parser.ConstructorDeclarationContext context, AdapterParameters adapterParameters) {

        ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration();
        AdapterUtil.setModifiers(context.modifiers(), constructorDeclaration, adapterParameters);
        AdapterUtil.setComments(constructorDeclaration, context, adapterParameters);
        AdapterUtil.setPosition(constructorDeclaration, context);

        constructorDeclaration.setName(context.Identifier().getText());
        constructorDeclaration.setTypeParameters(Adapters.getTypeParametersContextAdapter().adapt(context.typeParameters(), adapterParameters));
        constructorDeclaration.setBlock(Adapters.getConstructorBlockContextAdapter().adapt(context.constructorBlock(), adapterParameters));
        constructorDeclaration.setThrows(Adapters.getQualifiedNameListContextAdapter().adapt(context.qualifiedNameList(), adapterParameters));
        constructorDeclaration.setParameters(Adapters.getFormalParametersContextAdapter().adapt(context.formalParameters(), adapterParameters));

        return constructorDeclaration;
    }
}
