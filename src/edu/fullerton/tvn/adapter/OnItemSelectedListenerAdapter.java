package edu.fullerton.tvn.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Adapter for the OnItemSelectedListener interface.
 * 
 * @author ryan
 *
 */
public abstract class OnItemSelectedListenerAdapter implements OnItemSelectedListener
{

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> adapterView,
			View arg1,
			int arg2,
			long arg3)
	{
		// implement as needed.
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// implement as needed
	}
}
