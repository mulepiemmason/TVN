package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.CategoryDbAdapter;
import edu.fullerton.tvn.db.datatypes.Category;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * Activity for creating and udpating categories.
 * 
 * @author ryan
 *
 */
public class CreateUpdateCategoryActivity extends Activity
{
	private CategoryDbAdapter categoryDbAdapter;
	
	private EditText nameTextField;
	private EditText descriptionTextField;
	private Button saveButton;
	
	private int savedKey;
	
	private boolean update = false;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		categoryDbAdapter = new CategoryDbAdapter(this);
		categoryDbAdapter.open();
		
		setContentView(R.layout.create_update_category);
		initComponents();
		
		Bundle extras = this.getIntent().getExtras();
		
		if (extras != null)
		{
			Category savedCategory = (Category) extras
					.getSerializable(TVNProperties.SerializableKeys.CATEGORY);

			if (savedCategory != null)
			{
				populateFields(savedCategory);
				this.update = true;
				this.savedKey = savedCategory.getKey();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
	
	private void initComponents()
	{
		nameTextField = (EditText) this.findViewById(R.id.categoryNameTextField);
		descriptionTextField = (EditText) this.findViewById(R.id.categoryDescriptionTextField);
		
		saveButton = (Button) this.findViewById(R.id.saveCategoryButton);
		saveButton.setOnClickListener(new SaveButtonListener());
	}
	
	private void populateFields(Category savedCategory)
	{
		nameTextField.setText(savedCategory.getName());
		descriptionTextField.setText(savedCategory.getDescription());
	}
	
	private class SaveButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Category cat = new Category();
			cat.setName(nameTextField.getText().toString());
			cat.setDescription(descriptionTextField.getText().toString());

			if(update)
			{
				cat.setKey(savedKey);
				categoryDbAdapter.updateRecord(cat);
			}
			else
			{
				categoryDbAdapter.createRecord(cat);
			}
			
			Intent listCategoriesIntent = new Intent(
					CreateUpdateCategoryActivity.this,
					ListCategoriesActivity.class);
			
			startActivity(listCategoriesIntent);
			finish();
		}
	}
}
