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
import com.github.antlrjavaparser.api.stmt.CatchClause;

public class CatchesContextAdapter implements Adapter<List<CatchClause>, Java8Parser.CatchesContext> {
    public List<CatchClause> adapt(Java8Parser.CatchesContext context, AdapterParameters adapterParameters) {

        List<CatchClause> catchClauseList = new LinkedList<CatchClause>();
        for (Java8Parser.CatchClauseContext catchClauseContext : context.catchClause()) {
            catchClauseList.add(Adapters.getCatchClauseContextAdapter().adapt(catchClauseContext, adapterParameters));
        }

        return catchClauseList;
    }
}
