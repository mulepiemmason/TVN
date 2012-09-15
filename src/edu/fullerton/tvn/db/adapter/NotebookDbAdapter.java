/**
 * 
 */
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
import edu.fullerton.tvn.db.datatypes.Note;
import edu.fullerton.tvn.db.datatypes.Notebook;

/**
 * @author ryan
 *
 */
public class NotebookDbAdapter extends AbstractDbAdapter
{
	public final static String KEY_ID = "_id";
	public final static String NAME = "name";
	public final static String DESCRIPTION = "description";
	public final static String NOTES = "notes";
	
	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public NotebookDbAdapter(Context context)
	{
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#createRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public int createRecord(IDbObject dbObject)
	{
		Notebook notebook = (Notebook) dbObject;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, notebook.getName());
		values.put(DESCRIPTION, notebook.getDescription());
		
		if (!notebook.getNotes().isEmpty())
		{
			values.put(NOTES, serializeNotes(notebook.getNotes()));
		}
		
		int id = (int) db.insert(NOTEBOOKS_TBL_NAME, null, values);
		db.close();
		
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#deleteRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public void deleteRecord(IDbObject dbObject)
	{
		Notebook notebook = (Notebook) dbObject;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(NOTEBOOKS_TBL_NAME, KEY_ID + " = ?", new String[]
		{ String.valueOf(notebook.getKey()) });
		
		db.close();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#fetchAll()
	 */
	@Override
	public List<IDbObject> fetchAll()
	{
		List<IDbObject> notebookList = new ArrayList<IDbObject>();
		String query = "SELECT * FROM " + NOTEBOOKS_TBL_NAME;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				Notebook notebook = new Notebook();
				notebook.setKey(Integer.valueOf(cursor.getString(0)));
				notebook.setName(cursor.getString(1));
				notebook.setDescription(cursor.getString(2));
				notebook.setNotes(deserializeNotes(cursor.getString(3)));
				
				notebookList.add(notebook);
			}
			while (cursor.moveToNext());
		}
		
		return notebookList;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#fetchRecord(int)
	 */
	@Override
	public IDbObject fetchRecord(int id) throws SQLException
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(NOTEBOOKS_TBL_NAME, new String[]
		{ KEY_ID, NAME, DESCRIPTION, NOTES }, KEY_ID + "=?", new String[]
		{ String.valueOf(id) }, null, null, null, null);
		
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		
		Notebook notebook = new Notebook(Integer.valueOf(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2),
				deserializeNotes(cursor.getString(3)));
		
		return notebook;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#updateRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public int updateRecord(IDbObject dbObject)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Notebook notebook = (Notebook) dbObject;
		
		ContentValues values = new ContentValues();
		values.put(KEY_ID, notebook.getKey());
		values.put(NAME, notebook.getName());
		values.put(DESCRIPTION, notebook.getDescription());
		values.put(NOTES, serializeNotes(notebook.getNotes()));
		
		return db.update(NOTEBOOKS_TBL_NAME, values, KEY_ID + "=?",
				new String[]
				{ String.valueOf(notebook.getKey()) });
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.AbstractDbAdapter#getObjectCount()
	 */
	@Override
	public int getObjectCount()
	{
		String query = "SELECT * FROM " + NOTEBOOKS_TBL_NAME;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.close();
		
		return cursor.getCount();
	}
	
	private String serializeNotes(List<Note> notes)
	{
		return new Gson().toJson(notes);
	}
	
	private List<Note> deserializeNotes(String json)
	{
		return new Gson().fromJson(json, new TypeToken<Collection<Note>>(){}.getType());
	}

}
