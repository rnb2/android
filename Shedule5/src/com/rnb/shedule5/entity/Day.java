/**
 * 
 */
package com.rnb.shedule5.entity;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author budukh-rn, 02.09.2014
 *
 */
public class Day {

	private static final String JSON_ID2 = "id";
	private static final String JSON_DAY = "day";
	private static final String JSON_SHEDULES = "shedules";

	private UUID id;
	private Integer day;
	private JSONArray shedules = new JSONArray();

	public Day() {
		id = UUID.randomUUID();
	}

	public Day(JSONObject jsonObject) throws JSONException {
		id = UUID.fromString(jsonObject.getString(JSON_ID2));
		day = jsonObject.getInt(JSON_DAY);
		shedules = jsonObject.getJSONArray(JSON_SHEDULES);
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jO = new JSONObject();

		jO.put(JSON_ID2, id.toString());
		jO.put(JSON_DAY, day);
		jO.put(JSON_SHEDULES, shedules);

		return jO;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public JSONArray getShedules() {
		return shedules;
	}

	public void setShedules(JSONArray shedules) {
		this.shedules = shedules;
	}

	@Override
	public String toString() {
		return day.toString();
	}
}
