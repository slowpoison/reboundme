package com.android.rebound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Helper {
	
	static String httpGet(HttpGet req) {
		HttpClient client = new DefaultHttpClient();
		HttpResponse resp;
		try {
			resp = client.execute(req);
			return inputStreamToString(resp.getEntity().getContent());
		} catch (Exception e) {
			ReboundmeActivity.logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
		
	static String inputStreamToString(InputStream is) throws IOException {
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
