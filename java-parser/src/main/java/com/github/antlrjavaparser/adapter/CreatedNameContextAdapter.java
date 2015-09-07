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
import com.github.antlrjavaparser.api.type.Type;

public class CreatedNameContextAdapter implements Adapter<Type, Java8Parser.CreatedNameContext> {
    public Type adapt(Java8Parser.CreatedNameContext context, AdapterParameters adapterParameters) {

        if (context.classOrInterfaceType() != null) {
            return Adapters.getClassOrInterfaceTypeContextAdapter().adapt(context.classOrInterfaceType(), adapterParameters);
        } else if (context.primitiveType() != null) {
            return Adapters.getPrimitiveTypeContextAdapter().adapt(context.primitiveType(), adapterParameters);
        }

        throw new RuntimeException("Unknown created name type");
    }
}
