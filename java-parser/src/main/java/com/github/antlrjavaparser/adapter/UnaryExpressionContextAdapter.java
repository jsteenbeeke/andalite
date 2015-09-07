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
import com.github.antlrjavaparser.api.expr.Expression;
import com.github.antlrjavaparser.api.expr.UnaryExpr;

public class UnaryExpressionContextAdapter implements Adapter<Expression, Java8Parser.UnaryExpressionContext> {
    public Expression adapt(Java8Parser.UnaryExpressionContext context, AdapterParameters adapterParameters) {

        if (context.unaryExpressionNotPlusMinus() != null) {
            return Adapters.getUnaryExpressionNotPlusMinusContextAdapter().adapt(context.unaryExpressionNotPlusMinus(), adapterParameters);
        } else {
            UnaryExpr unaryExpr = new UnaryExpr();
            AdapterUtil.setComments(unaryExpr, context, adapterParameters);
            AdapterUtil.setPosition(unaryExpr, context);

            UnaryExpr.Operator operator = null;

            if (context.PLUS() != null) {
                operator = UnaryExpr.Operator.positive;
            } else if (context.PLUSPLUS() != null) {
                operator = UnaryExpr.Operator.preIncrement;
            } else if (context.SUB() != null) {
                operator = UnaryExpr.Operator.negative;
            } else if (context.SUBSUB() != null) {
                operator = UnaryExpr.Operator.preDecrement;
            } else if (context.TILDE() != null) {
                operator = UnaryExpr.Operator.inverse;
            } else if (context.BANG() != null) {
                operator = UnaryExpr.Operator.not;
            }

            unaryExpr.setOperator(operator);
            unaryExpr.setExpr(Adapters.getUnaryExpressionContextAdapter().adapt(context.unaryExpression(), adapterParameters));

            return unaryExpr;
        }
    }
}
