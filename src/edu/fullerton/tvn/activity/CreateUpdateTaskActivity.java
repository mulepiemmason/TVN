package edu.fullerton.tvn.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import edu.fullerton.tvn.R;
import edu.fullerton.tvn.TVNActivity;
import edu.fullerton.tvn.adapter.OnItemSelectedListenerAdapter;
import edu.fullerton.tvn.alarm.AlarmReceiver;
import edu.fullerton.tvn.db.adapter.CategoryDbAdapter;
import edu.fullerton.tvn.db.adapter.TasksDbAdapter;
import edu.fullerton.tvn.db.datatypes.Category;
import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.db.datatypes.Subtask;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 * 
 */
public class CreateUpdateTaskActivity extends Activity
{
	private final static String DATE_DELIMITER = "/";
	private final static String TIME_DELIMITER = ":";
	
	private EditText nameTextField;

	private Spinner categorySpinner;
	private Spinner prioritySpinner;
	
	private Integer priority;
	private String category;

	private DatePicker dueDateWidget;
	private TimePicker dueTimeWidget;
	
	private int hour;
	private int minute;

	private CheckBox alarmCheckBox;
	
	private List<Subtask> subtasks;

	private Button saveButton;

	private TasksDbAdapter taskDbAdapter;
	
	private int savedKey;
	
	private boolean update = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		taskDbAdapter = new TasksDbAdapter(this);
		taskDbAdapter.open();

		setContentView(R.layout.create_update_task);
		initComponents();

		Bundle extras = this.getIntent().getExtras();
		
		if(extras != null)
		{
			Task savedTask = (Task) extras
					.getSerializable(TVNProperties.SerializableKeys.TASK);
			
			if(savedTask != null)
			{
				populateFields(savedTask);
				this.update = true;
				this.savedKey = savedTask.getKey();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void populateFields(Task savedTask)
	{
		nameTextField.setText(savedTask.getName());

		ArrayAdapter<String> categoryAdapter = (ArrayAdapter<String>) categorySpinner
				.getAdapter();
		int position = categoryAdapter.getPosition(savedTask.getCategory());

		categorySpinner.setSelection(position);
		
		String[] date = savedTask.getDueDate().split("/");
		
		dueDateWidget.init(Integer.valueOf(date[2]) ,
				Integer.valueOf(date[0]) - 1, Integer.valueOf(date[1]), null);
		
		String[] times = savedTask.getDueTime().split(TIME_DELIMITER);
		dueTimeWidget.setCurrentHour(Integer.valueOf(times[0]));
		dueTimeWidget.setCurrentMinute(Integer.valueOf(times[1]));
		
		alarmCheckBox.setChecked(savedTask.getAlarm());
		
		ArrayAdapter<CharSequence> priorityAdapter = (ArrayAdapter<CharSequence>) prioritySpinner
				.getAdapter();
		position = priorityAdapter.getPosition(String.valueOf(savedTask.getPriority()));
		
		prioritySpinner.setSelection(position);
		
		subtasks = savedTask.getSubtasks();
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
		nameTextField = (EditText) this.findViewById(R.id.taskNameTextField);

		categorySpinner = (Spinner) this.findViewById(R.id.categorySpinner);
		
		CategoryDbAdapter categoryDbAdapter = new CategoryDbAdapter(this);
		categoryDbAdapter.open();
		
		List<IDbObject> categories = categoryDbAdapter.fetchAll();
		List<String> categoryNames = new ArrayList<String>();
		
		for(IDbObject obj : categories)
		{
			Category c = (Category) obj;
			categoryNames.add(c.getName());
		}
		
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				categoryNames.toArray(new String[categoryNames.size()]));
		
		categoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		categorySpinner.setAdapter(categoryAdapter);
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View arg1,
					int arg2, long arg3)
			{
				category = adapterView.getItemAtPosition(arg2).toString();
			}
		});
		
		categoryDbAdapter.close();
		
		prioritySpinner = (Spinner) this.findViewById(R.id.prioritySpinner);
		
		// populate the priority spinner choices
		ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter
				.createFromResource(this, R.array.Priority_Array,
						android.R.layout.simple_spinner_item);
		priorityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prioritySpinner.setAdapter(priorityAdapter);
		prioritySpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView,
					View view,
					int arg2,
					long arg3)
			{
				priority = Integer.valueOf(adapterView.getItemAtPosition(arg2).toString());
			}
		});
		
		dueDateWidget = (DatePicker) this.findViewById(R.id.dueDateWidget);
		dueTimeWidget = (TimePicker) this.findViewById(R.id.dueTimeWidget);
		dueTimeWidget.setIs24HourView(true);
		dueTimeWidget.setOnTimeChangedListener(new OnTimeChangedListener()
		{
			@Override
			public void onTimeChanged(TimePicker view, int changedHour, int changedMinute)
			{
				hour = changedHour;
				minute = changedMinute;
			}
		});
		
		hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		minute = Calendar.getInstance().get(Calendar.MINUTE);

		alarmCheckBox = (CheckBox) this.findViewById(R.id.alarmCheckBox);

		saveButton = (Button) this.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new SaveButtonListener());
	}

	private class SaveButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Task dbObj = new Task();
			dbObj.setName(nameTextField.getText().toString());
			dbObj.setCategory(category);
			dbObj.setPriority(priority);
			
			int month = dueDateWidget.getMonth() + 1;
			dbObj.setDueDate(String.valueOf(month + DATE_DELIMITER
					+ dueDateWidget.getDayOfMonth() + DATE_DELIMITER
					+ dueDateWidget.getYear()));
			dbObj.setDueTime(String.valueOf(hour + TIME_DELIMITER + minute));
			dbObj.setAlarm(alarmCheckBox.isChecked());
			
			if(update)
			{
				dbObj.setKey(savedKey);
				dbObj.setSubtasks(subtasks);
				taskDbAdapter.updateRecord(dbObj);
				
				if (dbObj.getAlarm())
				{
					setAlarm(savedKey);
				}
			}
			else
			{
				int id = taskDbAdapter.createRecord(dbObj);
				
				if (dbObj.getAlarm())
				{
					setAlarm(id);
				}
			}
			
			Intent listTasksIntent = new Intent(CreateUpdateTaskActivity.this,
					TVNActivity.class);
			listTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(listTasksIntent);
			finish();
		}
		
		private void setAlarm(int id)
		{
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DATE, dueDateWidget.getDayOfMonth());
			calendar.set(Calendar.MONTH, dueDateWidget.getMonth());
			calendar.set(Calendar.YEAR, dueDateWidget.getYear());
			calendar.set(Calendar.HOUR_OF_DAY, dueTimeWidget.getCurrentHour());
			calendar.set(Calendar.MINUTE, dueTimeWidget.getCurrentMinute());
			calendar.set(Calendar.SECOND, 0);

			Intent intent = new Intent(CreateUpdateTaskActivity.this,
					AlarmReceiver.class);
			intent.putExtra(TVNProperties.SerializableKeys.TASK_ID, id);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					CreateUpdateTaskActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);
		}
	}
}
