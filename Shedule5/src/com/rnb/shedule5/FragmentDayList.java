/**
 * 
 */
package com.rnb.shedule5;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rnb.shedule5.adapters.SheduleAdapter;
import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * Фрагмент списка уроков
 * @author budukh-rn, 10.10.2014
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
	private SheduleAdapter adapter;
	 
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
		
		/*ArrayAdapter<Shedule> adapter = new ArrayAdapter<Shedule>(getActivity(),
		        android.R.layout.simple_list_item_1, shedules); // data
		*/
		
		adapter = new SheduleAdapter(getActivity(), shedules);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final int i = day-1;
		
		View view = inflater.inflate(R.layout.fragment_day_list, container, false);
		
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.fragmentDayLayout);
		layout.setBackgroundResource(AppUtil.getColors50()[i]);
		
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), AppUtil.PATH_FONTS_2);
		
		
		textViewCaption = (TextView) view.findViewById(R.id.titleDayText1);
		textViewCaption.setTypeface(typeface);
		textViewCaption.setPaintFlags(textViewCaption.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		
				
		TextView emptyText = (TextView) view.findViewById(android.R.id.empty);
		emptyText.setTypeface(typeface);		
		
		updateValues(shedules);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SheduleLab lab = SheduleLab.getInstance(getActivity(), day);
		shedules = lab.getShedules(day);
		Collections.sort(shedules);
		
		adapter.getList().clear();
		adapter.getList().addAll(shedules);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		/*Intent intent = new Intent(getActivity(), SettingActivity.class);
		intent.putExtra(Shedule.JSON_NUMBER_DAY+"_", day);

		SheduleAdapter listAdapter = (SheduleAdapter) getListAdapter();
		Shedule shedule = listAdapter.getItem(position);
		
		intent.putExtra(Shedule.JSON_ID2, shedule.getId());
		startActivityForResult(intent, 1);*/
	}
	
	/**
	 * 
	 * @param shedules
	 */
	private void updateValues(ArrayList<Shedule> shedules){
		final int i = day-1;
		textViewCaption.setText(days[i]);
		Collections.sort(shedules);
	}

}
