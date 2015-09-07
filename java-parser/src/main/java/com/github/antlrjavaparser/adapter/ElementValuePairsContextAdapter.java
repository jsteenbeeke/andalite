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
import com.github.antlrjavaparser.api.Node;
import com.github.antlrjavaparser.api.expr.MemberValuePair;

import java.util.LinkedList;
import java.util.List;

public class ElementValuePairsContextAdapter implements Adapter<List<MemberValuePair>, Java8Parser.ElementValuePairsContext> {
    public List<MemberValuePair> adapt(Java8Parser.ElementValuePairsContext context, AdapterParameters adapterParameters) {

        /*
        elementValuePairs
            :   elementValuePair
                (COMMA elementValuePair
                )*
            ;
         */

        List<MemberValuePair> memberValuePairList = new LinkedList<MemberValuePair>();
        for (Java8Parser.ElementValuePairContext elementValuePairContext : context.elementValuePair()) {
            memberValuePairList.add(Adapters.getElementValuePairContextAdapter().adapt(elementValuePairContext, adapterParameters));
        }

        return memberValuePairList;
    }
}
