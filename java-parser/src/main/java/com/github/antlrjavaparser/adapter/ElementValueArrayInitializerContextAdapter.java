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
import com.github.antlrjavaparser.api.expr.ArrayInitializerExpr;
import com.github.antlrjavaparser.api.expr.Expression;

public class ElementValueArrayInitializerContextAdapter implements Adapter<ArrayInitializerExpr, Java8Parser.ElementValueArrayInitializerContext> {
    public ArrayInitializerExpr adapt(Java8Parser.ElementValueArrayInitializerContext context, AdapterParameters adapterParameters) {
        /*
            elementValueArrayInitializer
            :   LBRACE
                    (elementValue
                            (COMMA elementValue
                            )*
                    )? (COMMA)? RBRACE
            ;
        */
        ArrayInitializerExpr arrayInitializerExpr = new ArrayInitializerExpr();
        AdapterUtil.setComments(arrayInitializerExpr, context, adapterParameters);
        AdapterUtil.setPosition(arrayInitializerExpr, context);

        List<Expression> expressionList = new LinkedList<Expression>();
        for (Java8Parser.ElementValueContext elementValueContext : context.elementValue()) {
            expressionList.add(Adapters.getElementValueContextAdapter().adapt(elementValueContext, adapterParameters));
        }
        arrayInitializerExpr.setValues(expressionList);

        return arrayInitializerExpr;
    }
}
