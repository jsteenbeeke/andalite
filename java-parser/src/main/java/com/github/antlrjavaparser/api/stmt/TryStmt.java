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
package com.github.antlrjavaparser.api.stmt;

import java.util.List;

import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.visitor.GenericVisitor;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class TryStmt extends Statement {

    private BlockStmt tryBlock;

    private List<CatchClause> catchs;

    private List<Resource> resources;

    private BlockStmt finallyBlock;

    public TryStmt() {
    }

    public TryStmt(BlockStmt tryBlock, List<CatchClause> catchs, BlockStmt finallyBlock) {
        this.tryBlock = tryBlock;
        this.catchs = catchs;
        this.finallyBlock = finallyBlock;
    }

    public TryStmt(int beginLine, int beginColumn, int endLine, int endColumn, BlockStmt tryBlock, List<CatchClause> catchs, BlockStmt finallyBlock) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.tryBlock = tryBlock;
        this.catchs = catchs;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<CatchClause> getCatchs() {
        return catchs;
    }

    public BlockStmt getFinallyBlock() {
        return finallyBlock;
    }

    public BlockStmt getTryBlock() {
        return tryBlock;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setCatchs(List<CatchClause> catchs) {
        this.catchs = catchs;
    }

    public void setFinallyBlock(BlockStmt finallyBlock) {
        this.finallyBlock = finallyBlock;
    }

    public void setTryBlock(BlockStmt tryBlock) {
        this.tryBlock = tryBlock;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
