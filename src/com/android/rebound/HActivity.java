package com.android.rebound;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HActivity {
	private String startTime, notes;
	private HActivitySet[] sets;

	public HActivity(String actJson) {
		try {
			JSONObject act = (JSONObject) new JSONTokener(actJson).nextValue();
			startTime = act.getString("start_time");
			notes = act.getString("notes");
			JSONObject exercise = (JSONObject) act.getJSONArray("exercises").get(0);
			sets = HActivitySet.parse(act.getJSONArray("sets"));
		} catch (JSONException e) {
			ReboundmeActivity.error(e.getMessage());
		}
	}
}
