/**
 * 
 */
package com.rnb.shedule5;


import java.util.ArrayList;
import java.util.Collections;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * @author budukh-rn 29.08.2014
 *
 */
public class FragmentDay extends Fragment {
	
	//public final static String TAG = "rnb";
	private static final String ARG_SECTION_NUMBER = "section_number_Day";
	public final static String EXTRA_ID = "com.rnb.criminalintent.id_crime";
	
	String[] days;
	private ArrayList<Shedule> shedules;
	private int day;
	private TextView textView;
	private Button textViewDetail;
	private Animation animation;
	
	public FragmentDay() {
	}
	
	public static FragmentDay getInstance(int day) {
		FragmentDay fragment = new FragmentDay();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, day);
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//UUID uuid = (UUID) getArguments().getSerializable(EXTRA_ID);
		day = getArguments().getInt(ARG_SECTION_NUMBER);
		
		SheduleLab lab = SheduleLab.getInstance(getActivity(), day);
		
		shedules = lab.getShedules(day);
		
		days = AppUtil.getDaysStrings(getActivity());
		
		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_day, container, false);
		
		final int i = day-1;
		
		GridLayout gridLayout = (GridLayout) view.findViewById(R.id.fragmentDayGrid);
		gridLayout.setBackgroundResource(AppUtil.getColors50()[i]);
		
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), AppUtil.PATH_FONTS_2);
		
		textView = (TextView) view.findViewById(R.id.titleDayText1);
		textView.setTypeface(typeface);
		textViewDetail = (Button) view.findViewById(R.id.textViewDetail1);
		textViewDetail.setTypeface(typeface);			
		

		updateValues(shedules);
		
		
		
		textViewDetail.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				intent.putExtra(Shedule.JSON_NUMBER_DAY+"_", day);
				startActivity(intent);
				return true;
			}
							
		});
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		textViewDetail.startAnimation(animation);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SheduleLab lab = SheduleLab.getInstance(getActivity(), day);
		shedules = lab.getShedules(day);
		updateValues(shedules);
	}
	
	private void updateValues(ArrayList<Shedule> shedules){
				
		final int i = day-1;
		
		textView.setText(days[i]);
		
		Collections.sort(shedules);
		
		StringBuilder sb = new StringBuilder();
		for(Shedule sh: shedules){
			sb.append(sh.getNumberLesson());
			sb.append(". ");
			sb.append(getNameLesson(sh));
			setTimeLesson(sh, sb);
			sb.append("\r\n");
		}
		textViewDetail.setBackgroundResource(AppUtil.getColors500()[i]);
		textViewDetail.setText(sb.toString());
		
		
	}
	
	private void setTimeLesson(Shedule sh, StringBuilder sb){
		if(sh.getTimeBegin() == null ||  sh.getTimeEnd() == null || 
			sh.getTimeBegin().isEmpty() || sh.getTimeEnd().isEmpty())
			return;
		
		sb.append(" (");
		sb.append(sh.getTimeBegin());
		sb.append("-");
		sb.append(sh.getTimeEnd());
		sb.append(")");
	}
	
	private String getNameLesson(Shedule sh){
		if(sh.getName().isEmpty())
			return sh.getFullname();
		return sh.getName();
	}
		
}
