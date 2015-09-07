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
package com.jeroensteenbeeke.andalite.java.transformation;

import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.ReturnStatementNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;

public class BodyContainerOperationBuilder extends
		AbstractOperationBuilder<IBodyContainer, IBodyContainerOperation> {
	BodyContainerOperationBuilder(IStepCollector collector,
			IJavaNavigation<IBodyContainer> navigation) {
		super(collector, navigation);
	}

	public IfStatementLocator inIfExpression() {
		return new IfStatementLocator(this);
	}

	public StatementOperationBuilder<ReturnStatement> forReturnStatement() {
		return new StatementOperationBuilder<ReturnStatement>(getCollector(),
				new ReturnStatementNavigation(getNavigation()));
	}
}
