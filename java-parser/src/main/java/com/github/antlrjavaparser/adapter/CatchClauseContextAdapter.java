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
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.VariableDeclaratorId;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.type.Type;

import java.util.LinkedList;
import java.util.List;

public class CatchClauseContextAdapter implements Adapter<CatchClause, Java8Parser.CatchClauseContext> {
    public CatchClause adapt(Java8Parser.CatchClauseContext context, AdapterParameters adapterParameters) {

        /*
        catchClause
            :   CATCH LPAREN catchFormalParameter RPAREN block
            ;

        catchFormalParameter
            :    variableModifiers type (BAR type)* Identifier (LBRACKET RBRACKET)*
            ;

        */

        CatchClause catchClause = new CatchClause();
        AdapterUtil.setComments(catchClause, context, adapterParameters);
        AdapterUtil.setPosition(catchClause, context);

        CatchParameter parameter = new CatchParameter();
        AdapterUtil.setComments(parameter, context.catchFormalParameter(), adapterParameters);
        AdapterUtil.setPosition(parameter, context);
        AdapterUtil.setVariableModifiers(context.catchFormalParameter().variableModifiers(), parameter, adapterParameters);

        List<Type> typeList = new LinkedList<Type>();
        for (Java8Parser.TypeContext typeContext : context.catchFormalParameter().type()) {
            typeList.add(Adapters.getTypeContextAdapter().adapt(typeContext, adapterParameters));
        }
        parameter.setTypeList(typeList);

        VariableDeclaratorId variableDeclaratorId = new VariableDeclaratorId();
        variableDeclaratorId.setName(context.catchFormalParameter().Identifier().getText());
        parameter.setId(variableDeclaratorId);

        // TODO: Figure out how this is handled with multi-catch
        /*
        if (context.catchFormalParameter().LBRACKET() != null && context.catchFormalParameter().LBRACKET().size() > 0) {
            ReferenceType referenceType = (ReferenceType)parameter.getType();
            referenceType.setArrayCount(referenceType.getArrayCount() + context.catchFormalParameter().LBRACKET().size());
        }
        */

        catchClause.setExcept(parameter);
        catchClause.setCatchBlock(Adapters.getBlockContextAdapter().adapt(context.block(), adapterParameters));

        return catchClause;
    }
}