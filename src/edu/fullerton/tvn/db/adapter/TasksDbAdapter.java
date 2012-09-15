package edu.fullerton.tvn.db.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.db.datatypes.Subtask;
import edu.fullerton.tvn.db.datatypes.Task;

/**
 * Database adapter for the tasks table.
 * 
 * @author ryan
 * 
 */
public class TasksDbAdapter extends AbstractDbAdapter
{
	public final static String KEY_ID = "_id";
	public final static String NAME = "name";
	public final static String DUE_DATE = "due_date";
	public final static String DUE_TIME = "due_time";
	public final static String PRIORITY = "priority";
	public final static String ALARM = "alarm";
	public final static String CATEGORY = "category";
	public final static String SUBTASKS = "subtasks";

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public TasksDbAdapter(Context context)
	{
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#deleteRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public void deleteRecord(IDbObject dbObject)
	{
		Task task = (Task) dbObject;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(TASKS_TBL_NAME, KEY_ID + " = ?",
				new String[] { String.valueOf(task.getKey()) });
		db.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#fetchAll()
	 */
	@Override
	public List<IDbObject> fetchAll()
	{
		List<IDbObject> taskList = new ArrayList<IDbObject>();
		String query = "SELECT * FROM " + TASKS_TBL_NAME;

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst())
		{
			do
			{
				Task task = new Task();
				task.setKey(Integer.valueOf(cursor.getString(0)));
				task.setName(cursor.getString(1));
				task.setDueDate(cursor.getString(2));
				task.setDueTime(cursor.getString(3));
				task.setPriority(Integer.valueOf(cursor.getString(4)));
				task.setAlarm((Integer.valueOf(cursor.getString(5)) == 1) ? true
						: false);
				task.setCategory(cursor.getString(6));
				task.setSubtasks(deserializeSubtasks(cursor.getString(7)));

				taskList.add(task);
			}
			while (cursor.moveToNext());
		}

		return taskList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#fetchRecord(int)
	 */
	@Override
	public IDbObject fetchRecord(int id) throws SQLException
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = db.query(TASKS_TBL_NAME,
				new String[] { KEY_ID, NAME, DUE_DATE, DUE_TIME, PRIORITY,
						ALARM, CATEGORY, SUBTASKS }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
		{
			cursor.moveToFirst();
		}
		
		Task task = new Task(Integer.valueOf(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				Integer.valueOf(cursor.getString(4)), (Integer.valueOf(cursor
						.getString(5)) == 1) ? true : false,
				cursor.getString(6), deserializeSubtasks(cursor.getString(7)));

		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.fullerton.tvn.db.adapter.IDbAdapter#createRecord(edu.fullerton.tvn
	 * .db.datatypes.IDbObject)
	 */
	@Override
	public int createRecord(IDbObject dbObject)
	{
		Task taskObj = (Task) dbObject;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NAME, taskObj.getName());
		values.put(ALARM, taskObj.getAlarm());
		values.put(CATEGORY, taskObj.getCategory());
		values.put(DUE_DATE, taskObj.getDueDate());
		values.put(DUE_TIME, taskObj.getDueTime());
		values.put(PRIORITY, taskObj.getPriority());
		values.put(ALARM, taskObj.getAlarm() ? 1 : 0);
		
		if (!taskObj.getSubtasks().isEmpty())
		{
			values.put(SUBTASKS, serializeSubtasks(taskObj.getSubtasks()));
		}

		int id = (int) db.insert(TASKS_TBL_NAME, null, values);
		db.close();
		
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.fullerton.tvn.db.adapter.IDbAdapter#updateRecord(edu.fullerton.tvn
	 * .db.datatypes.IDbObject)
	 */
	@Override
	public int updateRecord(IDbObject dbObject)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Task task = (Task) dbObject;

		ContentValues values = new ContentValues();
		values.put(KEY_ID, task.getKey());
		values.put(NAME, task.getName());
		values.put(DUE_DATE, task.getDueDate());
		values.put(DUE_TIME, task.getDueTime());
		values.put(ALARM, task.getAlarm() ? 1 : 0);
		values.put(PRIORITY, task.getPriority());
		values.put(CATEGORY, task.getCategory());
		values.put(SUBTASKS, serializeSubtasks(task.getSubtasks()));

		return db.update(TASKS_TBL_NAME, values, KEY_ID + "=?",
				new String[] { String.valueOf(task.getKey()) });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#getObjectCount()
	 */
	@Override
	public int getObjectCount()
	{
		String query = "SELECT * FROM " + TASKS_TBL_NAME;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.close();

		return cursor.getCount();
	}
	
	private String serializeSubtasks(List<Subtask> subtasks)
	{
		return new Gson().toJson(subtasks);
	}
	
	private List<Subtask> deserializeSubtasks(String json)
	{
		return new Gson().fromJson(json, new TypeToken<Collection<Subtask>>(){}.getType());
	}
}
