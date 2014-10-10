/**
 * 
 */
package com.rnb.shedule5;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author budukh-rn, 10.09.2014
 *
 */
public class AboutFragment  extends Fragment{

	/**
	 * 
	 */
	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		
		getActivity().getActionBar().setTitle(R.string.action_about);
		
		TextView textView = (TextView) view.findViewById(R.id.textViewAboutVers);
		
		StringBuilder sb  = new StringBuilder();
		sb.append(getResources().getString(R.string.action_about_vers));
		sb.append(" ");
		sb.append(getResources().getString(R.string.version));
		sb.append(" (");
		sb.append(getResources().getString(R.string.versionBuild));
		sb.append(")");
		
		textView.setText(sb.toString());
		
		return view;
	}
}
