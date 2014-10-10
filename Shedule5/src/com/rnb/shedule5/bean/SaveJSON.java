/**
 * 
 */
package com.rnb.shedule5.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.rnb.shedule5.entity.Shedule;

/**
 * @author budukh-rn
 *
 */
public class SaveJSON {

	private Context context;
	private String fileName;

	/**
	 * @param <context>
	 * 
	 */
	public SaveJSON(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
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

				// Toast.makeText(context,
				// context.getFilesDir().getAbsolutePath(),
				// Toast.LENGTH_LONG).show();

				OutputStream outputStream = context.openFileOutput(fileName,
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

}
