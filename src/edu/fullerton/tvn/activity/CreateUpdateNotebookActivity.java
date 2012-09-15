package edu.fullerton.tvn.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.TVNActivity;
import edu.fullerton.tvn.db.adapter.NotebookDbAdapter;
import edu.fullerton.tvn.db.datatypes.Note;
import edu.fullerton.tvn.db.datatypes.Notebook;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class CreateUpdateNotebookActivity extends Activity
{
	private EditText nameTextField;
	private EditText descriptionTextField;
	private Button saveButton;
	
	private List<Note> notes;
	
	private NotebookDbAdapter dbAdapter;
	
	private boolean update = false;
	
	private int savedKey;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		dbAdapter = new NotebookDbAdapter(this);
		dbAdapter.open();
		
		setContentView(R.layout.create_update_notebook);
		initComponents();
		
		Bundle extras = this.getIntent().getExtras();
		
		if(extras != null)
		{
			Notebook savedNotebook = (Notebook) extras
					.getSerializable(TVNProperties.SerializableKeys.NOTEBOOK);
			
			if(savedNotebook != null)
			{
				populateFields(savedNotebook);
				this.update = true;
				this.savedKey = savedNotebook.getKey();
			}
		}
	}
	
	private void initComponents()
	{
		nameTextField = (EditText) this.findViewById(R.id.createUpdateNotebookNameText);
		descriptionTextField = (EditText) this
				.findViewById(R.id.createUpdateNotebookDescriptionText);
		saveButton = (Button) this.findViewById(R.id.createUpdateNotebookSaveButton);
		saveButton.setOnClickListener(new SaveButtonListener());
	}
	
	private void populateFields(Notebook savedNotebook)
	{
		nameTextField.setText(savedNotebook.getName());
		descriptionTextField.setText(savedNotebook.getDescription());
		notes = savedNotebook.getNotes();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
	
	private class SaveButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Notebook notebook = new Notebook();
			notebook.setName(nameTextField.getText().toString());
			notebook.setDescription(descriptionTextField.getText().toString());
			
			if(update)
			{
				notebook.setKey(savedKey);
				notebook.setNotes(notes);
				dbAdapter.updateRecord(notebook);
			}
			else
			{
				dbAdapter.createRecord(notebook);
			}
			
			Intent listNotebooksIntent = new Intent(
					CreateUpdateNotebookActivity.this, TVNActivity.class);
			listNotebooksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(listNotebooksIntent);
			finish();
		}
	}
}
