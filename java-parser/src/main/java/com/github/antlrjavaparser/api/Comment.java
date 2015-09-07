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
package com.github.antlrjavaparser.api;

import com.github.antlrjavaparser.api.body.JavadocComment;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

/**
 * Abstract class for all AST nodes that represent comments.
 *
 * @see BlockComment
 * @see LineComment
 * @see JavadocComment
 * @author Julio Vilmar Gesser
 */
public abstract class Comment {

    private String content;

    public Comment() {
    }

    public Comment(String content) {
        this.content = content;
    }

    /**
     * Return the text of the comment.
     *
     * @return text of the comment
     */
    public final String getContent() {
        return content;
    }

    /**
     * Sets the text of the comment.
     *
     * @param content
     *            the text of the comment to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }
}
