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
import com.github.antlrjavaparser.api.PackageDeclaration;

public class PackageDeclarationContextAdapter implements Adapter<PackageDeclaration, Java8Parser.PackageDeclarationContext> {
    public PackageDeclaration adapt(Java8Parser.PackageDeclarationContext context, AdapterParameters adapterParameters) {
        if (context == null) {
            return null;
        }

        PackageDeclaration packageDeclaration = new PackageDeclaration();
        AdapterUtil.setComments(packageDeclaration, context, adapterParameters);
        AdapterUtil.setPosition(packageDeclaration, context);
        packageDeclaration.setName(Adapters.getQualifiedNameContextAdapter().adapt(context.qualifiedName(), adapterParameters));

        if (context.annotations() != null) {
            packageDeclaration.setAnnotations(Adapters.getAnnotationsContextAdapter().adapt(context.annotations(), adapterParameters));
        }

        return packageDeclaration;
    }
}