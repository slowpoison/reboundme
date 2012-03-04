package com.android.rebound;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HActivitySet {
	private String weight, repetitions, notes;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(String repetitions) {
		this.repetitions = repetitions;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public HActivitySet(String weight, String repetitions, String notes) {
		this.weight = weight;
		this.repetitions = repetitions;
		this.notes = notes;
	}

	public static HActivitySet[] parse(JSONArray setsJson) {
		try {
			HActivitySet[] sets = new HActivitySet[setsJson.length()];
			for (int i=0; i < setsJson.length(); ++i) {
				JSONObject o;
				o = (JSONObject) setsJson.get(i);
				sets[i] = new HActivitySet(o.getString("weight"), o.getString("repetitions"), o.getString("notes"));
			}
			return sets;
		} catch (JSONException e) {
			ReboundmeActivity.error(e.getMessage());
			return null;
		}
	}

}