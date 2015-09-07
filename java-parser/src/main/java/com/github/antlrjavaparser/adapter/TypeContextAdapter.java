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
import com.github.antlrjavaparser.api.type.ReferenceType;
import com.github.antlrjavaparser.api.type.Type;

public class TypeContextAdapter implements Adapter<Type, Java8Parser.TypeContext> {
    public Type adapt(Java8Parser.TypeContext context, AdapterParameters adapterParameters) {

        if (context.classOrInterfaceType() != null) {
            ReferenceType referenceType = new ReferenceType();
            AdapterUtil.setComments(referenceType, context, adapterParameters);
            AdapterUtil.setPosition(referenceType, context);

            referenceType.setType(Adapters.getClassOrInterfaceTypeContextAdapter().adapt(context.classOrInterfaceType(), adapterParameters));
            if (context.LBRACKET() != null && context.LBRACKET().size() > 0) {
                referenceType.setArrayCount(context.LBRACKET().size());
            }
            return referenceType;
        } else if (context.primitiveType() != null) {

            // If there's an array in the mix, this becomes a referenceType
            if (context.LBRACKET() != null && context.LBRACKET().size() > 0) {
                ReferenceType referenceType = new ReferenceType();
                AdapterUtil.setComments(referenceType, context, adapterParameters);
                AdapterUtil.setPosition(referenceType, context);

                referenceType.setType(Adapters.getPrimitiveTypeContextAdapter().adapt(context.primitiveType(), adapterParameters));
                referenceType.setArrayCount(context.LBRACKET().size());
                return referenceType;
            } else {
                return Adapters.getPrimitiveTypeContextAdapter().adapt(context.primitiveType(), adapterParameters);
            }
        }

        return null;
    }
}
