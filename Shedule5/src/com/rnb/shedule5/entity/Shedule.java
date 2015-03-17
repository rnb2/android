/**
 * 
 */
package com.rnb.shedule5.entity;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author budukh-rn, 02.09.2014
 *
 */
public class Shedule implements Serializable, Comparable<Shedule>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String EXTA_SHEDULE_ID = "com.rnb.shedule5.entity.shedule.shedule_id";
	public static final String EXTA_SHEDULE_LESSON = "com.rnb.shedule5.entity.shedule.numberLesson";
	public static final String EXTA_SHEDULE_DAY = "com.rnb.shedule5.entity.shedule.numberDay";
	public static final String JSON_NAME = "name";
	public static final String JSON_FULLNAME = "fullname";
	public static final String JSON_NUMBER_LESSON = "numberLesson";
	public static final String JSON_NUMBER_DAY = "numberDay";
	public static final String JSON_ID2 = "id";
	public static final String JSON_TIME_B = "timeBegin";
	public static final String JSON_TIME_E = "timeEnd";

	private String name;
	private String fullname;
	private Integer numberLesson;
	private Integer numberDay;
	private String timeBegin;
	private String timeEnd;
	private UUID id;

	public Shedule() {
		id = UUID.randomUUID();
	}

	public Shedule(JSONObject object) throws JSONException {
		id = UUID.fromString(object.getString(JSON_ID2));
		name = object.getString(JSON_NAME);
		fullname = object.getString(JSON_FULLNAME);
		
		if(object.has(JSON_TIME_B))
			timeBegin = object.getString(JSON_TIME_B);
		
		if(object.has(JSON_TIME_E))
			timeEnd = object.getString(JSON_TIME_E);
		
		numberLesson = object.getInt(JSON_NUMBER_LESSON);
		numberDay = object.getInt(JSON_NUMBER_DAY);
	}

	public JSONObject toJsonObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_ID2, id);
		jsonObject.put(JSON_NUMBER_LESSON, numberLesson);
		jsonObject.put(JSON_NUMBER_DAY, numberDay);
		
		if (timeBegin != null)
			jsonObject.put(JSON_TIME_B, timeBegin);
		if (timeEnd != null)
			jsonObject.put(JSON_TIME_E, timeEnd);
		
		if (fullname != null)
			jsonObject.put(JSON_FULLNAME, fullname);
		
		jsonObject.put(JSON_NAME, name);

		return jsonObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getNumberLesson() {
		return numberLesson;
	}

	public void setNumberLesson(Integer numberLesson) {
		this.numberLesson = numberLesson;
	}

	public Integer getNumberDay() {
		return numberDay;
	}

	public void setNumberDay(Integer numberDay) {
		this.numberDay = numberDay;
	}

	public String getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Shedule another) {
		if(this.numberLesson != null){
			return this.numberLesson.compareTo(another.getNumberLesson());
		}
		return 0;
	}
}
