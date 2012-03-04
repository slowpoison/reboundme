package com.android.rebound;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HActivity {
	private String startTime, notes, exercise;
	private String name;
	private HActivitySet[] sets;
	
	public HActivity(String actJson) {
		try {
			JSONObject act = (JSONObject) new JSONTokener(actJson).nextValue();
			startTime = act.getString("start_time");
			notes = act.getString("notes");
			// only one exercise for now
			JSONObject exerciseObj = (JSONObject) act.getJSONArray("exercises").get(0);
			exercise = exerciseObj.getString("secondary_type");
			sets = HActivitySet.parse(exerciseObj.getJSONArray("sets"));
			name = exerciseObj.getString("secondary_type");
		} catch (JSONException e) {
			ReboundmeActivity.error(e.getMessage());
		}
	}
	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public HActivitySet[] getSets() {
		return sets;
	}

	public void setSets(HActivitySet[] sets) {
		this.sets = sets;
	}

	public HActivity(String exercise, int repetitions) {
		this.startTime = new Date().toLocaleString();
		this.exercise = exercise;
		this.sets = new HActivitySet[1];
		this.sets[0] = new HActivitySet("0", repetitions, "");
	}

	@Override
	public String toString() {
		return "start: " + startTime + "\nnotes: " + notes + "\nexercise: " + exercise + "\nsets:" + sets.toString();
	}

	public String toJSON() {
		return "{\"start_time\": " + startTime +
					",\"exercises\":[\"primary_type\":\"Other\", \"secondary_type\":" +
					name + "\",\"primary_muscle_group\":\"Shoulders\",\"sets\":["
					+ sets[0].toJSON() + "]]}";
	}
}
