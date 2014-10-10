/**
 * 
 */
package com.rnb.shedule5;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ListFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * @author budukh-rn
 *
 */
public class FragmentDayList extends ListFragment {
	
	public final static String TAG = "rnb";
	private static final String ARG_SECTION_NUMBER = "section_number_Day";
	public final static String EXTRA_ID = "com.rnb.criminalintent.id_crime";

	String data[] = new String[] { 
			 "one", "two", "three", "four",
			 "one2", "two2", "three2", "four2",
			 "one3", "two3", "three3", "four3"
			 };
	 
		
	private String[] days;
	private ArrayList<Shedule> shedules;
	private int day;
	private TextView textViewCaption;
	 
	 public static FragmentDayList getInstance(int day){
		FragmentDayList fragment = new FragmentDayList();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, day);
		fragment.setArguments(args);
		return fragment;
	 }
	 
	/**
	 * 
	 */
	public FragmentDayList() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		day = getArguments().getInt(ARG_SECTION_NUMBER);
		
		SheduleLab lab = SheduleLab.getInstance(getActivity(), day);
		
		shedules = lab.getShedules(day);
		
		days = AppUtil.getDaysStrings(getActivity());
		
		//animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ArrayAdapter<Shedule> adapter = new ArrayAdapter<Shedule>(getActivity(),
		        android.R.layout.simple_list_item_1, shedules);
		
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final int i = day-1;
		
		View view = inflater.inflate(R.layout.fragment_day_list, null);
		
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.fragmentDayLayout);
		layout.setBackgroundResource(AppUtil.getColors50()[i]);
		
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), AppUtil.PATH_FONTS_2);
		
		textViewCaption = (TextView) view.findViewById(R.id.titleDayText1);
		textViewCaption.setTypeface(typeface);
		
		updateValues(shedules);
		
		return view;
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
		
		textViewCaption.setText(days[i]);
		
		Collections.sort(shedules);
		
		/*StringBuilder sb = new StringBuilder();
		for(Shedule sh: shedules){
			sb.append(sh.getNumberLesson());
			sb.append(". ");
			sb.append(getNameLesson(sh));
			setTimeLesson(sh, sb);
			sb.append("\r\n");
		}
		textViewDetail.setBackgroundResource(AppUtil.getColors500()[i]);
		textViewDetail.setText(sb.toString());
		*/
		
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
