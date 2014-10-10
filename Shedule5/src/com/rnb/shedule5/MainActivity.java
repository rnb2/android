package com.rnb.shedule5;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	//private static final String TAG = "rnb";

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private int day = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		
				
		day = position+1;
		
		//Log.i(TAG, "MainActivity: onNavigationDrawerItemSelected position=" + position + " day=" + day);
		
		if(day < 7){
		
			fragmentManager.beginTransaction()
			.replace(R.id.container, FragmentDayList.getInstance(day))
			.commit();
			
			/*fragmentManager
				.beginTransaction()
				.replace(R.id.container,
				 FragmentDay.getInstance(day)).commit();*/
		}
	

	}

	public void onSectionAttached(int number) {
		/*switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}*/
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		
		//Добавление расписания
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingActivity.class);
			intent.putExtra(Shedule.JSON_NUMBER_DAY+"_", day);
			startActivity(intent);
			
			return true;
		}
		
		//Сохранение на SD карте файлов расписания JSON / отправка по почте 18.09.2014
		if (id == R.id.action_menu_share) {
			final Dialog dialog = new Dialog(this);
			
			dialog.setContentView(R.layout.dialog_share);
			dialog.setTitle(R.string.action_menu_share);
			
			Button buttonCancel = (Button) dialog.findViewById(R.id.action_dialogShare_cancel);
			Button buttonSend = (Button) dialog.findViewById(R.id.action_dialogShare_send);
			final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialogShareRadioGroup);
			
			final SheduleLab instance = SheduleLab.getInstance(this, day);
			
			buttonSend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(radioGroup.getCheckedRadioButtonId() == R.id.radio0){
						if(instance.saveSheduleExternal(day))
							Toast.makeText(MainActivity.this, R.string.toast_sendData, Toast.LENGTH_SHORT).show();
					}
					if(radioGroup.getCheckedRadioButtonId() == R.id.radio1){
						if(instance.saveSheduleExternalAll())
							Toast.makeText(MainActivity.this, R.string.toast_sendData, Toast.LENGTH_SHORT).show();
					}
					dialog.dismiss();
				}
			});
			
			buttonCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			dialog.show();
			return true;
		}
		//--
		
		//Импорт с SD карты файлов расписания JSON 19.09.2014
		if (id == R.id.action_menu_import) {
		
			final Dialog dialog = new Dialog(this);
			
			dialog.setContentView(R.layout.dialog_import);
			dialog.setTitle(R.string.action_menu_import);
			
			Button buttonCancel = (Button) dialog.findViewById(R.id.action_dialogImport_cancel);
			Button buttonSend = (Button) dialog.findViewById(R.id.action_dialogImport_send);
			final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialogImportRadioGroup);
			final TextView path = (TextView) dialog.findViewById(R.id.dialogImportText_Path);
			path.setText(null);
			
			final SheduleLab instance = SheduleLab.getInstance(this, day);
			
			buttonSend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(path.getText() == null || path.getText().length() == 0)
						Toast.makeText(MainActivity.this, "select path=" + path.getText(), Toast.LENGTH_SHORT).show();
					
				}
			});
			
			buttonCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			dialog.show();
			return true;
		}

		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
