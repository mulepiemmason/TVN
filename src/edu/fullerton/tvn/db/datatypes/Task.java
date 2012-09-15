package edu.fullerton.tvn.db.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements IDbObject, Serializable
{
	private static final long serialVersionUID = 2673376184635278608L;
	
	private Integer key = null;
	private String name = null;
	private String dueDate = null;
	private String dueTime = null;
	private Integer priority = null;
	private boolean alarm = false;
	private String category = null;
	
	private List<Subtask> subtasks = null;
	
	/**
	 * Default Constructor
	 */
	public Task()
	{
		super();
	}
	
	/**
	 * @param name
	 * @param dueDate
	 * @param dueTime
	 * @param priority
	 * @param alertLevel
	 * @param category
	 * @param progress
	 */
	public Task(String name, String dueDate, String dueTime,
			Integer priority, boolean alarm, String category,
			List<Subtask> subtasks)
	{
		this.name = name;
		this.dueDate = dueDate;
		this.dueTime = dueTime;
		this.priority = priority;
		this.alarm = alarm;
		this.category = category;
		this.subtasks = subtasks;
	}
	
	/**
	 * 
	 * @param key
	 * @param name
	 * @param dueDate
	 * @param dueTime
	 * @param priority
	 * @param alertLevel
	 * @param category
	 * @param progress
	 */
	public Task(Integer key, String name, String dueDate, String dueTime,
			Integer priority, boolean alarm, String category,
			List<Subtask> subtasks)
	{
		this.key = key;
		this.name = name;
		this.dueDate = dueDate;
		this.dueTime = dueTime;
		this.priority = priority;
		this.alarm = alarm;
		this.category = category;
		this.subtasks = subtasks;
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
	 * @return the dueDate
	 */
	public String getDueDate()
	{
		return dueDate;
	}
	
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate)
	{
		this.dueDate = dueDate;
	}
	
	/**
	 * @return the dueTime
	 */
	public String getDueTime()
	{
		return dueTime;
	}
	
	/**
	 * @param dueTime the dueTime to set
	 */
	public void setDueTime(String dueTime)
	{
		this.dueTime = dueTime;
	}
	
	/**
	 * @return the priority
	 */
	public Integer getPriority()
	{
		return priority;
	}
	
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}
	
	/**
	 * @return the alarm
	 */
	public boolean getAlarm()
	{
		return alarm;
	}
	
	/**
	 * @param alarm the alarm to set
	 */
	public void setAlarm(boolean alarm)
	{
		this.alarm = alarm;
	}
	
	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}
	
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * @return the subtasks
	 */
	public List<Subtask> getSubtasks()
	{
		if(subtasks == null)
		{
			subtasks = new ArrayList<Subtask>();
		}
		
		return subtasks;
	}

	/**
	 * @param subtasks the subtasks to set
	 */
	public void setSubtasks(List<Subtask> subtasks)
	{
		this.subtasks = subtasks;
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
