package edu.fullerton.tvn.activity;

import java.util.List;

import android.app.Activity;
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
import android.widget.TextView;
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
public class DisplayNotebookActivity extends Activity
{
	private TextView name;
	private TextView description;
	private ListView noteList;
	
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
		
		setContentView(R.layout.display_notebook);
		
		dbAdapter = new NotebookDbAdapter(this);
		dbAdapter.open();
		
		initComponents();
		populateComponents();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		Intent tvnActivityIntent = new Intent(this, TVNActivity.class);
		tvnActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(tvnActivityIntent);
		finish();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_notebook_menu, menu);
		
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
			case R.id.update_notebook:
				updateNotebook();
				return true;
			case R.id.delete_notebook:
				deleteNotebook();
				return true;
			case R.id.new_note:
				createNote();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void initComponents()
	{
		name = (TextView) this.findViewById(R.id.displayNotebookName);
		description = (TextView) this.findViewById(R.id.displayNotebookDescription);
		noteList = (ListView) this.findViewById(R.id.displayNotebookNoteList);
	}
	
	private void populateComponents()
	{
		Bundle extras = this.getIntent().getExtras();
		
		this.notebook = (Notebook) extras
				.getSerializable(TVNProperties.SerializableKeys.NOTEBOOK);
		
		name.setText(notebook.getName());
		description.setText(notebook.getDescription());
		
		List<Note> notes = notebook.getNotes();
		
		if(!notes.isEmpty())
		{
			noteList.setAdapter(new ArrayAdapter<Object>(this,
					R.layout.list_notes,
					notes.toArray(new Object[notes.size()])));
			noteList.setTextFilterEnabled(true);
			noteList.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1,
						int arg2, long id)
				{
					Note n = (Note) adapterView.getItemAtPosition((int) id);
					
					Intent displayNoteIntent = new Intent(
							DisplayNotebookActivity.this,
							DisplayNoteActivity.class);
					
					displayNoteIntent.putExtra(
							TVNProperties.SerializableKeys.NOTEBOOK, notebook);
					displayNoteIntent.putExtra(
							TVNProperties.SerializableKeys.NOTE, n);
					
					startActivity(displayNoteIntent);
					finish();
				}
			});
		}
		else
		{
			String[] empty = { "No notes yet!" };
			
			noteList.setAdapter(new ArrayAdapter<Object>(this,
					R.layout.list_notes, empty));
		}
	}
	
	private void updateNotebook()
	{
		Intent updateIntent = new Intent(this, CreateUpdateNotebookActivity.class);
		updateIntent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK, notebook);
		startActivity(updateIntent);
	}
	
	private void deleteNotebook()
	{
		dbAdapter.deleteRecord(notebook);
		
		Intent listNotebooksIntent = new Intent(this, TVNActivity.class);
		listNotebooksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(listNotebooksIntent);
		finish();
	}
	
	private void createNote()
	{
		Intent createNoteIntent = new Intent(this, CreateUpdateNoteActivity.class);
		createNoteIntent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK, notebook);
		this.startActivity(createNoteIntent);
		finish();
	}
}
