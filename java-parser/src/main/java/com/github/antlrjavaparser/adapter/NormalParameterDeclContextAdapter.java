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
import com.github.antlrjavaparser.api.type.ReferenceType;

public class NormalParameterDeclContextAdapter implements Adapter<Parameter, Java8Parser.NormalParameterDeclContext> {
    public Parameter adapt(Java8Parser.NormalParameterDeclContext context, AdapterParameters adapterParameters) {
        /*
        normalParameterDecl
            :   variableModifiers type Identifier (LBRACKET RBRACKET)*
            ;
         */
        Parameter parameter = new Parameter();
        AdapterUtil.setComments(parameter, context, adapterParameters);
        AdapterUtil.setPosition(parameter, context);

        AdapterUtil.setVariableModifiers(context.variableModifiers(), parameter, adapterParameters);
        parameter.setType(Adapters.getTypeContextAdapter().adapt(context.type(), adapterParameters));

        VariableDeclaratorId variableDeclaratorId = new VariableDeclaratorId();
        variableDeclaratorId.setName(context.Identifier().getText());
        parameter.setId(variableDeclaratorId);

        if (context.LBRACKET() != null && context.LBRACKET().size() > 0) {
            variableDeclaratorId.setArrayCount(context.LBRACKET().size());
        }

        return parameter;
    }
}