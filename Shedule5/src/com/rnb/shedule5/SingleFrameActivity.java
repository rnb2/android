/**
 * 
 */
package com.rnb.shedule5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * @author budukh-rn
 *
 */
public abstract class SingleFrameActivity extends FragmentActivity {

	protected abstract  Fragment createFragment() ;
	
	/**
	 * 
	 */
	public SingleFrameActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction()
				.add(R.id.container, createFragment())
				.commit();
		}

	}
	
	
	
}
