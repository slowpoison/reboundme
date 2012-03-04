package com.android.rebound;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Hgraph {
	private static HttpGet newGetRequest(String url) {
		HttpGet req = new HttpGet("http://api.runkeeper.com/" + url);
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
		return req;
	}
	
	public static HActivity[] getAllActivities() {
		/*
		HttpGet req = new HttpGet("/strengthTrainingActivities");
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
		*/
		
		return parseActivies(Helper.httpGet(newGetRequest("strengthTrainingActivities")));
	}
	
	public static void addActivity(String exercise, String repetitions) {
		HActivity act = new HActivity(exercise, repetitions);
		Helper.httpPost(newHttpPost("strengthTrainingActivities"), act.toJSON());
	}

	
	private static HttpPost newHttpPost(String url) {
		HttpPost req = new HttpPost(url);
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
		
		/*
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("client_id", ReboundmeActivity.getClientId()));
		params.add(new BasicNameValuePair("client_secret", ReboundmeActivity.getClientSecret()));
		params.add(new BasicNameValuePair("redirect_uri", "javascript:close()"));
			req.setEntity(new UrlEncodedFormEntity(params));
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(req);
		*/
		return req;
	}

	private static HActivity[] parseActivies(String actsJson) {
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