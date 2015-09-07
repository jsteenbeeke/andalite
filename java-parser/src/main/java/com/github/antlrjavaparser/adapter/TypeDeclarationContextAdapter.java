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
import com.github.antlrjavaparser.api.body.EmptyTypeDeclaration;
import com.github.antlrjavaparser.api.body.TypeDeclaration;

public class TypeDeclarationContextAdapter implements Adapter<TypeDeclaration, Java8Parser.TypeDeclarationContext> {
    public TypeDeclaration adapt(Java8Parser.TypeDeclarationContext context, AdapterParameters adapterParameters) {

        // Determine which Type we're dealing with
        if (context.classOrInterfaceDeclaration() != null) {
            return Adapters.getClassOrInterfaceDeclarationContextAdapter().adapt(context.classOrInterfaceDeclaration(), adapterParameters);
        } else if (context.SEMI() != null) {
            TypeDeclaration typeDeclaration = new EmptyTypeDeclaration();
            AdapterUtil.setComments(typeDeclaration, context, adapterParameters);
            AdapterUtil.setPosition(typeDeclaration, context);
            return typeDeclaration;
        }

        return null;
    }
}
