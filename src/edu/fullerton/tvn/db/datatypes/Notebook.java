package edu.fullerton.tvn.db.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ryan
 *
 */
public class Notebook implements IDbObject, Serializable
{
	private static final long serialVersionUID = -854241121691129041L;
	
	private Integer key = null;
	private String name = null;
	private String description = null;
	
	private List<Note> notes = null;
	
	/**
	 * Default constructor
	 */
	public Notebook()
	{
		super();
	}
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param notes
	 */
	public Notebook(String name, String description, List<Note> notes)
	{
		this.name = name;
		this.description = description;
		this.notes = notes;
	}
	
	/**
	 * 
	 * @param key
	 * @param name
	 * @param description
	 * @param notes
	 */
	public Notebook(Integer key, String name, String description,
			List<Note> notes)
	{
		this.key = key;
		this.name = name;
		this.description = description;
		this.notes = notes;
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

	/**
	 * @return the notes
	 */
	public List<Note> getNotes()
	{
		if (notes == null)
		{
			notes = new ArrayList<Note>();
		}

		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(List<Note> notes)
	{
		this.notes = notes;
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
