package edu.fullerton.tvn.db.adapter;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * 
 * @author ryan
 *
 */
public abstract class AbstractDbAdapter
{
	private final static String TAG = AbstractDbAdapter.class.getSimpleName();
	
	private static String CATEGORY_CREATE_STATEMENT;
	private static String TASKS_CREATE_STATEMENT;
	private static String NOTEBOOKS_CREATE_STATEMENT;

	private static String DB_NAME = TVNProperties.Database.DB_NAME;
	private static int DB_VERSION = TVNProperties.Database.DB_VERSION;
	
	protected final static String CATEGORY_TBL_NAME = "categories";
	protected final static String TASKS_TBL_NAME = "tasks";
	protected final static String NOTEBOOKS_TBL_NAME = "notebooks";
	
	protected static DatabaseHelper dbHelper;
	
	private Context context;
	
	public AbstractDbAdapter(Context context)
	{
		this.context = context;
		
		constructCreateStatements();
	}
	
	public abstract int createRecord(IDbObject dbObject);
	public abstract void deleteRecord(IDbObject dbObject);
	public abstract List<IDbObject> fetchAll();
	public abstract IDbObject fetchRecord(int id);
	public abstract int updateRecord(IDbObject dbObject);
	public abstract int getObjectCount();
	
	private void constructCreateStatements()
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer = new StringBuffer();
		buffer.append("create table " + CATEGORY_TBL_NAME
				+ " (_id integer primary key autoincrement, ");
		buffer.append("name text not null, ");
		buffer.append("description text);");
		
		CATEGORY_CREATE_STATEMENT = buffer.toString();
		
		buffer = new StringBuffer();
		buffer.append("create table " + TASKS_TBL_NAME
				+ " (_id integer primary key autoincrement, ");
		buffer.append("name text not null, ");

		// Due date and time stored as ISO8601 strings
		buffer.append("due_date text, "); // YYYY-MM-DD
		buffer.append("due_time text, "); // HH:MM:SS.SSS

		buffer.append("priority integer, ");
		buffer.append("alarm integer, ");
		buffer.append("category text, ");
		buffer.append("subtasks text);");

		TASKS_CREATE_STATEMENT = buffer.toString();
		
		buffer = new StringBuffer();
		buffer.append("create table " + NOTEBOOKS_TBL_NAME + " (_id integer primary key autoincrement, ");
		buffer.append("name text not null, ");
		buffer.append("description text, ");
		buffer.append("notes text);");
		
		NOTEBOOKS_CREATE_STATEMENT = buffer.toString();
	}
	
	public AbstractDbAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(this.context);
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	protected static class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			Log.d(TAG, "Creating DB tables...");
			db.execSQL(CATEGORY_CREATE_STATEMENT);
			db.execSQL(TASKS_CREATE_STATEMENT);
			db.execSQL(NOTEBOOKS_CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO save data (ALTER TABLE?)
			db.execSQL("DROP TABLE IF EXISTS " + TASKS_TBL_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TBL_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + NOTEBOOKS_TBL_NAME);
			onCreate(db);
		}
	}
}
