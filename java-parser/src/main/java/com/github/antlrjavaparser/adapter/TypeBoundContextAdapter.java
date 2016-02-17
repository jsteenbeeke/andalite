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
import com.github.antlrjavaparser.api.type.Type;

public class TypeBoundContextAdapter implements Adapter<List<ClassOrInterfaceType>, Java8Parser.TypeBoundContext> {
    public List<ClassOrInterfaceType> adapt(Java8Parser.TypeBoundContext context, AdapterParameters adapterParameters) {

        if (context == null) {
            return null;
        }

        List<Type> typeList = new LinkedList<Type>();
        List<ClassOrInterfaceType> classOrInterfaceTypeList = new LinkedList<ClassOrInterfaceType>();

        for (Java8Parser.TypeContext typeContext : context.type()) {
            typeList.add(Adapters.getTypeContextAdapter().adapt(typeContext, adapterParameters));
        }

        // All instances should be classOrInterfaceType
        return AdapterUtil.convertTypeList(typeList);
    }
}
