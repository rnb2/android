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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	private int position = -1;
	private SheduleLab lab;
	 
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
		
		lab = SheduleLab.getInstance(getActivity(), day);
		
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
		
		//registerForContextMenu(getListView());
		//registerForContextMenu(view.findViewById(android.R.id.list));
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
		
		registerForContextMenu(view.findViewById(android.R.id.list));
				
		return view;
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		lab = SheduleLab.getInstance(getActivity(), day);
		shedules = lab.getShedules(day);
		Collections.sort(shedules);
		
		adapter.getList().clear();
		adapter.getList().addAll(shedules);
		adapter.notifyDataSetChanged();
	
	}
	
	@Override
	public void onPause() {
		unregisterForContextMenu(getListView());
		super.onPause();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		//Log.i(TAG, "onListItemClick " + position);  
			
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    
		position = info.position;
		Shedule shedule = shedules.get(position);
		
		String[] lessonsArray = getResources().getStringArray(R.array.lessons);
		
		menu.setHeaderTitle(lessonsArray[position] + ": " + shedule.getFullname());
	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cntx_item1:
			 doEdit(position);
			break;
			
		case R.id.cntx_item2:
			doDelete(position);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	private void doEdit(int position2) {
		Intent intent = new Intent(getActivity(), SettingEditActivity.class);
		intent.putExtra(Shedule.EXTA_SHEDULE_DAY, day);
		intent.putExtra(Shedule.EXTA_SHEDULE_LESSON, position2);
		intent.putExtra(Shedule.EXTA_SHEDULE_ID, shedules.get(position2).getId());
		startActivity(intent);
		
		//Log.i(TAG, "doEdit... put=" + shedules.get(position2).getId());
		//Toast.makeText(getActivity(), R.string.edit, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Удаление урока
	 * @param position2 
	 */
	private void doDelete(int position2) {
		if(position2 < 0 )
			return;
		
		Shedule shedule = shedules.get(position2);
				
		if(shedule != null){
			lab.removeShedule(shedule);
			boolean saveShedule = lab.saveShedule();
			
			if(saveShedule){
				shedules = lab.getShedules(day);
				adapter.getList().clear();
				adapter.getList().addAll(shedules);
				adapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), R.string.delete, Toast.LENGTH_SHORT).show();
			}
		}
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
