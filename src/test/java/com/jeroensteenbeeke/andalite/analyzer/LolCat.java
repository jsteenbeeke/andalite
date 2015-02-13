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

package com.jeroensteenbeeke.andalite.analyzer;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "IDX_LOLCAT_CANHAZ", columnList = "canHaz"), uniqueConstraints = {
		@UniqueConstraint(name = "U_LOLCAT_HANDLE", columnNames = "handle"),
		@UniqueConstraint(name = "U_LOLCAT_CODE", columnNames = "code") })
public class LolCat {
	@Id
	private Long id;

	@Column(nullable = false)
	private String handle;

	@Column(nullable = false)
	private String code;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private LolCat canHaz;

	public LolCat() {
	}

	public Long getId() {
		return id;
	}

	public LolCat getCanHaz() {
		return canHaz;
	}

	public void setCanHaz(LolCat canHaz) {
		this.canHaz = canHaz;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
