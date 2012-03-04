package com.android.rebound;

import java.util.logging.Level;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Hgraph {
	public HActivity[] getAllActivities() {
		HttpGet req = new HttpGet("/strengthTrainingActivities");
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
		
		return parseActivies(Helper.httpGet(req));
	}

	private HActivity[] parseActivies(String actsJson) {
		try {
			JSONObject acts = (JSONObject) new JSONTokener(actsJson).nextValue();
			HActivity hActs[] = new HActivity[Integer.parseInt(acts.getString("size"))];
			JSONArray items = acts.getJSONArray("items");
			
			return hActs;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}