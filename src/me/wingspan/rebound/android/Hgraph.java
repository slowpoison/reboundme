package me.wingspan.rebound.android;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Hgraph {
	private static HttpGet newGetRequest(String url) {
		HttpGet req = new HttpGet("http://api.runkeeper.com/" + url);
		addMandatoryHeaders(req);
		return req;
	}
	
	private static void addMandatoryHeaders(HttpRequest req) {
		req.addHeader("Host", "api.runkeeper.com");
		req.addHeader("Authorization", "Bearer " + ReboundmeActivity.getAccessToken());
		req.addHeader("Accept", "application/vnd.com.runkeeper.StrengthTrainingActivityFeed+json");
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
	
	public static void addActivity(String exercise, int repetitions) {
		HActivity act = new HActivity(exercise, repetitions);
		ReboundmeActivity.info("activities json " + act.toJSON() + " " + act.toJSON().length());
		String resp = Helper.httpPost(newHttpPost("strengthTrainingActivities",
				"application/vnd.com.runkeeper.NewStrengthTrainingActivity+json"), act.toJSON());
		ReboundmeActivity.info(resp);
	}

	
	private static HttpPost newHttpPost(String url, String contentType) {
		HttpPost req = new HttpPost("http://api.runkeeper.com/" + url);
		addMandatoryHeaders(req);
		req.addHeader("Content-Type", contentType);
		
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