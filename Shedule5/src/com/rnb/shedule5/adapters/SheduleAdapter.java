/**
 * 
 */
package com.rnb.shedule5.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rnb.shedule5.R;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * Отображение списка уроков
 * @author budukh-rn, 10.10.2014
 *
 */
public class SheduleAdapter extends ArrayAdapter<Shedule> {
	
	private Context context;
	private ArrayList<Shedule> list = new ArrayList<Shedule>();
	private LayoutInflater inflater;

	private int mLastPosition = 1;
	
	/**
	 * Отображение списка уроков
	 * @author budukh-rn, 10.10.2014
	 * @param context
	 * @param list
	 */
	public SheduleAdapter(Context context, ArrayList<Shedule> list) {
		super(context, R.layout.day_list_item);
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View  view = convertView;
				
		if(view == null)
				view = inflater.inflate(R.layout.day_list_item, parent, false);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), AppUtil.PATH_FONTS_2);
		
		TextView textTime = (TextView) view.findViewById(R.id.textViewTime);
		TextView textLesson = (TextView) view.findViewById(R.id.textViewLesson);
		
		Shedule shedule = getItem(position);
		
		textTime.setText(getTimeLesson(shedule));
		textLesson.setText(getNameLesson(shedule));
		
		textLesson.setTypeface(typeface);
		
		 // This tells the view where to start based on the direction of the scroll.
        // If the last position to be loaded is <= the current position, we want
        // the views to start below their ending point (500f further down).
        // Otherwise, we start above the ending point.
        float initialTranslation = (mLastPosition <= position ? 500f : -500f);

        view.setTranslationY(initialTranslation);
        view.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        mLastPosition = position;

		
		return view;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Shedule getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private String getTimeLesson(Shedule sh){
		StringBuilder sb = new StringBuilder();
				
		sb.append(sh.getNumberLesson());
		sb.append(". ");
		
		if(sh.getTimeBegin() == null ||  sh.getTimeEnd() == null || 
			sh.getTimeBegin().isEmpty() || sh.getTimeEnd().isEmpty())
			return sb.toString();
	
		sb.append(sh.getTimeBegin());
		sb.append(" - ");
		sb.append(sh.getTimeEnd());
				
		return sb.toString();
	}
	
	private String getNameLesson(Shedule sh){
		if(sh.getName().isEmpty())
			return sh.getFullname();
		return sh.getFullname();
	}

	public ArrayList<Shedule> getList() {
		return list;
	}

	public void setList(ArrayList<Shedule> list) {
		this.list = list;
	}

	public int getmLastPosition() {
		return mLastPosition;
	}

	
}
