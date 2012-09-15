package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.TasksDbAdapter;
import edu.fullerton.tvn.db.datatypes.Subtask;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * Activity to display a subtask.
 * 
 * @author ryan
 *
 */
public class DisplaySubtaskActivity extends Activity
{
	private TextView name;
	private TextView estimate;
	private TextView todo;
	
	private Subtask subtask;
	private Task task;
	private TasksDbAdapter dbAdapter;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_subtask);
		
		dbAdapter = new TasksDbAdapter(this);
		dbAdapter.open();
		
		initComponents();
		populateFields();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_subtask_menu, menu);
		
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
			case R.id.update_subtask:
				updateSubtask();
				return true;
			case R.id.delete_subtask:
				deleteSubtask();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void initComponents()
	{
		name = (TextView) this.findViewById(R.id.displaySubtaskName);
		estimate = (TextView) this.findViewById(R.id.displaySubtaskEstimateValue);
		todo = (TextView) this.findViewById(R.id.displaySubtaskTodoValue);
	}
	
	private void populateFields()
	{
		Bundle extras = this.getIntent().getExtras();
		
		this.subtask = (Subtask) extras
				.getSerializable(TVNProperties.SerializableKeys.SUBTASK);
		
		name.setText(subtask.getName());
		estimate.setText(String.valueOf(subtask.getEstimate()));
		todo.setText(String.valueOf(subtask.getTodo()));
		
		// Task that the subtask belongs to
		this.task = (Task) extras.getSerializable(TVNProperties.SerializableKeys.TASK);
	}
	
	private void updateSubtask()
	{
		Intent updateIntent = new Intent(this, CreateUpdateSubtaskActivity.class);
		updateIntent.putExtra(TVNProperties.SerializableKeys.SUBTASK, subtask);
		updateIntent.putExtra(TVNProperties.SerializableKeys.TASK, task);
		startActivity(updateIntent);
		finish();
	}
	
	private void deleteSubtask()
	{
		for(Subtask s : task.getSubtasks())
		{
			if(s.getName().equals(subtask.getName()))
			{
				task.getSubtasks().remove(s);
				break;
			}
		}
		
		dbAdapter.updateRecord(task);

		Intent displayTaskIntent = new Intent(this, DisplayTaskActivity.class);
		
		displayTaskIntent.putExtra(TVNProperties.SerializableKeys.TASK, task);

		startActivity(displayTaskIntent);
		finish();
	}
}
