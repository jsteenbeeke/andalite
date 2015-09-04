/*
 * Copyright (C) 2015 Julio Vilmar Gesser and Mike DeHaan
 *
 * This file is part of antlr-java-parser.
 *
 * antlr-java-parser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * antlr-java-parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with antlr-java-parser.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.github.antlrjavaparser.adapter;

import com.github.antlrjavaparser.Java8Parser;
import com.github.antlrjavaparser.api.expr.AnnotationExpr;

import java.util.LinkedList;
import java.util.List;

public class AnnotationsContextAdapter implements Adapter<List<AnnotationExpr>, Java8Parser.AnnotationsContext> {
    public List<AnnotationExpr> adapt(Java8Parser.AnnotationsContext context, AdapterParameters adapterParameters) {
        if (context != null) {
            List<AnnotationExpr> annotationExprList = new LinkedList<AnnotationExpr>();
            for (Java8Parser.AnnotationContext annotationContext : context.annotation()) {
                annotationExprList.add(Adapters.getAnnotationContextAdapter().adapt(annotationContext, adapterParameters));
            }
            return annotationExprList;
        }

        throw new RuntimeException("Null annotations context");
    }
}
