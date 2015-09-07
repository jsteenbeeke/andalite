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

public class ElementValuePairContextAdapter implements Adapter<MemberValuePair, Java8Parser.ElementValuePairContext> {
    public MemberValuePair adapt(Java8Parser.ElementValuePairContext context, AdapterParameters adapterParameters) {

        /*
        elementValuePair
        :   Identifier EQ elementValue
        ;
        */

        MemberValuePair memberValuePair = new MemberValuePair();
        AdapterUtil.setComments(memberValuePair, context, adapterParameters);
        AdapterUtil.setPosition(memberValuePair, context);
        memberValuePair.setName(context.Identifier().getText());
        memberValuePair.setValue(Adapters.getElementValueContextAdapter().adapt(context.elementValue(), adapterParameters));

        return memberValuePair;
    }
}
