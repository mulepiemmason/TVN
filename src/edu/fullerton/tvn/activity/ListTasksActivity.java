package edu.fullerton.tvn.activity;

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
import edu.fullerton.tvn.db.adapter.CategoryDbAdapter;
import edu.fullerton.tvn.db.adapter.TasksDbAdapter;
import edu.fullerton.tvn.db.datatypes.Category;
import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.impexp.ImpExpUtil;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class ListTasksActivity extends ListActivity
{
	private TasksDbAdapter dbAdapter;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		dbAdapter = new TasksDbAdapter(this);
		dbAdapter.open();
		
		populateList();
		
		dbAdapter.close();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_task_menu, menu);
		
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
			case R.id.new_task:
				createTask();
				return true;
			case R.id.manage_categories:
				displayCategories();
				return true;
			case R.id.import_string:
				importTasks();
				return true;
			case R.id.export_string:
				exportTasks();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * 
	 */
	private void populateList()
	{
		List<IDbObject> tasks = dbAdapter.fetchAll();
		
		if (!tasks.isEmpty())
		{
			setListAdapter(new ArrayAdapter<Object>(this, R.layout.list_tasks,
					tasks.toArray(new Object[tasks.size()])));

			ListView listView = getListView();
			listView.setTextFilterEnabled(true);

			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int arg2, long id)
				{
					Task t = (Task) adapterView.getItemAtPosition((int) id);

					Intent displayTaskIntent = new Intent(
							ListTasksActivity.this, DisplayTaskActivity.class);

					displayTaskIntent.putExtra(
							TVNProperties.SerializableKeys.TASK, t);

					startActivity(displayTaskIntent);
				}
			});
		}
		else
		{
			String[] empty = { "No tasks yet!" };
			
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_tasks,
					empty));
		}
	}
	
	/*
	 * Calls the create/update task view
	 */
	private void createTask()
	{
		final Intent createTaskIntent = new Intent(this, CreateUpdateTaskActivity.class);
		this.startActivity(createTaskIntent);
	}
	
	private void displayCategories()
	{
		final Intent displayCategoriesIntent = new Intent(this, ListCategoriesActivity.class);
		this.startActivity(displayCategoriesIntent);
	}
	
	private void importTasks()
	{
		final String[] files = ImpExpUtil
				.constructFileList(TVNProperties.ImpExp.TASKS_SUBDIR);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select tasks you wish to import:");
		builder.setItems(files, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				final List<IDbObject> tasks = ImpExpUtil.importFile(files[which],
						TVNProperties.ImpExp.TASKS_SUBDIR);
				
				for(IDbObject obj : tasks)
				{
					final Task task = (Task) obj;
					
					final String cat = task.getCategory();
					final CategoryDbAdapter catAdapter = new CategoryDbAdapter(
							ListTasksActivity.this);
					
					final List<IDbObject> categories = catAdapter.fetchAll();
					
					boolean exists = false;
					
					for(IDbObject o : categories)
					{
						final Category category = (Category) o;
						if(category.getName().equalsIgnoreCase(cat))
						{
							exists = true;
							break;
						}
					}
					
					if(!exists)
					{
						Category newCategory = new Category(cat, "Imported");
						catAdapter.createRecord(newCategory);
					}
					
					dbAdapter.createRecord(task);
				}
				
				Intent listTasksIntent = new Intent(ListTasksActivity.this,
						TVNActivity.class);
				listTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(listTasksIntent);
				finish();
			}
		});
		
		builder.create().show();
	}
	
	private void exportTasks()
	{
		final EditText input = new EditText(this);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Enter a name for the file the tasks will be stored in:")
				.setTitle("Enter file name for tasks").setCancelable(true)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						ImpExpUtil.exportObjects(dbAdapter.fetchAll(), input
								.getText().toString(),
								TVNProperties.ImpExp.TASKS_SUBDIR);
					}
				}).setView(input);
		
		builder.create().show();
		
		Toast.makeText(this, "Tasks, " + input.getText().toString()
				+ ", exported!", Toast.LENGTH_LONG).show();
	}
}
