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
import com.github.antlrjavaparser.api.stmt.ExplicitConstructorInvocationStmt;
import com.github.antlrjavaparser.api.stmt.Statement;

public class ExplicitConstructorInvocationContextAdapter implements Adapter<Statement, Java8Parser.ExplicitConstructorInvocationContext> {
    public Statement adapt(Java8Parser.ExplicitConstructorInvocationContext context, AdapterParameters adapterParameters) {
        /*
        explicitConstructorInvocation
            :               (nonWildcardTypeArguments)? (THIS|SUPER) arguments SEMI
            |   primary DOT (nonWildcardTypeArguments)? SUPER arguments SEMI
            ;

            nonWildcardTypeArguments
                :   LT typeList GT
                ;
         */

        ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt = new ExplicitConstructorInvocationStmt();
        AdapterUtil.setComments(explicitConstructorInvocationStmt, context, adapterParameters);
        AdapterUtil.setPosition(explicitConstructorInvocationStmt, context);

        explicitConstructorInvocationStmt.setArgs(Adapters.getArgumentsContextAdapter().adapt(context.arguments(), adapterParameters));
        explicitConstructorInvocationStmt.setThis(context.THIS() != null);

        if (context.nonWildcardTypeArguments() != null) {
            explicitConstructorInvocationStmt.setTypeArgs(Adapters.getTypeListContextAdapter().adapt(context.nonWildcardTypeArguments().typeList(), adapterParameters));
        }

        /*
        if (context.SUPER() != null) {
            SuperExpr superExpr = new SuperExpr();
            explicitConstructorInvocationStmt.setExpr(superExpr);
        } else if (context.THIS() != null) {
            ThisExpr thisExpr = new ThisExpr();
            explicitConstructorInvocationStmt.setExpr(thisExpr);
        }
*/
        if (context.primary() != null) {
            explicitConstructorInvocationStmt.setExpr(Adapters.getPrimaryContextAdapter().adapt(context.primary(), adapterParameters));
        }

        return explicitConstructorInvocationStmt;
    }
}
