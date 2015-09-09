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
import com.github.antlrjavaparser.ParseException;
import com.github.antlrjavaparser.api.type.PrimitiveType;

public class PrimitiveTypeContextAdapter implements Adapter<PrimitiveType, Java8Parser.PrimitiveTypeContext> {
    public PrimitiveType adapt(Java8Parser.PrimitiveTypeContext context, AdapterParameters adapterParameters) {

        PrimitiveType primitiveType;

        if (context.BOOLEAN() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Boolean);
        } else if (context.BYTE() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Byte);
        } else if (context.CHAR() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Char);
        } else if (context.DOUBLE() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Double);
        } else if (context.FLOAT() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Float);
        } else if (context.INT() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Int);
        } else if (context.LONG() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Long);
        } else if (context.SHORT() != null) {
            primitiveType = new PrimitiveType(PrimitiveType.Primitive.Short);
        } else {
            throw new ParseException("Unknown primitive type");
        }

        AdapterUtil.setComments(primitiveType, context, adapterParameters);
        AdapterUtil.setPosition(primitiveType, context);

        return primitiveType;
    }
}