package com.jeroensteenbeeke.andalite.dummy;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.jeroensteenbeeke.hyperion.meld.filter.RequiredFilterField;
import com.jeroensteenbeeke.hyperion.webcomponents.entitypage.DefaultFieldType;
import com.jeroensteenbeeke.hyperion.webcomponents.entitypage.annotation.EntityFormField;
import com.jeroensteenbeeke.hyperion.webcomponents.entitypage.annotation.Minimum;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class with unicode characters
 *
 * éééé (2-byte)
 * ᚦᚦᚦᚦ (3-byte)
 * 💩💩💩💩 (4-byte)
 */
@Entity
public class FooBar extends BaseDomainObject implements Serializable
{

	@SequenceGenerator(allocationSize = 1, initialValue = 1, name = "FooBar",
			sequenceName = "SEQ_ID_FooBar")
	@Access(value = AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FooBar")
	@Id
	private Long id;

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "foobar")
	private List<FooBarRule> rules = new ArrayList<>();

	@Column(nullable = false)
	@EntityFormField(label = "Name")
	private String name;

	@JoinColumn(name = "owner")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@RequiredFilterField
	private User owner;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "foobar")
	private List<WeekTemplate> templates = new ArrayList<>();

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Override
	public final Serializable getDomainObjectId()
	{
		return getId();
	}

	public List<FooBarRule> getRules()
	{
		return rules;
	}

	public void setRules(List<FooBarRule> rules)
	{
		this.rules = rules;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public List<FooBarTemplate> getTemplates()
	{
		return templates;
	}

	public void setTemplates(List<FooBarTemplate> templates)
	{
		this.templates = templates;
	}

}
