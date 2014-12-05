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

	@ManyToOne(optional = true)
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
