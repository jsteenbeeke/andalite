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
import com.github.antlrjavaparser.api.body.Parameter;
import com.github.antlrjavaparser.api.body.VariableDeclaratorId;

public class EllipsisParameterDeclContextAdapter implements Adapter<Parameter, Java8Parser.EllipsisParameterDeclContext> {
    public Parameter adapt(Java8Parser.EllipsisParameterDeclContext context, AdapterParameters adapterParameters) {

        /*
        ellipsisParameterDecl
            :   variableModifiers
                type  ELLIPSIS
                Identifier
            ;
         */

        Parameter parameter = new Parameter();
        AdapterUtil.setVariableModifiers(context.variableModifiers(), parameter, adapterParameters);
        AdapterUtil.setComments(parameter, context, adapterParameters);
        AdapterUtil.setPosition(parameter, context);

        parameter.setType(Adapters.getTypeContextAdapter().adapt(context.type(), adapterParameters));
        parameter.setVarArgs(true);

        VariableDeclaratorId variableDeclaratorId = new VariableDeclaratorId();
        variableDeclaratorId.setName(context.Identifier().getText());

        parameter.setId(variableDeclaratorId);

        return parameter;
    }
}
