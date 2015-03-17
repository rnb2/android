/**
 * 
 */
package com.rnb.shedule5;

import java.text.MessageFormat;
import java.util.UUID;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * @author budukh-rn 30 марта 2015 г.
 *
 */
public class SettingFragmentEdit extends Fragment {

	private int currentDay;
	protected SheduleLab lab;
	private int currentLesson;
	private static final String TAG = "rnb";
	private Shedule shedule;
	 int DIALOG_TIME_B = 1;
	 int DIALOG_TIME_E = 2;
	 int myHourB = 8;
	 int myMinuteB = 15;
	private Button textTimeBegin;
	private Button textTimeEnd;
	

	/**
	 * 
	 */
	private SettingFragmentEdit() {
	}

	public static SettingFragmentEdit getInstance(int day, int lesson, UUID id) {
		SettingFragmentEdit fragment = new SettingFragmentEdit();
		Bundle args = new Bundle();
		args.putInt(Shedule.EXTA_SHEDULE_DAY, day);
		args.putInt(Shedule.EXTA_SHEDULE_LESSON, lesson);
		args.putSerializable(Shedule.EXTA_SHEDULE_ID, id);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		currentDay = getArguments().getInt(Shedule.EXTA_SHEDULE_DAY);
		currentLesson = getArguments().getInt(Shedule.EXTA_SHEDULE_LESSON);
		UUID uuid = (UUID) getArguments().getSerializable(Shedule.EXTA_SHEDULE_ID);

		lab = SheduleLab.getInstance(getActivity(), currentDay);
		shedule = lab.getShedule(uuid);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_settings_edit, container, false);
		Log.i(TAG, MessageFormat.format("edit: day={0} less={1}", currentDay, currentLesson));
		
		TextView textDay = (TextView) view.findViewById(R.id.textDayEdit);
		TextView fullName = (TextView) view.findViewById(R.id.editTextFullNameEdit);
		TextView shortName = (TextView) view.findViewById(R.id.editTextShortNameEdit);
		
		if(shedule != null){
		fullName.setText(shedule.getFullname());
		shortName.setText(shedule.getName());
		}
		
		String string = AppUtil.getDaysStrings(getActivity())[currentDay-1];
		textDay.setText(string);
		
		textTimeBegin = (Button) view.findViewById(R.id.textTimeBeginEdit);
		textTimeEnd = (Button) view.findViewById(R.id.textTimeEndEdit);
		
		String timeBegin = shedule.getTimeBegin() == null ? "" : shedule.getTimeBegin();
		String timeEnd = shedule.getTimeEnd() == null ? "" : shedule.getTimeEnd();
		
		textTimeBegin.setText(timeBegin);
		textTimeEnd.setText(timeEnd);
		
		textTimeBegin.setOnClickListener(textTimeBeginListener(DIALOG_TIME_B));
		textTimeEnd.setOnClickListener(textTimeBeginListener(DIALOG_TIME_E));
		
						
		Button buttonAdd = (Button) view.findViewById(R.id.buttonEdit);
		buttonAdd.setOnClickListener(editListener());
		
		
		return view;
	}

	private OnClickListener editListener() {
		// TODO Auto-generated method stub
		return null;
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

}
