package me.wingspan.rebound.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class Helper {
	
	static String httpGet(HttpGet req) {
		HttpClient client = new DefaultHttpClient();
		HttpResponse resp;
		try {
			resp = client.execute(req);
			return inputStreamToString(resp.getEntity().getContent());
		} catch (Exception e) {
			ReboundmeActivity.error(e.getMessage());
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

	public static String httpPost(HttpPost req, String data) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp;
//			printHeaders(req.getAllHeaders());
//			req.setHeader("Content-Length", String.valueOf(data.length()));
//			req.setHeader("Content-Length", String.valueOf(0));
			StringEntity e = new StringEntity(data, HTTP.UTF_8);
//			e.setContentType("application/json");
			req.setEntity(e);
			resp = client.execute(req);
			ReboundmeActivity.info("Status code " + resp.getStatusLine().getStatusCode());
			return inputStreamToString(resp.getEntity().getContent());
		} catch (ClientProtocolException e) {
			ReboundmeActivity.error("client proto " + e.toString());
			return null;
		} catch (Exception e) {
//			Log.d("ReboudMe", e.getMessage().toString());
			Writer w = new StringWriter();
			PrintWriter pw = new PrintWriter(w);
			e.printStackTrace(pw);
			ReboundmeActivity.error(w.toString());
			return null;
		}
	}
	
	public static void printHeaders(Header[] headers) {
		for (int i=0; i < headers.length; ++i)
			ReboundmeActivity.info(headers[i].getName() + ": " + headers[i].getValue());
	}
}