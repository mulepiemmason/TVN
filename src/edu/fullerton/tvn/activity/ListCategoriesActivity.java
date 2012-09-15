/**
 * 
 */
package edu.fullerton.tvn.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.CategoryDbAdapter;
import edu.fullerton.tvn.db.datatypes.Category;
import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class ListCategoriesActivity extends ListActivity
{
	private CategoryDbAdapter dbAdapter;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		dbAdapter = new CategoryDbAdapter(this);
		dbAdapter.open();
		
		populateList();
		
		dbAdapter.close();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_category_menu, menu);
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.new_category:
				createCategory();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void createCategory()
	{
		Intent createCategoryIntent = new Intent(this, CreateUpdateCategoryActivity.class);
		this.startActivity(createCategoryIntent);
		finish();
	}
	
	private void populateList()
	{
		List<IDbObject> categories = dbAdapter.fetchAll();
		
		if (!categories.isEmpty())
		{
			setListAdapter(new ArrayAdapter<Object>(this,
					R.layout.list_categories,
					categories.toArray(new Object[categories.size()])));

			ListView listView = getListView();
			listView.setTextFilterEnabled(true);

			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int arg2, long id)
				{
					Category c = (Category) adapterView
							.getItemAtPosition((int) id);

					Intent displayCategoryIntent = new Intent(
							ListCategoriesActivity.this,
							DisplayCategoryActivity.class);

					displayCategoryIntent.putExtra(
							TVNProperties.SerializableKeys.CATEGORY, c);

					startActivity(displayCategoryIntent);
				}
			});
		} 
		else
		{
			String[] empty = { "No categories yet!" };
			
			setListAdapter(new ArrayAdapter<String>(this,
					R.layout.list_categories, empty));
		}
	}
}
