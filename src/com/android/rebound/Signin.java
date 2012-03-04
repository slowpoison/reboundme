package com.android.rebound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Signin extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
	}
	
	public void signin(View v) {
//		startActivity(b);
		Intent doSignin = new Intent(this, WebActivity.class);
		startActivityForResult(doSignin, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String code = data.getStringExtra("RESULT_STRING");
		
		// get the access token
		getAccessTokenAndFinish(code);
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void getAccessTokenAndFinish(String code) {
		String url = "https://runkeeper.com/apps/token";
		HttpPost req = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("client_id", ReboundmeActivity.getClientId()));
		params.add(new BasicNameValuePair("client_secret", ReboundmeActivity.getClientSecret()));
		params.add(new BasicNameValuePair("redirect_uri", "javascript:close()"));

		try {
			req.setEntity(new UrlEncodedFormEntity(params));
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(req);
			String json = inputStreamToString(resp.getEntity().getContent());
			String token = parseAccessToken(json);
			Intent ret = new Intent();
			ret.putExtra("access_token", token);
			setResult(RESULT_OK, ret);
		} catch (Exception e) {
			Intent ret = new Intent();
			ret.putExtra("error", e.getMessage());
			setResult(RESULT_CANCELED, ret);
		}
		finish();
	}
	
	
	private String parseAccessToken(String json) throws JSONException {
		JSONObject obj = (JSONObject) new JSONTokener(json).nextValue();
		return obj.getString("access_token");
	}

	// Fast Implementation
	private String inputStreamToString(InputStream is) throws IOException {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    
	    // Wrap a BufferedReader around the InputStream
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	    // Read response until the end
	    while ((line = rd.readLine()) != null) { 
	        total.append(line); 
	    }
	    
	    // Return full string
	    return total.toString();
	}

}