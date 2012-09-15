package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.NotebookDbAdapter;
import edu.fullerton.tvn.db.datatypes.Note;
import edu.fullerton.tvn.db.datatypes.Notebook;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class DisplayNoteActivity extends Activity
{
	private TextView name;
	private TextView text;
	
	private Note note;
	private Notebook notebook;
	
	private NotebookDbAdapter dbAdapter;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_note);
		
		dbAdapter = new NotebookDbAdapter(this);
		dbAdapter.open();
		
		initComponents();
		populateFields();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_note_menu, menu);
		
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
			case R.id.update_note:
				updateNote();
				return true;
			case R.id.delete_note:
				deleteNote();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void deleteNote()
	{
		for (Note n : notebook.getNotes())
		{
			if (n.getName().equals(note.getName()))
			{
				notebook.getNotes().remove(n);
				break;
			}
		}

		dbAdapter.updateRecord(notebook);

		Intent displayNotebookIntent = new Intent(this,
				DisplayNotebookActivity.class);
		displayNotebookIntent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK,
				notebook);

		startActivity(displayNotebookIntent);
		finish();
	}

	private void updateNote()
	{
		Intent updateIntent = new Intent(this, CreateUpdateNoteActivity.class);
		updateIntent.putExtra(TVNProperties.SerializableKeys.NOTE, note);
		updateIntent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK, notebook);
		startActivity(updateIntent);
		finish();
	}

	private void populateFields()
	{
		Bundle extras = this.getIntent().getExtras();

		this.note = (Note) extras
				.getSerializable(TVNProperties.SerializableKeys.NOTE);

		name.setText(note.getName());
		text.setText(note.getText());

		this.notebook = (Notebook) extras
				.getSerializable(TVNProperties.SerializableKeys.NOTEBOOK);
	}

	private void initComponents()
	{
		name = (TextView) this.findViewById(R.id.displayNoteName);
		text = (TextView) this.findViewById(R.id.displayNoteText);
	}
}
