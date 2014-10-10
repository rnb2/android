/**
 * 
 */
package com.rnb.shedule5;

import android.app.Fragment;

/**
 * @author budukh-rn, 10.09.2014
 *
 */
public class AboutActivity extends SingleFrameActivity {

	/**
	 * 
	 */
	public AboutActivity() {
	}

	/* (non-Javadoc)
	 * @see com.rnb.shedule5.SingleFrameActivity#createFragment()
	 */
	@Override
	protected Fragment createFragment() {
		return new AboutFragment();
	}

}
