/**
 * 
 */
package com.rnb.shedule5;

import java.util.UUID;

import android.app.Fragment;

import com.rnb.shedule5.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.rnb.shedule5.entity.Shedule;

/**
 * @author budukh-rn
 * 30 марта 2015 г.	
 *
 */
public class SettingEditActivity extends SingleFrameActivity {

	/**
	 * 
	 */
	public SettingEditActivity() {
	}

	@Override
	protected Fragment createFragment() {
		int day = getIntent().getIntExtra(Shedule.EXTA_SHEDULE_DAY, 99);
		int lesson = getIntent().getIntExtra(Shedule.EXTA_SHEDULE_LESSON, 1);
		UUID id = (UUID) getIntent().getSerializableExtra(Shedule.EXTA_SHEDULE_ID);
		return SettingFragmentEdit.getInstance(day, lesson, id);
	}

}
