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
import com.github.antlrjavaparser.api.expr.InstanceOfExpr;
import com.github.antlrjavaparser.api.type.Type;

public class InstanceOfExpressionContextAdapter implements Adapter<Expression, Java8Parser.InstanceOfExpressionContext> {
    public Expression adapt(Java8Parser.InstanceOfExpressionContext context, AdapterParameters adapterParameters) {

        Expression expression = Adapters.getRelationalExpressionContextAdapter().adapt(context.relationalExpression(), adapterParameters);

        if (context.type() != null) {
            Type type = Adapters.getTypeContextAdapter().adapt(context.type(), adapterParameters);
            InstanceOfExpr instanceOfExpr = new InstanceOfExpr();
            instanceOfExpr.setType(type);
            instanceOfExpr.setExpr(expression);
            return instanceOfExpr;
        }

        return expression;
    }
}
