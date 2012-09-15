package edu.fullerton.tvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.TVNActivity;
import edu.fullerton.tvn.db.adapter.TasksDbAdapter;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class NotifyTaskDueActivity extends Activity
{
	private TextView taskName;
	private Button okButton;
	
	private TasksDbAdapter dbAdapter;
	
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notify_due);
		
		dbAdapter = new TasksDbAdapter(this);
		dbAdapter.open();

		initComponents();
		populateComponents();
	}
	
	private void initComponents()
	{
		taskName = (TextView) this.findViewById(R.id.notifyTaskName);
		okButton = (Button) this.findViewById(R.id.notifyButton);
		okButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent listTasksIntent = new Intent(NotifyTaskDueActivity.this,
						TVNActivity.class);
				listTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(listTasksIntent);
				finish();
			}
		});
	}
	
	private void populateComponents()
	{
		Bundle extras = this.getIntent().getExtras();
		int id = extras.getInt(TVNProperties.SerializableKeys.TASK_ID);
		Task task = (Task) dbAdapter.fetchRecord(id);
		taskName.setText(task.getName());
	}
}
