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

package com.jeroensteenbeeke.andalite.transformation.operations;

import java.util.List;

import com.jeroensteenbeeke.andalite.analyzer.IOutputable;
import com.jeroensteenbeeke.andalite.transformation.Transformation;

public interface IOperation<T extends IOutputable> {
	List<Transformation> perform(T input) throws OperationException;

	String getDescription();
}
