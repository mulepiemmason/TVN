package edu.fullerton.tvn.impexp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.fullerton.tvn.db.datatypes.IDbObject;
import edu.fullerton.tvn.db.datatypes.Notebook;
import edu.fullerton.tvn.db.datatypes.Task;
import edu.fullerton.tvn.properties.TVNProperties;

/**
 * @author ryan
 *
 */
public class ImpExpUtil
{
	private final static String LOG_TAG = "ImpExp";
	private final static String DIR_NAME = "TVN";
	
	/**
	 * 
	 * @param subDirName
	 * @return
	 */
	public static String[] constructFileList(String subDirName)
	{
		List<String> fileList = null;
		
		String state = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(state))
		{
			File subDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ DIR_NAME
					+ File.separator + subDirName);
			
			if(subDir.exists())
			{
				File[] files = subDir.listFiles();
				
				for(int i = 0; i < files.length; i++)
				{
					if(fileList == null)
					{
						fileList = new ArrayList<String>();
					}
					
					// remove the extension
					fileList.add(files[i].getName().split("\\.")[0]);
				}
			}
			else
			{
				Log.e(LOG_TAG, "Nothing to import");
			}
		}
		
		return fileList.toArray(new String[fileList.size()]);
	}

	/**
	 * 
	 * @param filename
	 * @param subDirName
	 * @return
	 */
	public static List<IDbObject> importFile(String filename, String subDirName)
	{
		final String state = Environment.getExternalStorageState();
		List<IDbObject> objects = null;
		
		if(Environment.MEDIA_MOUNTED.equals(state))
		{
			File inputFile = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ DIR_NAME
					+ File.separator + subDirName + File.separator + filename + ".json");
			
			StringBuilder builder = new StringBuilder();
			String line = null;
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				while((line = reader.readLine()) != null)
				{
					builder.append(line);
				}
				
				// GSON needs to be able to create an instance of the type,
				// IDbObject is an interface, so we need to know whether a 
				// task or notebooks are being imported
				if (TVNProperties.ImpExp.TASKS_SUBDIR.equals(subDirName))
				{
					objects = new Gson().fromJson(builder.toString(),
							new TypeToken<Collection<Task>>()
							{
							}.getType());
				} 
				else
				{
					objects = new Gson().fromJson(builder.toString(),
							new TypeToken<Collection<Notebook>>()
							{
							}.getType());
				}
			} 
			catch (FileNotFoundException e)
			{
				Log.e(LOG_TAG, "Could not find file - " + inputFile.getAbsolutePath());
			} 
			catch (IOException e)
			{
				Log.e(LOG_TAG, "Exception reading file - " + inputFile.getAbsolutePath());
			}
		}
		else
		{
			Log.e(LOG_TAG, "SD Card not mounted!");
		}
		
		return objects;
	}

	/**
	 * 
	 * @param objects
	 * @param filename
	 */
	public static void exportObjects(List<IDbObject> objects, String filename,
			String subDirName)
	{
		String exportText = new Gson().toJson(objects);
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			File subDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ DIR_NAME
					+ File.separator + subDirName);

			if (!subDir.exists())
			{
				if (!subDir.mkdirs())
				{
					Log.e(LOG_TAG, "Error creating " + DIR_NAME
							+ File.separator + subDir.getName() + " directory!");
				}
			}

			File output = new File(subDir.getAbsolutePath() + File.separator
					+ filename + ".json");

			try
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						output));
				writer.write(exportText);
				writer.flush();
				writer.close();
			} 
			catch (IOException e)
			{
				Log.e(LOG_TAG, "Error saving file to SD card - " + output);
			}
		} 
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
		{
			Log.e(LOG_TAG, "Media is read only; cannot save file to media.");
		} 
		else
		{
			Log.e(LOG_TAG,
					"Media is unavailable. Make sure SD card is installed");
		}
	}
}
