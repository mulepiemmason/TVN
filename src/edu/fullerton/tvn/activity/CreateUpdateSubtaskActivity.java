package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.db.adapter.TasksDbAdapter;
import edu.fullerton.tvn.db.datatypes.Subtask;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * Activity for creating and updating a subtask.
 * 
 * @author ryan
 *
 */
public class CreateUpdateSubtaskActivity extends Activity
{
	private TasksDbAdapter taskDbAdapter;
	
	private EditText nameTextField;
	private EditText estimateTextField;
	private EditText todoTextField;
	private Button saveButton;
	
	private Task savedTask;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		taskDbAdapter = new TasksDbAdapter(this);
		taskDbAdapter.open();
		
		setContentView(R.layout.create_update_subtask);
		initComponents();
		
		Bundle extras = this.getIntent().getExtras();
		
		if(extras  != null)
		{
			Subtask savedSubtask = (Subtask) extras
					.getSerializable(TVNProperties.SerializableKeys.SUBTASK);
			
			savedTask = (Task) extras
					.getSerializable(TVNProperties.SerializableKeys.TASK);
			
			if(savedSubtask != null)
			{
				populateFields(savedSubtask);
				
				// find element with matching name and remove
				for(Subtask s : savedTask.getSubtasks())
				{
					if(s.getName().equals(savedSubtask.getName()))
					{
						savedTask.getSubtasks().remove(s);
						break;
					}
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
	
	private void initComponents()
	{
		nameTextField = (EditText) this.findViewById(R.id.createUpdateNameTextField);
		estimateTextField = (EditText) this.findViewById(R.id.createUpdateSubtaskEstimate);
		todoTextField = (EditText) this.findViewById(R.id.createUpdateSubtaskToDo);
		saveButton = (Button) this.findViewById(R.id.saveSubtaskButton);
		saveButton.setOnClickListener(new SaveButtonListener());
	}
	
	private void populateFields(Subtask savedSubtask)
	{
		nameTextField.setText(savedSubtask.getName());
		estimateTextField.setText(String.valueOf(savedSubtask.getEstimate()));
		todoTextField.setText(String.valueOf(savedSubtask.getTodo()));
	}
	
	private class SaveButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Subtask subtask = new Subtask();
			subtask.setName(nameTextField.getText().toString());
			subtask.setEstimate(Integer.valueOf(estimateTextField.getText()
					.toString().trim()));
			subtask.setTodo(Integer.valueOf(todoTextField.getText().toString().trim()));

			boolean duplicate = false;
			for(Subtask s : savedTask.getSubtasks())
			{
				if(s.getName().equals(subtask.getName()))
				{
					duplicate = true;
					break;
				}
			}
			
			if(duplicate)
			{
				// TODO toast thingy
			}
			else
			{
				savedTask.getSubtasks().add(subtask);
				taskDbAdapter.updateRecord(savedTask);
				
				Intent displayTaskIntent = new Intent(
						CreateUpdateSubtaskActivity.this,
						DisplayTaskActivity.class);
				
				displayTaskIntent.putExtra(TVNProperties.SerializableKeys.TASK, savedTask);
				
				startActivity(displayTaskIntent);
				finish();
			}
		}
	}
}
