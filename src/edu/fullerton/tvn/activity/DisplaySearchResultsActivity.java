package edu.fullerton.tvn.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.TVNActivity;
import edu.fullerton.tvn.db.datatypes.Note;
import edu.fullerton.tvn.db.datatypes.Notebook;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class DisplaySearchResultsActivity extends Activity
{
	private TextView parameter;
	private ListView results;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_search_results);
		
		initComponents();
		populateComponents();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		Intent listTasksIntent = new Intent(this, TVNActivity.class);
		listTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(listTasksIntent);
		finish();
	}
	
	private void initComponents()
	{
		parameter = (TextView) this.findViewById(R.id.displaySearchParameterLabel);
		results = (ListView) this.findViewById(R.id.displaySearchResultsList);
	}
	
	private void populateComponents()
	{
		final Bundle extras = this.getIntent().getExtras();
		
		@SuppressWarnings("unchecked")
		final HashMap<Notebook, Note> resultsMap = (HashMap<Notebook, Note>) extras
				.getSerializable(TVNProperties.SerializableKeys.RESULT_MAP);
		
		parameter.setText(extras
				.getString(TVNProperties.SerializableKeys.SEARCH_PARAM));
		
		if(!resultsMap.isEmpty())
		{
			final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			
			for(Map.Entry<Notebook, Note> entry : resultsMap.entrySet())
			{
				final Map<String, Object> datum = new HashMap<String, Object>(2);
				datum.put("Note", entry.getValue());
				datum.put("Notebook", entry.getKey());
				
				data.add(datum);
			}

			results.setAdapter(new SimpleAdapter(this, data,
					android.R.layout.simple_list_item_2, new String[]
					{ "Note", "Notebook" }, new int[]
					{ android.R.id.text1, android.R.id.text2 }));
			results.setTextFilterEnabled(true);
			results.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1,
						int arg2, long id)
				{
					@SuppressWarnings("unchecked")
					final HashMap<String, Object> map = (HashMap<String, Object>) adapterView
							.getItemAtPosition((int) id);

					Notebook notebook = null;
					Note note = null;

					for (Map.Entry<String, Object> entry : map.entrySet())
					{
						if (entry.getKey().equals("Note"))
						{
							note = (Note) entry.getValue();
						}

						if (entry.getKey().equals("Notebook"))
						{
							notebook = (Notebook) entry.getValue();
						}
					}

					final Intent intent = new Intent(
							DisplaySearchResultsActivity.this,
							DisplayNoteActivity.class);

					intent.putExtra(TVNProperties.SerializableKeys.NOTE, note);
					intent.putExtra(TVNProperties.SerializableKeys.NOTEBOOK,
							notebook);

					startActivity(intent);
					finish();
				}
			});
		}
		
	}
}
