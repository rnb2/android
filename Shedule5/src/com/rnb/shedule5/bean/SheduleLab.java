/**
 * 
 */
package com.rnb.shedule5.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * @author budukh-rn, 02.09.2014
 *
 */
public class SheduleLab {
	
	private static SheduleLab instance;
	
	private Context context;
	private ArrayList<Shedule> shedules;
	private SheduleJSON sheduleJSON;
	//private static final String TAG = "rnb";
	
	/**
	 * @param context2 
	 * 
	 */
	private SheduleLab(Context context2, int day) {
		context = context2;
		
		//загрузка данных из файла json
		sheduleJSON = new SheduleJSON(context, AppUtil.fileNameJSON);		
		try {
			shedules = sheduleJSON.load(day);
			
		//Log.i(TAG, "SheduleLab: shedules.size()=" + shedules.size() + " day=" + day);	
	 	} catch (IOException | JSONException e) {
			e.printStackTrace();
			shedules = new ArrayList<Shedule>();
		}
				
	}
	
	public static SheduleLab getInstance(Context context, int day){
		if(instance == null){
			instance = new SheduleLab(context.getApplicationContext(), day);
		}
		return instance;
	}
	
	/*public void sendEmail() {

	      String[] recipients = {recipient.getText().toString()};
	      Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
	      // prompts email clients only
	      email.setType("message/rfc822");

	      email.putExtra(Intent.EXTRA_EMAIL, recipients);
	      email.putExtra(Intent.EXTRA_SUBJECT, "shedules");
	      email.putExtra(Intent.EXTRA_TEXT, body.getText().toString());

	      try {
		    // the user can choose the email client
	         startActivity(Intent.createChooser(email, "Choose an email client from..."));
	     
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(MainActivity.this, "No email client installed.",
	        		 Toast.LENGTH_LONG).show();
	      }
	   }*/
	
	public Shedule getShedule(UUID uuid){
		for (Shedule c : shedules) {
			if (c.getId().equals(uuid)) {
				return c;
			}
		}
		return null;
	}
		
	public void addShedule(Shedule c){
		shedules.add(c);
	}
	
	public void removeShedule(Shedule c){
		shedules.remove(c);
	}
	
	public boolean saveShedule(){
		try{
			sheduleJSON.save(shedules);
			//Log.i(TAG, "save crime !!!!");
		}catch(Exception x){
			//Log.i(TAG, "ERRor save crime !!!!", x);
			Toast.makeText(context, "Error save saveShedule !!!!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public boolean saveSheduleExternal(int day){
		try{
			shedules = new ArrayList<Shedule>();
			HashMap<Integer, ArrayList<Shedule>> map = new HashMap<Integer, ArrayList<Shedule>>();
			ArrayList<Shedule> sheduleDays = sheduleJSON.load(day);
					if(!sheduleDays.isEmpty()){
						map.put(day, sheduleDays);
					}	
			sheduleJSON.saveExternal(map);
			//Toast.makeText(context, R.string.toast_sendData, Toast.LENGTH_SHORT).show();
		}catch(Exception x){
			x.printStackTrace();
			Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public boolean saveSheduleExternalAll(){
		try{
			shedules = new ArrayList<Shedule>();
			HashMap<Integer, ArrayList<Shedule>> map = new HashMap<Integer, ArrayList<Shedule>>();
			for (int i = 1; i < 7; i++) {
				ArrayList<Shedule> sheduleDays = sheduleJSON.load(i);
				
				if(!sheduleDays.isEmpty() && !map.containsKey(i)){
						map.put(i, sheduleDays);
					}	
			} 
			sheduleJSON.saveExternal(map);
			
		}catch(Exception x){
			x.printStackTrace();
			Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public ArrayList<Shedule> getShedules() {
		return shedules;
	}

	public ArrayList<Shedule> getShedules(int day) {
		try {
			shedules = sheduleJSON.load(day);
		} catch (IOException | JSONException e) {
			shedules = new ArrayList<Shedule>();
		}
		return shedules;
	}
}
