package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.CategoryDbAdapter;
import edu.fullerton.tvn.db.datatypes.Category;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class DisplayCategoryActivity extends Activity
{
	private TextView name;
	private TextView description;
	
	private Category category;
	
	private CategoryDbAdapter dbAdapter;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_category);
		
		dbAdapter = new CategoryDbAdapter(this);
		dbAdapter.open();
		
		initComponents();
		populateViews();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_category_menu, menu);
		
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
			case R.id.update_category:
				updateCategory();
				return true;
			case R.id.delete_category:
				deleteCategory();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void updateCategory()
	{
		Intent updateIntent = new Intent(this, CreateUpdateCategoryActivity.class);
		updateIntent.putExtra(TVNProperties.SerializableKeys.CATEGORY, category);
		startActivity(updateIntent);
	}
	
	private void deleteCategory()
	{
		dbAdapter.deleteRecord(category);
		
		Intent listCategoriesIntent = new Intent(this,
				ListCategoriesActivity.class);
		
		startActivity(listCategoriesIntent);
		finish();
	}
	
	private void initComponents()
	{
		name = (TextView) this.findViewById(R.id.displayCategoryNameValue);
		description = (TextView) this.findViewById(R.id.displayCategoryDescriptionValue);
	}
	
	private void populateViews()
	{
		Bundle extras = this.getIntent().getExtras();
		
		this.category = (Category) extras
				.getSerializable(TVNProperties.SerializableKeys.CATEGORY);
		
		name.setText(category.getName());
		description.setText(category.getDescription());
	}
}
