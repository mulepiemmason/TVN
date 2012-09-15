package edu.fullerton.tvn;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import edu.fullerton.tvn.activity.ListNotesActivity;
import edu.fullerton.tvn.activity.ListTasksActivity;

/**
 * 
 * @author ryan
 *
 */
public class TVNActivity extends TabActivity
{
	private final static int DEFAULT_TAB = 0;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		// Adding Tasks tab to tab pane
		intent = new Intent().setClass(this, ListTasksActivity.class);
		spec = tabHost.newTabSpec("tasks").setIndicator("Tasks").setContent(intent);
		tabHost.addTab(spec);
		
		// Adding Notes tab to tab pane
		intent = new Intent().setClass(this, ListNotesActivity.class);
		spec = tabHost.newTabSpec("notes").setIndicator("Notebooks").setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(DEFAULT_TAB);
	}
}