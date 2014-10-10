/**
 * 
 */
package com.rnb.shedule5.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * @author budukh-rn, 02.09.2014
 *
 */
public class SheduleJSON {

	private static final String prefix = "_";
	private String fileName;
	private Context context;


	/**
	 * 
	 */
	public SheduleJSON() {
	}

	public SheduleJSON(Context context, String fileName) {
		super();
		this.context = context;
		this.fileName = fileName;
	}

	/**
	 * Загрузка из fileName
	 * 
	 * @param day
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Shedule getSheduleDay(Integer day) throws IOException,
			JSONException {
		Shedule shedule = null;
		BufferedReader reader = null;

		try {
			String name = getFileName(day);
			
			InputStream inputStream = context.openFileInput(name);
			reader = new BufferedReader(new InputStreamReader(inputStream));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONArray array = (JSONArray) new JSONTokener(sb.toString())
					.nextValue();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (day == object.getInt(Shedule.JSON_NUMBER_DAY)){
					shedule = new Shedule(object);
					break;
				}	
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (reader != null)
				reader.close();
		}

		return shedule;
	}
	
	/**
	 * Загрузка из fileName
	 * 
	 * @param day
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<Shedule> load(Integer day) throws IOException,
			JSONException {
		ArrayList<Shedule> list = new ArrayList<Shedule>();
		BufferedReader reader = null;

		try {
			String name = getFileName(day);
			
			InputStream inputStream = context.openFileInput(name);
			reader = new BufferedReader(new InputStreamReader(inputStream));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONArray array = (JSONArray) new JSONTokener(sb.toString())
					.nextValue();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (day == object.getInt(Shedule.JSON_NUMBER_DAY))
					list.add(new Shedule(object));
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (reader != null)
				reader.close();
		}

		return list;
	}

	/**
	 * Сохранение в файл на SD карту
	 * 
	 * @param shedules
	 */
	public void saveExternal(HashMap<Integer, ArrayList<Shedule>> map) {
		
		File dir = new File (AppUtil.directorySd);
		dir.mkdir();
		
		/*Log.i("rnb",  " map="+ map.size());
		
		for(Integer key :map.keySet()){
			Log.i("rnb",  " key="+ key);
		}*/
		
		for(Integer key :map.keySet()){
			//Log.i("rnb",  "2: key="+ key);
			
			ArrayList<Shedule> shedules = map.get(key);
			//Log.i("rnb",  "2: shedules="+ shedules);		
			
			JSONArray jsonArray = new JSONArray();
			String name = getFileName(key);
			File file = new File(dir, name);
			FileOutputStream f = null;

			for (Shedule shedule : shedules) {
				jsonArray = getArrayDay(shedule, jsonArray);
			}
	
			OutputStreamWriter writer = null;
			try {
				f = new FileOutputStream(file);
				writer = new OutputStreamWriter(f);
				writer.write(jsonArray.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
						f.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
				
		}
	}
	
	private JSONArray getArrayDay(Shedule shedule, JSONArray array) {
		try {
			array.put(shedule.toJsonObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return array;
	}
	
	/**
	 * Сохранение в файл
	 * 
	 * @param shedules
	 */
	public void save(ArrayList<Shedule> shedules) {
		
			
		JSONArray jsonArray = new JSONArray();

		for (Shedule crime : shedules) {

			Writer writer = null;

			try {

				jsonArray.put(crime.toJsonObject());

				String name = getFileName(crime.getNumberDay());
				OutputStream outputStream = context.openFileOutput(name,
						Context.MODE_PRIVATE);
				writer = new OutputStreamWriter(outputStream);
				writer.write(jsonArray.toString());
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	private String getFileName(Integer numberDay) {
		StringBuilder sb = new StringBuilder();
		sb.append(fileName);
		sb.append(prefix);
		sb.append(numberDay);
		sb.append(AppUtil.filePrefix);
		return sb.toString();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
