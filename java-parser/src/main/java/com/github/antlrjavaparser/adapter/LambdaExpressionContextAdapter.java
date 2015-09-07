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
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.LambdaExpr;

/**
 * User: mdehaan
 * Date: 3/19/14
 */
public class LambdaExpressionContextAdapter implements Adapter<Expression, Java8Parser.LambdaExpressionContext> {

    @Override
    public Expression adapt(Java8Parser.LambdaExpressionContext context, AdapterParameters adapterParameters) {

        Expression expression = null;
        if (context.LAMBDA() != null) {

            // TODO: Store Lambda data in tree
            return new LambdaExpr();
        }

        if (context.conditionalExpression() != null) {
            return Adapters.getConditionalExpressionContextAdapter().adapt(context.conditionalExpression(), adapterParameters);
        }

        throw new ParseException("Unknown Expression");
    }

}