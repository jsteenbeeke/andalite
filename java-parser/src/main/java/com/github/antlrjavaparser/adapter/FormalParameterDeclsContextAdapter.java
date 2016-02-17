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

import java.util.LinkedList;
import java.util.List;

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.api.body.Parameter;

public class FormalParameterDeclsContextAdapter implements Adapter<List<Parameter>, Java8Parser.FormalParameterDeclsContext> {
    public List<Parameter> adapt(Java8Parser.FormalParameterDeclsContext context, AdapterParameters adapterParameters) {

        /*
        formalParameterDecls
        locals [int parameterType]
            :   ellipsisParameterDecl                               {$parameterType = 1;}
            |   normalParameterDecl (COMMA normalParameterDecl)*    {$parameterType = 2;}
            |   (normalParameterDecl COMMA)+ ellipsisParameterDecl  {$parameterType = 3;}
            ;
         */
        List<Parameter> parameterList = new LinkedList<Parameter>();

        if (context.normalParameterDecl() != null && context.normalParameterDecl().size() > 0) {
            for (Java8Parser.NormalParameterDeclContext normalParameterDeclContext : context.normalParameterDecl()) {
                parameterList.add(Adapters.getNormalParameterDeclContextAdapter().adapt(normalParameterDeclContext, adapterParameters));
            }
        }

        if (context.ellipsisParameterDecl() != null) {
            parameterList.add(Adapters.getEllipsisParameterDeclContextAdapter().adapt(context.ellipsisParameterDecl(), adapterParameters));
        }

        return parameterList;
    }
}
