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
import com.github.antlrjavaparser.api.expr.ConditionalExpr;
import com.github.antlrjavaparser.api.expr.Expression;

public class ConditionalExpressionContextAdapter implements Adapter<Expression, Java8Parser.ConditionalExpressionContext> {
    public Expression adapt(Java8Parser.ConditionalExpressionContext context, AdapterParameters adapterParameters) {
        Expression expression = Adapters.getConditionalOrExpressionContextAdapter().adapt(context.conditionalOrExpression(), adapterParameters);
        AdapterUtil.setComments(expression, context.expression(1), adapterParameters);
        AdapterUtil.setPosition(expression, context);

        if (context.QUES() != null) {
            Expression thenExpression = Adapters.getExpressionContextAdapter().adapt(context.expression(0), adapterParameters);
            Expression elseExpression = Adapters.getExpressionContextAdapter().adapt(context.expression(1), adapterParameters);

            ConditionalExpr conditionalExpr = new ConditionalExpr();
            conditionalExpr.setCondition(expression);
            conditionalExpr.setThenExpr(thenExpression);
            conditionalExpr.setElseExpr(elseExpression);

            return conditionalExpr;
        } else {
            return expression;
        }
    }
}
