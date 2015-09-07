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

public class ElementValueContextAdapter implements Adapter<Expression, Java8Parser.ElementValueContext> {
    public Expression adapt(Java8Parser.ElementValueContext context, AdapterParameters adapterParameters) {

        if (context.conditionalExpression() != null) {
            return Adapters.getConditionalExpressionContextAdapter().adapt(context.conditionalExpression(), adapterParameters);
        } else if (context.annotation() != null) {
            return Adapters.getAnnotationContextAdapter().adapt(context.annotation(), adapterParameters);
        } else if (context.elementValueArrayInitializer() != null) {
            return Adapters.getElementValueArrayInitializerContextAdapter().adapt(context.elementValueArrayInitializer(), adapterParameters);
        }

        throw new RuntimeException("Unknown Element Value type");
    }
}
