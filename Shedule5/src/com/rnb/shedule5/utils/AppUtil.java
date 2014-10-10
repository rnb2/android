/**
 * 
 */
package com.rnb.shedule5.utils;

import android.content.Context;
import android.os.Environment;

import com.rnb.shedule5.R;

/**
 * @author budukh-rn, 03.09.2014
 *
 */
public final class AppUtil {
	
	public static final String PATH_FONTS_2 = "fonts/ytX8zlzL.ttf";
	public static final String fileNameJSON = "shedules";
	public static final String filePrefix = ".json"; 
	public static final String directorySd = Environment.getExternalStorageDirectory().getAbsolutePath() + "/schedule5"; 

	/**
	 * 
	 */
	public AppUtil() {
	}

	public static String[] getDaysStrings(Context context) {
		return new String[] { 
				context.getString(R.string.title_day1),
				context.getString(R.string.title_day2),
				context.getString(R.string.title_day3),
				context.getString(R.string.title_day4),
				context.getString(R.string.title_day5),
				context.getString(R.string.title_day6),
				context.getString(R.string.title_day7)};
	}
	
	public static Integer[] getColors500(){
		return new Integer[]{
				R.color.Indigo500,
				R.color.Blue500,
				R.color.Purple500,
				R.color.Pink500,
				R.color.Red500,
				R.color.Lime500,
				R.color.DeepOrange500
		};
	}
	
	public static Integer[] getColors50(){
		return new Integer[]{
				R.color.Indigo50,
				R.color.Blue50,
				R.color.Purple50,
				R.color.Pink50,
				R.color.Red50,
				R.color.Lime50,
				R.color.DeepOrange50
		};
	}
}
