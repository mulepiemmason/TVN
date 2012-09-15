/**
 * 
 */
package edu.fullerton.tvn.db.datatypes;

import java.io.Serializable;

/**
 * @author ryan
 *
 */
public class Note implements IDbObject, Serializable
{
	private static final long serialVersionUID = -6250532961960307997L;

	private String name = null;
	private String text = null;
	
	/**
	 * Default constructor
	 */
	public Note()
	{
		super();
	}
	
	/**
	 * 
	 * @param name
	 * @param text
	 */
	public Note(String name, String text)
	{
		
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
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}
