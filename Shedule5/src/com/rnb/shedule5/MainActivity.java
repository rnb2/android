package com.rnb.shedule5;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.rnb.shedule5.bean.SheduleLab;
import com.rnb.shedule5.entity.Shedule;
import com.rnb.shedule5.utils.AppUtil;

/**
 * 
 * @author budukh-rn
 * 19 сент. 2014 г.	
 *
 */
public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String TAG = "rnb";

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
	private final String DIR_SD = "schedule5";

	protected int selectDay;

	private Set<String> emailSet;

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
		
		
		emailSet = getEmailSet();
		
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

		int id = item.getItemId();		
		
		//Добавление расписания 18.09.2014
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingActivity.class);
			intent.putExtra(Shedule.JSON_NUMBER_DAY+"_", day);
			startActivity(intent);
			
			return true;
		}
		
		/**
		 * Отправка по почте 17.03.2015
		 */
		if (id == R.id.action_menu_share) {
			final Dialog dialog = new Dialog(this);
			
			dialog.setContentView(R.layout.dialog_send);
			dialog.setTitle(R.string.action_menu_share);
			
			Button buttonCancel = (Button) dialog.findViewById(R.id.action_dialogSend_cancel);
			Button buttonSend = (Button) dialog.findViewById(R.id.action_dialogSend_send);
			final AutoCompleteTextView editTextEmail = (AutoCompleteTextView) dialog.findViewById(R.id.editTextMail);
						
			editTextEmail.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));
			
			final EditText editTextSubject = (EditText) dialog.findViewById(R.id.editTextSubject);
			final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialogSendRadioGroup);
			
			buttonSend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean result = false;
					if(checkEmail(editTextEmail.getText().toString())){
						
						String subject = editTextSubject.getText().toString();
						if(subject.isEmpty())
							subject = getString(R.string.title_subject);
						
						if(radioGroup.getCheckedRadioButtonId() == R.id.radio0Send){
							result = sendEmail(editTextEmail.getText().toString(), subject, false);
						}
						if(radioGroup.getCheckedRadioButtonId() == R.id.radio1Send){
							result = sendEmail(editTextEmail.getText().toString(), subject, true);
						}
					}	
					else
						Toast.makeText(getApplicationContext(), R.string.toast_errorMail, Toast.LENGTH_SHORT).show();
					
					if(result){
						//Toast.makeText(getApplicationContext(), R.string.toast_sendData, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
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
		
		//
		/**
		 * Сохранение на SD карте файлов расписания JSON 16.03.2015
		 *  
		 */
		if (id == R.id.action_menu_save) {
			final Dialog dialog = new Dialog(this);
			
			dialog.setContentView(R.layout.dialog_share);
			dialog.setTitle(R.string.action_menu_save);
			
			Button buttonCancel = (Button) dialog.findViewById(R.id.action_dialogShare_cancel);
			Button buttonSend = (Button) dialog.findViewById(R.id.action_dialogShare_send);
			final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialogShareRadioGroup);
			
			final SheduleLab lab = SheduleLab.getInstance(this, day);
			
			buttonSend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
					if(radioGroup.getCheckedRadioButtonId() == R.id.radio0){
						if(lab.saveSheduleExternal(day))
							Toast.makeText(MainActivity.this, R.string.toast_sendData, Toast.LENGTH_SHORT).show();
					}
					if(radioGroup.getCheckedRadioButtonId() == R.id.radio1){
						if(lab.saveSheduleExternalAll())
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
			
			if (!Environment.getExternalStorageState().equals(
			        Environment.MEDIA_MOUNTED)) {
			      Toast.makeText(MainActivity.this, getString(R.string.toast_SDCard_Error, Environment.getExternalStorageState()), Toast.LENGTH_SHORT).show();
			      return false;
			}
		
			final Dialog dialog = new Dialog(this);
			
			dialog.setContentView(R.layout.dialog_import);
			dialog.setTitle(R.string.action_menu_import);
			
			Button buttonCancel = (Button) dialog.findViewById(R.id.action_dialogImport_cancel);
			Button buttonSend = (Button) dialog.findViewById(R.id.action_dialogImport_send);
			Button buttonPath = (Button) dialog.findViewById(R.id.dialogImportButton_Path);
			RadioButton button0 = (RadioButton) dialog.findViewById(R.id.radio0_import);
			RadioButton button1 = (RadioButton) dialog.findViewById(R.id.radio1_import);
			RadioButton button2 = (RadioButton) dialog.findViewById(R.id.radio2_import);
			final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialogImportRadioGroup);
			
			final EditText path = (EditText) dialog.findViewById(R.id.dialogImportText_Path);
			
			final Spinner spinner = (Spinner) dialog.findViewById(R.id.dialogImportSpinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
			        R.array.Days6, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(selectListener());
			
			
			final String sdPath = getExternalPath();
		    final String fileName = AppUtil.getFileName(day, sdPath);
		    path.setText(fileName);
		   
			
		    final SheduleLab lab = SheduleLab.getInstance(this, day);
			
			OnClickListener radioListener = new OnClickListener() {				
				@Override
				public void onClick(View v) {
					
					RadioButton radioButton = (RadioButton) v;
					path.setVisibility(View.VISIBLE);
					spinner.setVisibility(View.GONE);
					
					switch (radioButton.getId()) {

					case R.id.radio0_import:
						path.setText(fileName);
						break;

					case R.id.radio1_import:
						path.setVisibility(View.GONE);
						spinner.setVisibility(View.VISIBLE);
						Log.i(TAG, "just click radio select="+ selectDay);
						break;

					case R.id.radio2_import:
						path.setText(sdPath);
						break;

					default:
						break;
					}
					
				}
			};
			button0.setOnClickListener(radioListener);
			button1.setOnClickListener(radioListener);
			button2.setOnClickListener(radioListener);
			
			buttonPath.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					path.setText(sdPath);
				}
			});
			
			buttonSend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(path.getText() != null || path.getText().length() != 0){
					
						File file = new File(path.getText().toString());
						
						int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
						
						if (!file.exists() && checkedRadioButtonId != R.id.radio2_import) {
							StringBuilder sb = new StringBuilder();
							sb.append(getString(R.string.toast_file_error));
							sb.append(" ");
							sb.append(path.getText().toString());
							Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
							return;
						}	
						boolean resultSave = false;
						switch (checkedRadioButtonId) {
						case R.id.radio0_import:
							resultSave = lab.saveSheduleExternal(day, path.getText().toString());
							break;

						case R.id.radio1_import:{
							int d = selectDay + 1;
							Log.i(TAG, "d=" + d);
							resultSave = lab.saveSheduleExternal(d, path.getText().toString());
							break;
						}	
						case R.id.radio2_import:
							for (int i = 1; i < 7; i++) {
								String fileName2 = AppUtil.getFileName(i, path.getText().toString());
								File file2 = new File(fileName2);
								if (file2.exists())
									resultSave = lab.saveSheduleExternal(i, path.getText().toString());
							}
							break;	
						default:
							break;
						}
						
						
						if(resultSave)
							Toast.makeText(MainActivity.this, R.string.toast_importData, Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(MainActivity.this, R.string.toast_importDataError, Toast.LENGTH_SHORT).show();
						
						dialog.dismiss();
						
					}
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
	
	private String getExternalPath() {
		File sdPath1 = Environment.getExternalStorageDirectory();
		final File sdPath = new File(sdPath1.getAbsolutePath() + "/" + DIR_SD);
		
		return sdPath.getPath();
	}

	protected boolean checkEmail(String string) {
				
		if(!string.isEmpty() &&  string.matches(AppUtil.emailPattern))
			return true;
				
		return false;
	}

	/**
	 * Отправка email c файлами 
	 * @param email
	 * @param subject
	 * @param week true - все файлы / false - текущий день 
	 * @return
	 */
	protected boolean sendEmail(String email, String subject, boolean week) {
		boolean r = false;
		String message;
		
		try {
            message = "files from schedule! by R.N.B"; 

            final Intent emailIntent = week ? new Intent(android.content.Intent.ACTION_SEND_MULTIPLE) : new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                          new String[] { email });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                          subject);
            if(week)
            	feelFilesAll(emailIntent);
            else
            	feelFiles(emailIntent);
            
            emailIntent
                          .putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent,
                          "Sending email..."));
			r = true; 
          } catch (Throwable t) {
          	r = false;
                Toast.makeText(this,
                              "Request failed try again: " + t.toString(),
                              Toast.LENGTH_LONG).show();
      	}	
		return r;
	}

	/**
	 * Заполнение emailIntent
	 * @param emailIntent 
	 */
	private void feelFilesAll(final Intent emailIntent) {
		
		final String sdPath = getExternalPath();
		ArrayList<Uri> list = new ArrayList<Uri>();
		for (int i = 1; i < 7; i++) {
			String fileName = AppUtil.getFileName(i, sdPath);
			File file2 = new File(fileName);
			Uri URI = null;
			
			if (file2.exists())
				URI = Uri.parse("file://" + fileName);
					
			if (URI != null) {
			    list.add(URI);
			}
			file2 = null;
		}
		 emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);
	}

	/**
	 * Заполнение emailIntent
	 * @param emailIntent 
	 */
	private void feelFiles(final Intent emailIntent) {
		final String sdPath = getExternalPath();
		Uri URI = null;
		String fileName = AppUtil.getFileName(day, sdPath);
		File file2 = new File(fileName);
		if (file2.exists())
			URI = Uri.parse("file://" + fileName);

		if (URI != null) {
			emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
		}
		file2 = null;
	}

	private OnItemSelectedListener selectListener() {
		return new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectDay = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				selectDay = -1;
			}

		};
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
	
	/**
	 * получение email контактов
	 * 17.03.2015
	 * @return
	 */
	private Set<String> getEmailSet() {
		Account[] accounts = AccountManager.get(this).getAccounts();
		emailSet = new HashSet<String>();
			for (Account account : accounts) {
				if (AppUtil.EMAIL_PATTERN.matcher(account.name).matches()) {
					emailSet.add(account.name);
					Log.i(TAG, account.name);
				}
			}
		return emailSet;
	}


}
