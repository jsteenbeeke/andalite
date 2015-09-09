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
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;

/**
 * User: mdehaan
 * Date: 8/22/2014
 */
public class MethodReferenceContextAdapter implements Adapter<Expression, Java8Parser.MethodReferenceContext> {
    @Override
    public Expression adapt(Java8Parser.MethodReferenceContext context, AdapterParameters adapterParameters) {

        if (context.lambdaExpression() != null) {
            return Adapters.getLambdaExpressionContextAdapter().adapt(context.lambdaExpression(), adapterParameters);
        }

        // TODO: Store the method reference data in the data tree

        // Otherwise return a method reference

        return new MethodReferenceExpr();
    }
}