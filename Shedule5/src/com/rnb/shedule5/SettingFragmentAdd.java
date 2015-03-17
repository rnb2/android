/**
 * 
 */
package com.rnb.shedule5;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * @author budukh-rn, 10.09.2014
 *
 */
public class SettingFragmentAdd extends Fragment {

	// private static final String TAG = "rnb";
	private int currentDay;
	private SheduleLab lab;
	private int selectLesson;
	private EditText textFullName;
	private EditText textShortName;
	private Button textTimeBegin;
	private Button textTimeEnd;
	private Spinner spinner;
	
	 int DIALOG_TIME_B = 1;
	 int DIALOG_TIME_E = 2;
	 int myHourB = 8;
	 int myMinuteB = 15;
	 int myLesson = 45;

	/**
	 * 
	 */
	private SettingFragmentAdd() {
	}

	public static SettingFragmentAdd getInstance(int day) {
		SettingFragmentAdd fragment = new SettingFragmentAdd();
		Bundle args = new Bundle();
		args.putInt(Shedule.JSON_NUMBER_DAY, day);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		lab.saveShedule();
		//Log.i(TAG, "save shedule");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDay = getArguments().getInt(Shedule.JSON_NUMBER_DAY);
		lab = SheduleLab.getInstance(getActivity(), currentDay);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings_add, container, false);
		
		TextView textDay = (TextView) view.findViewById(R.id.textDay);
		String string = AppUtil.getDaysStrings(getActivity())[currentDay-1];
		textDay.setText(string);
		
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.lessons, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(selectLessonListener());
		
		int position = lab.getShedules().isEmpty() ? 0 : lab.getShedules().size();
		spinner.setSelection(position, true);
		
		textFullName = (EditText) view.findViewById(R.id.editTextFullName);
		textShortName = (EditText) view.findViewById(R.id.editTextShortName);

		textTimeBegin = (Button) view.findViewById(R.id.textTimeBegin);
		textTimeEnd = (Button) view.findViewById(R.id.textTimeEnd);
		
		textTimeBegin.setOnClickListener(textTimeBeginListener(DIALOG_TIME_B));
		textTimeEnd.setOnClickListener(textTimeBeginListener(DIALOG_TIME_E));
		
						
		Button buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
		buttonAdd.setOnClickListener(addListener());

		Button buttonDel = (Button) view.findViewById(R.id.buttonDelete);
		buttonDel.setOnClickListener(deleteListener());
		
	return view;
	}
	
	
	private OnClickListener textTimeBeginListener(final int id) {
		return new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onCreateDialog(id).show(); 	
			}
		};
	}

	private Dialog onCreateDialog(int id) {
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), myCallBack(id), myHourB, myMinuteB, true);
	    return tpd;
	}
	
	private OnTimeSetListener myCallBack(final int id){
		return new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				 if (id == DIALOG_TIME_B) {	
					textTimeBegin.setText(new StringBuilder().append(AppUtil.padding_str(hourOfDay)).append(":").append(AppUtil.padding_str(minute)));
				 }
				 
				 if (id == DIALOG_TIME_E) {	
					 myHourB = hourOfDay;
					 myMinuteB = minute;
					 textTimeEnd.setText(new StringBuilder().append(AppUtil.padding_str(hourOfDay)).append(":").append(AppUtil.padding_str(minute)));
				 }		
				
			};
		};	
	}
	  
	  
	

	
	
	private OnItemSelectedListener selectLessonListener() {
		return new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectLesson = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				selectLesson = -1;
			}

		};
	}
	
	private AlertDialog getDeleteDialog(){
	    int i = selectLesson + 1;
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()); 
	
	    dialog.setTitle(R.string.dialog_title_delete); 
	    dialog.setMessage(getResources().getString(R.string.dialog_msg_delete, i)); 
	    dialog.setIcon(android.R.drawable.ic_dialog_alert);

	    dialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
	            	doDelete();
					dialog.dismiss();
	            }   
	        })

	        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {	            	
	                dialog.dismiss();
	            }
	        });

        return dialog.create();
    }
		
	
	private void doDelete() {
		Shedule forDeleteShedule = null;
		
		if(selectLesson == -1){
			Toast.makeText(getActivity(), getString(R.string.toast_selectLesson), Toast.LENGTH_SHORT).show();
			return;
		}
				
		boolean foundForDeletLesson = false;
		
		int position = selectLesson + 1;
		
		for(Shedule s:	lab.getShedules()){
			if(s.getNumberLesson() == position){
				foundForDeletLesson = true;
				forDeleteShedule = s;
				break;
				
			}	
		}
				
		if(foundForDeletLesson){
			lab.removeShedule(forDeleteShedule);
			lab.saveShedule();
		}	
	}
	
	/**
	 * Удалить Shedule
	 * 
	 * @return
	 */
	private OnClickListener deleteListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
            	if(lab.getShedules().isEmpty()){
        			Toast.makeText(getActivity(), getString(R.string.toast_deleteNoFound), Toast.LENGTH_SHORT).show();
        			return;
        		}
				AlertDialog diaBox = getDeleteDialog();
				diaBox.show();
			}

		};
	}
		
		
	/**
	 * Добавляет в список Shedule
	 * @return
	 */
	private OnClickListener addListener() {
		return new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				
				if(selectLesson == -1){
					Toast.makeText(getActivity(), getString(R.string.toast_selectLesson), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if(textShortName.getText().length() <= 0 && textFullName.getText().length() <= 0){
					Toast.makeText(getActivity(), getString(R.string.toast_inputNameLesson), Toast.LENGTH_LONG).show();
					return;
				}
				
				if(textTimeEnd.getText().length() <= 0 && textTimeBegin.getText().length() > 0){
					Toast.makeText(getActivity(), getString(R.string.toast_inputTimeEnd), Toast.LENGTH_LONG).show();
					return;
				}
				
				if(textTimeEnd.getText().length() > 0 && textTimeBegin.getText().length() <= 0){
					Toast.makeText(getActivity(), getString(R.string.toast_inputTimeBegin), Toast.LENGTH_LONG).show();
					return;
				}
				
						
						
				boolean isDoubleLesson = false;
				
				int position = selectLesson + 1;
				
				for(Shedule s:	lab.getShedules()){
					if(s.getNumberLesson() == position){
						Toast.makeText(getActivity(), getString(R.string.toast_doubleLesson), Toast.LENGTH_LONG).show();
						isDoubleLesson = true;
						break;
						
					}	
				}
				if(isDoubleLesson)
					return;
								
				JSONObject object = new JSONObject();
				try {
					object.put(Shedule.JSON_NUMBER_DAY, currentDay);
					object.put(Shedule.JSON_NUMBER_LESSON, position);
					object.put(Shedule.JSON_NAME, textShortName.getText().toString());
					object.put(Shedule.JSON_FULLNAME, textFullName.getText().toString());

					object.put(Shedule.JSON_TIME_B, textTimeBegin.getText().toString());
					object.put(Shedule.JSON_TIME_E, textTimeEnd.getText().toString());
					
					object.put(Shedule.JSON_ID2, UUID.randomUUID());
				
					Shedule c = new Shedule(object);
					lab.addShedule(c);
					
					String blank = "";
					textFullName.setText(blank);
					textFullName.requestFocus();
					textShortName.setText(blank);
					int length = getResources().getStringArray(R.array.lessons).length;
					
					if(position < length){
						spinner.setSelection(position, true);
						textTimeBegin.setText(new StringBuilder().append(textTimeEnd.getText()));
						textTimeEnd.setText(null);
					}
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		};
	}
}
