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
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.TVNActivity;
import edu.fullerton.tvn.db.adapter.TasksDbAdapter;
import edu.fullerton.tvn.db.datatypes.Subtask;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class DisplayTaskActivity extends Activity
{
	private TextView name;
	private TextView dueDate;
	private TextView dueTime;
	private TextView category;
	private TextView priority;
	private TextView alarm;
	private ProgressBar progress;
	private ListView subtaskList;
	
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
		
		setContentView(R.layout.display_tasks);
		
		dbAdapter = new TasksDbAdapter(this);
		dbAdapter.open();
		
		initComponents();
		populateComponents();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_task_menu, menu);
		
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
			case R.id.update_task:
				updateTask();
				return true;
			case R.id.delete_task:
				deleteTask();
				return true;
			case R.id.new_subtask:
				createSubtask();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
		name = (TextView) this.findViewById(R.id.displayTaskNameValue);
		dueDate = (TextView) this.findViewById(R.id.displayTaskDueDateValue);
		dueTime = (TextView) this.findViewById(R.id.displayTaskDueTimeValue);
		category = (TextView) this.findViewById(R.id.displayTaskCategoryValue);
		priority = (TextView) this.findViewById(R.id.displayTaskPriorityValue);
		alarm = (TextView) this.findViewById(R.id.displayTaskAlarmValue);
		progress = (ProgressBar) this.findViewById(R.id.displayTaskProgressValue);
		
		subtaskList = (ListView) this.findViewById(R.id.displayTaskSubtaskList);
	}
	
	private void populateComponents()
	{
		Bundle extras = this.getIntent().getExtras();
		
		this.task = (Task) extras
				.getSerializable(TVNProperties.SerializableKeys.TASK);
		
		name.setText(task.getName());
		dueDate.setText(task.getDueDate());
		dueTime.setText(task.getDueTime());
		category.setText(task.getCategory());
		priority.setText(String.valueOf(task.getPriority()));
		alarm.setText(task.getAlarm() ? "Yes" : "No");
		
		List<Subtask> subtasks = task.getSubtasks();
		
		if(!subtasks.isEmpty())
		{
			subtaskList.setAdapter(new ArrayAdapter<Object>(this,
					R.layout.list_subtasks, subtasks
							.toArray(new Object[subtasks.size()])));
			subtaskList.setTextFilterEnabled(true);
			subtaskList.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1,
						int arg2, long id)
				{
					Subtask s = (Subtask) adapterView.getItemAtPosition((int) id);
					
					Intent displaySubtaskIntent = new Intent(
							DisplayTaskActivity.this,
							DisplaySubtaskActivity.class);
					
					displaySubtaskIntent.putExtra(
							TVNProperties.SerializableKeys.TASK, task);
					displaySubtaskIntent.putExtra(
							TVNProperties.SerializableKeys.SUBTASK, s);
					
					startActivity(displaySubtaskIntent);
					finish();
				}
			});
			
			double total = 0.0;
			double todo = 0.0;
			for(Subtask s : subtasks)
			{
				total += s.getEstimate();
				todo += s.getTodo();
			}
			
			double progressPercentage = 100 - ((todo / total) * 100.0);
			
			progress.setProgress((int) progressPercentage);
		}
		else
		{
			String[] empty = { "No subtasks yet!" };
			
			subtaskList.setAdapter(new ArrayAdapter<Object>(this,
					R.layout.list_subtasks, empty));
			
			progress.setProgress(0);
		}
	}
	
	private void updateTask()
	{
		Intent updateIntent = new Intent(this, CreateUpdateTaskActivity.class);
		updateIntent.putExtra(TVNProperties.SerializableKeys.TASK, task);
		startActivity(updateIntent);
	}
	
	private void deleteTask()
	{
		dbAdapter.deleteRecord(task);

		Intent listTasksIntent = new Intent(this, TVNActivity.class);
		listTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(listTasksIntent);
		finish();
	}
	
	private void createSubtask()
	{
		Intent createSubtaskIntent = new Intent(this, CreateUpdateSubtaskActivity.class);
		createSubtaskIntent.putExtra(TVNProperties.SerializableKeys.TASK, task);
		this.startActivity(createSubtaskIntent);
		finish();
	}
}
