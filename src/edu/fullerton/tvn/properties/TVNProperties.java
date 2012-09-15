package edu.fullerton.tvn.properties;

/**
 * Contains various TVN properties.
 * 
 * @author ryan
 *
 */
public class TVNProperties
{
	public class Database
	{
		public final static String DB_NAME = "tvn_db";
		public final static int DB_VERSION = 7;
	}
	
	public class SerializableKeys
	{
		public final static String CATEGORY = "category";
		public final static String TASK = "task";
		public final static String SUBTASK = "subtask";
		public final static String NOTEBOOK = "notebook";
		public final static String NOTE = "note";
		public final static String RESULT_MAP = "result_map";
		public final static String SEARCH_PARAM = "search_parameter";
		public final static String TASK_ID = "task_id";
	}
	
	public class ImpExp
	{
		public final static String TASKS_SUBDIR = "tasks";
		public final static String NOTEBOOKS_SUBDIR = "notebooks";
	}
}
