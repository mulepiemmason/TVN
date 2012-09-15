package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.NotebookDbAdapter;
import edu.fullerton.tvn.db.datatypes.Note;
import edu.fullerton.tvn.db.datatypes.Notebook;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class CreateUpdateNoteActivity extends Activity
{
	private NotebookDbAdapter dbAdapter;
	
	private EditText nameTextField;
	private EditText noteTextField;
	private Button saveButton;
	
	private Notebook savedNotebook;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		dbAdapter = new NotebookDbAdapter(this);
		dbAdapter.open();
		
		setContentView(R.layout.create_update_note);
		initComponents();
		
		Bundle extras = this.getIntent().getExtras();
		
		if(extras != null)
		{
			Note savedNote = (Note) extras
					.getSerializable(TVNProperties.SerializableKeys.NOTE);

			savedNotebook = (Notebook) extras
					.getSerializable(TVNProperties.SerializableKeys.NOTEBOOK);
			
			if (savedNote != null)
			{
				populateFields(savedNote);

				for (Note n : savedNotebook.getNotes())
				{
					if (n.getName().equals(savedNote.getName()))
					{
						savedNotebook.getNotes().remove(n);
						break;
					}
				}
			}
		}
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
	
	private void initComponents()
	{
		nameTextField = (EditText) this.findViewById(R.id.createNoteNameTextField);
		noteTextField = (EditText) this.findViewById(R.id.createNoteText);
		saveButton = (Button) this.findViewById(R.id.createNoteSaveButton);
		saveButton.setOnClickListener(new SaveButtonListener());
	}
	
	private void populateFields(Note note)
	{
		nameTextField.setText(note.getName());
		noteTextField.setText(note.getText());
	}
	
	private class SaveButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Note note = new Note();
			note.setName(nameTextField.getText().toString());
			note.setText(noteTextField.getText().toString());
			
			boolean duplicate = false;
			for(Note n : savedNotebook.getNotes())
			{
				if(n.getName().equals(note.getName()))
				{
					duplicate = true;
					break;
				}
			}
			
			if(duplicate)
			{
				// TODO toast warn
			}
			else
			{
				savedNotebook.getNotes().add(note);
				dbAdapter.updateRecord(savedNotebook);
				
				Intent displayNotebookIntent = new Intent(
						CreateUpdateNoteActivity.this,
						DisplayNotebookActivity.class);
				
				displayNotebookIntent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK, savedNotebook);
				
				startActivity(displayNotebookIntent);
				finish();
			}
		}
	}
}
