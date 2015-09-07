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
import com.github.antlrjavaparser.api.stmt.Statement;
import com.github.antlrjavaparser.api.stmt.TryStmt;

public class TryWithResourcesContextAdapter implements Adapter<Statement, Java8Parser.TryWithResourcesContext> {
    public Statement adapt(Java8Parser.TryWithResourcesContext context, AdapterParameters adapterParameters) {

        /*
        tryWithResources
            :    TRY resourceSpecification block catches? (FINALLY block)?
            ;

        resourceSpecification
            :    '(' resources (';')? ')'                                                                      // ( Resources [;] )
            ;

        resources
            :    resource ( ';' resource )*                                                                // Resource { ; Resource }
            ;

        resource
            :    variableModifiers? type Identifier EQ expression                       // {VariableModifier} ReferenceType VariableDeclaratorId = Expression
            ;

         */

        TryStmt tryStmt = new TryStmt();
        AdapterUtil.setComments(tryStmt, context, adapterParameters);
        AdapterUtil.setPosition(tryStmt, context);
        tryStmt.setTryBlock(Adapters.getBlockContextAdapter().adapt(context.block(0), adapterParameters));

        // Set resources
        if (context.resourceSpecification() != null) {
            tryStmt.setResources(Adapters.getResourcesContextAdapter().adapt(context.resourceSpecification().resources(), adapterParameters));
        }

        if (context.catches() != null) {
            tryStmt.setCatchs(Adapters.getCatchesContextAdapter().adapt(context.catches(), adapterParameters));
        }

        if (context.FINALLY() != null) {
            tryStmt.setFinallyBlock(Adapters.getBlockContextAdapter().adapt(context.block(1), adapterParameters));
        }

        return tryStmt;
    }
}
