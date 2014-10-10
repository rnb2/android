/**
 * 
 */
package com.rnb.shedule5;

import android.app.Fragment;
import android.os.Bundle;

import com.rnb.shedule5.entity.Shedule;

/**
 * @author budukh-rn
 *
 */
public class SettingActivity extends SingleFrameActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	//private static final String TAG = "rnb";
	//private int day;

	public SettingActivity() {
	}

	@Override
	protected Fragment createFragment() {
		int day2 = getIntent().getIntExtra(Shedule.JSON_NUMBER_DAY+"_", 99);
		return SettingFragmentAdd.getInstance(day2);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		//day = position+1;
	}

	
}
