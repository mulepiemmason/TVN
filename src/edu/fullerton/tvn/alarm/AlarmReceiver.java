package edu.fullerton.tvn.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import edu.fullerton.tvn.activity.NotifyTaskDueActivity;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class AlarmReceiver extends BroadcastReceiver
{
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle extras = intent.getExtras();
		
		Intent notifyIntent = new Intent(context, NotifyTaskDueActivity.class);
		notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notifyIntent.putExtra(TVNProperties.SerializableKeys.TASK_ID,
				extras.getInt(TVNProperties.SerializableKeys.TASK_ID));
		context.startActivity(notifyIntent);
	}
}
