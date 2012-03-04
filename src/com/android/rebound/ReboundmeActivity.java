package com.android.rebound;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;


public class ReboundmeActivity extends Activity {
	public static final String ACCESS_TOKEN = "accessToken";
	private static String accessToken;
	public static Logger logger = Logger.getLogger("ReboundMe");
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get logged in status
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		accessToken = prefs.getString(ACCESS_TOKEN, null);
		
		Intent signin;
		if (accessToken != null) {
			logger.info("Already signed in with token " + accessToken);
			signin = new Intent(this, Home.class);
			signin.putExtra(ACCESS_TOKEN, accessToken);
		} else {
			signin = new Intent(this, Signin.class);
		}
		
		startActivityForResult(signin, 1);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			accessToken = data.getStringExtra("access_token");
			logger.info("Got access token " + accessToken);
			
			SharedPreferences prefs = getPreferences(MODE_PRIVATE);
			Editor e = prefs.edit();
			e.putString(ACCESS_TOKEN, accessToken);
			e.commit();
			
			Intent home = new Intent(this, Home.class);
			startActivity(home);
		} else {
			String error = data.getStringExtra("error");
			Toast.makeText(this, error, Toast.LENGTH_LONG);
			error(error);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getClientId() {
		return "bdf6d860d6964992a766c4405d7e5638";
	}

	public static String getClientSecret() {
		return "5fe0b3b9e4324f8e94497b4ef4186f8c";
	}

	public static String getAccessToken() {
		return accessToken;
	}
	
	public static void setAuthToken(String token) {
		accessToken = token;
	}

	public static void info(String message) {
		logger.log(Level.INFO, message);
	}
	
	public static void error(String message) {
		logger.log(Level.SEVERE, message);
	}
}