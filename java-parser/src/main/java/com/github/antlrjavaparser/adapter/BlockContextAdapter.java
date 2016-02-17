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
import com.github.antlrjavaparser.api.stmt.BlockStmt;
import com.github.antlrjavaparser.api.stmt.Statement;

public class BlockContextAdapter implements Adapter<BlockStmt, Java8Parser.BlockContext> {
    public BlockStmt adapt(Java8Parser.BlockContext context, AdapterParameters adapterParameters) {
        BlockStmt blockStmt = new BlockStmt();
        AdapterUtil.setComments(blockStmt, context, adapterParameters);
        AdapterUtil.setPosition(blockStmt, context);

        List<Statement> blockStmtList = new LinkedList<Statement>();
        for (Java8Parser.BlockStatementContext blockStatementContext : context.blockStatement()) {
            blockStmtList.add(Adapters.getBlockStatementContextAdapter().adapt(blockStatementContext, adapterParameters));
        }

        if (blockStmtList.size() > 0) {
            blockStmt.setStmts(blockStmtList);
        } else {
            // This is a block with no statements
            // We still need to go in and grab the comments
            AdapterUtil.setInternalComments(blockStmt, context, adapterParameters);
        }

        return blockStmt;
    }
}
