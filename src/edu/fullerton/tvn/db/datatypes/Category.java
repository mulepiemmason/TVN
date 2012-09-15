package edu.fullerton.tvn.db.datatypes;

import java.io.Serializable;

/**
 * @author ryan
 *
 */
public class Category implements IDbObject, Serializable
{
	private static final long serialVersionUID = -3020769949943061916L;
	
	private Integer key = null;
	private String name = null;
	private String description = null;
	
	/**
	 * Default constructor
	 */
	public Category()
	{
		
	}
	
	/**
	 * 
	 * @param name
	 * @param description
	 */
	public Category(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	/**
	 * 
	 * @param key
	 * @param name
	 * @param description
	 */
	public Category(Integer key, String name, String description)
	{
		this.key = key;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the key
	 */
	public Integer getKey()
	{
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Integer key)
	{
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}
