package edu.fullerton.tvn.db.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.fullerton.tvn.db.datatypes.Category;
import edu.fullerton.tvn.db.datatypes.IDbObject;

/**
 * @author ryan
 *
 */
public class CategoryDbAdapter extends AbstractDbAdapter
{
	public final static String KEY_ID = "_id";
	public final static String NAME = "name";
	public final static String DESCRIPTION = "description";
	
	public CategoryDbAdapter(Context context)
	{
		super(context);
	}

	/* (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#createRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public int createRecord(IDbObject dbObject)
	{
		Category categoryObj = (Category) dbObject;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME,  categoryObj.getName());
		values.put(DESCRIPTION, categoryObj.getDescription());
		
		int id = (int) db.insert(CATEGORY_TBL_NAME, null, values);
		db.close();
		
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#deleteRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public void deleteRecord(IDbObject dbObject)
	{
		Category category = (Category) dbObject;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(CATEGORY_TBL_NAME, KEY_ID + " = ?", new String[] { String.valueOf(category.getKey()) });
		db.close();
	}

	/* (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#fetchAll()
	 */
	@Override
	public List<IDbObject> fetchAll()
	{
		List<IDbObject> catList = new ArrayList<IDbObject>();
		String query = "SELECT * FROM " + CATEGORY_TBL_NAME;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				Category category = new Category();
				category.setKey(Integer.valueOf(cursor.getString(0)));
				category.setName(cursor.getString(1));
				category.setDescription(cursor.getString(2));
				
				catList.add(category);
			}
			while(cursor.moveToNext());
		}
		
		return catList;
	}

	/* (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#fetchRecord(int)
	 */
	@Override
	public IDbObject fetchRecord(int id) throws SQLException
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(CATEGORY_TBL_NAME, new String[]
		{ KEY_ID, NAME, DESCRIPTION }, KEY_ID + "=?", new String[]
		{ String.valueOf(id) }, null, null, null, null);
		
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		
		Category category = new Category(Integer.valueOf(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		
		return category;
	}

	/* (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#updateRecord(edu.fullerton.tvn.db.datatypes.IDbObject)
	 */
	@Override
	public int updateRecord(IDbObject dbObject)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Category category = (Category) dbObject;
		
		ContentValues values = new ContentValues();
		values.put(KEY_ID, category.getKey());
		values.put(NAME, category.getName());
		values.put(DESCRIPTION, category.getDescription());
		
		return db.update(CATEGORY_TBL_NAME, values, KEY_ID + "=?", new String[]
		{ String.valueOf(category.getKey()) });
	}

	/* (non-Javadoc)
	 * @see edu.fullerton.tvn.db.adapter.IDbAdapter#getObjectCount()
	 */
	@Override
	public int getObjectCount()
	{
		String query = "SELECT * FROM " + CATEGORY_TBL_NAME;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.close();
		
		return cursor.getCount();
	}
}
