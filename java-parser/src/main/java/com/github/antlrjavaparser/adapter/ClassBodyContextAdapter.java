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
import com.github.antlrjavaparser.api.body.BodyDeclaration;

import java.util.LinkedList;
import java.util.List;

public class ClassBodyContextAdapter implements Adapter<List<BodyDeclaration>, Java8Parser.ClassBodyContext> {
    public List<BodyDeclaration> adapt(Java8Parser.ClassBodyContext context, AdapterParameters adapterParameters) {

        List<BodyDeclaration> bodyDeclarationList = new LinkedList<BodyDeclaration>();
        for (Java8Parser.ClassBodyDeclarationContext classBodyDeclarationContext : context.classBodyDeclaration()) {
            bodyDeclarationList.add(Adapters.getClassBodyDeclarationContextAdapter().adapt(classBodyDeclarationContext, adapterParameters));
        }

        return bodyDeclarationList;
    }
}
