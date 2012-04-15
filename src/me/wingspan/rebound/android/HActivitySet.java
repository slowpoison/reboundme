package me.wingspan.rebound.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HActivitySet {
	private String weight, notes;
	private int repetitions;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public HActivitySet(String weight, int repetitions, String notes) {
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
				sets[i] = new HActivitySet(o.getString("weight"), Integer.parseInt(o.getString("repetitions")), o.getString("notes"));
			}
			return sets;
		} catch (JSONException e) {
			ReboundmeActivity.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String toString() {
		return "weight: " + weight + "\nrepetitions: " + repetitions + "\nnotes: " + notes;
	}

	public String toJSON() {
		return "{\"weight\":" + weight + ",\"repetitions\":" + repetitions + "}";
	}
}