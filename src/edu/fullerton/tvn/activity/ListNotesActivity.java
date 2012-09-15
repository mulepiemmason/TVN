package edu.fullerton.tvn.activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.TVNActivity;
import edu.fullerton.tvn.db.adapter.NotebookDbAdapter;
import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.db.datatypes.Note;
import edu.fullerton.tvn.db.datatypes.Notebook;
import edu.fullerton.tvn.impexp.ImpExpUtil;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class ListNotesActivity extends ListActivity
{
	private NotebookDbAdapter dbAdapter;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		dbAdapter = new NotebookDbAdapter(this);
		dbAdapter.open();
		
		populateList();
		
		dbAdapter.close();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_notebook_menu, menu);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.new_notebook:
				createNotebook();
				return true;
			case R.id.search_notebooks:
				initiateSearchDialog();
				return true;
			case R.id.import_string:
				importNotebooks();
				return true;
			case R.id.export_string:
				exportNotebooks();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void populateList()
	{
		final List<IDbObject> notebooks = dbAdapter.fetchAll();
		
		if(!notebooks.isEmpty())
		{
			setListAdapter(new ArrayAdapter<Object>(this,
					R.layout.list_notebooks,
					notebooks.toArray(new Object[notebooks.size()])));
			
			final ListView listView = getListView();
			listView.setTextFilterEnabled(true);
			
			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1,
						int arg2, long id)
				{
					final Notebook n = (Notebook) adapterView.getItemAtPosition((int) id);
					
					final Intent displayNotebookIntent = new Intent(
							ListNotesActivity.this,
							DisplayNotebookActivity.class);
					
					displayNotebookIntent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK, n);
					
					startActivity(displayNotebookIntent);
				}
			});
		}
		else
		{
			final String[] empty = { "No notebooks yet!" };
			
			setListAdapter(new ArrayAdapter<String>(this,
					R.layout.list_notebooks, empty));
		}
	}
	
	private void createNotebook()
	{
		final Intent createNotebookIntent = new Intent(this, CreateUpdateNotebookActivity.class);
		
		this.startActivity(createNotebookIntent);
	}
	
	private void initiateSearchDialog()
	{
		final EditText input = new EditText(this);
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Enter the phrase you wish to search for.").setTitle("Search Notebooks")
			.setCancelable(true)
			.setPositiveButton("Search", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					searchNotebooks(input.getText().toString());
				}
			}).setView(input);
		
		final AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void searchNotebooks(String text)
	{
		List<IDbObject> notebooks = dbAdapter.fetchAll();
		HashMap<Notebook, Note> matchingList = new HashMap<Notebook, Note>();
		
		for(IDbObject obj : notebooks)
		{
			for(Note n : ((Notebook) obj).getNotes())
			{
				if(n != null)
				{
					if(n.getText() != null && n.getText().contains(text))
					{
						matchingList.put((Notebook) obj, n);
					}
				}
			}
		}
		
		final Intent intent = new Intent(this, DisplaySearchResultsActivity.class);
		
		intent.putExtra(TVNProperties.SerializableKeys.SEARCH_PARAM, text);
		intent.putExtra(TVNProperties.SerializableKeys.RESULT_MAP, matchingList);
		
		startActivity(intent);
		finish();
	}
	
	private void importNotebooks()
	{
		final String[] files = ImpExpUtil
				.constructFileList(TVNProperties.ImpExp.NOTEBOOKS_SUBDIR);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select notebooks you wish to import:");
		builder.setItems(files, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				final List<IDbObject> notebooks = ImpExpUtil.importFile(files[which],
						TVNProperties.ImpExp.NOTEBOOKS_SUBDIR);
				
				for(IDbObject obj : notebooks)
				{
					final Notebook notebook = (Notebook) obj;
					dbAdapter.createRecord(notebook);
				}
				
				Intent intent = new Intent(ListNotesActivity.this,
						TVNActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(intent);
				finish();
			}
		});
		
		builder.create().show();
	}
	
	private void exportNotebooks()
	{
		final EditText input = new EditText(this);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Enter a name for the file the notebooks will be stored in:")
				.setTitle("Enter file name for notebooks").setCancelable(true)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						ImpExpUtil.exportObjects(dbAdapter.fetchAll(), input
								.getText().toString(),
								TVNProperties.ImpExp.NOTEBOOKS_SUBDIR);
					}
				}).setView(input);
		
		builder.create().show();
		
		Toast.makeText(this, "Notebooks, " + input.getText().toString()
				+ ", exported!", Toast.LENGTH_LONG).show();
	}
}
