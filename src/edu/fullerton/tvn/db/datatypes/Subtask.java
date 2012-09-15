package edu.fullerton.tvn.db.datatypes;

import java.io.Serializable;

/**
 * @author ryan
 *
 */
public class Subtask implements IDbObject, Serializable
{
	private static final long serialVersionUID = -749651329263600973L;
	
	private String name = null;
	private Integer estimate = null;
	private Integer todo = null;
	
	public Subtask()
	{
		
	}
	
	public Subtask(String name, Integer estimate, Integer todo)
	{
		this.name = name;
		this.estimate = estimate;
		this.todo = todo;
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
	 * @return the estimate
	 */
	public Integer getEstimate()
	{
		return estimate;
	}

	/**
	 * @param estimate the estimate to set
	 */
	public void setEstimate(Integer estimate)
	{
		this.estimate = estimate;
	}

	/**
	 * @return the todo
	 */
	public Integer getTodo()
	{
		return todo;
	}

	/**
	 * @param todo the todo to set
	 */
	public void setTodo(Integer todo)
	{
		this.todo = todo;
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
