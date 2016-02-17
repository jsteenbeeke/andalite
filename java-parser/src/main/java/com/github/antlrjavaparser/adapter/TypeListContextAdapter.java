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
import com.github.antlrjavaparser.api.type.Type;

public class TypeListContextAdapter implements Adapter<List<Type>, Java8Parser.TypeListContext> {
    public List<Type> adapt(Java8Parser.TypeListContext context, AdapterParameters adapterParameters) {

        if (context == null) {
            return null;
        }

        List<Type> typeList = new LinkedList<Type>();
        for (Java8Parser.TypeContext typeContext : context.type()) {
            typeList.add(Adapters.getTypeContextAdapter().adapt(typeContext, adapterParameters));
        }

        return typeList;
    }
}
