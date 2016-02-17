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

import java.util.List;

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.api.expr.Expression;

public class ArgumentsContextAdapter implements Adapter<List<Expression>, Java8Parser.ArgumentsContext> {
    public List<Expression> adapt(Java8Parser.ArgumentsContext context, AdapterParameters adapterParameters) {
        /*
            arguments
                :   LPAREN (expressionList
                    )? RPAREN
                ;
         */

        if (context.expressionList() != null) {
            return Adapters.getExpressionListContextAdapter().adapt(context.expressionList(), adapterParameters);
        } else {
            // This is allowed to be empty/null
            return null;
        }

        //throw new UnsupportedOperationException();
    }
}
