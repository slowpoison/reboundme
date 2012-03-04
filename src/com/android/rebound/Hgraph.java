package com.android.rebound;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Hgraph {
	private HttpGet newGetRequest(String url) {
		HttpGet req = new HttpGet(url);
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
		return req;
	}
	
	public HActivity[] getAllActivities() {
		/*
		HttpGet req = new HttpGet("/strengthTrainingActivities");
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
		*/
		
		return parseActivies(Helper.httpGet(newGetRequest("/strengthTrainingActivities")));
	}

	private HActivity[] parseActivies(String actsJson) {
		try {
			JSONObject acts = (JSONObject) new JSONTokener(actsJson).nextValue();
			HActivity hActs[] = new HActivity[Integer.parseInt(acts.getString("size"))];
			JSONArray items = acts.getJSONArray("items");
			for (int i=0; i < items.length(); ++i) {
				JSONObject o = (JSONObject) items.get(i);
				HttpGet req = newGetRequest(o.getString("uri"));
				String actJson = Helper.httpGet(req);
				hActs[i] = new HActivity(actJson);
			}
			
			return hActs;
		} catch (JSONException e) {
			ReboundmeActivity.error(e.getMessage());
			return null;
		}
	}
}