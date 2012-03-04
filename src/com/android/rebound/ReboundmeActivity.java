package com.android.rebound;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class ReboundmeActivity extends Activity {
	private String accessToken;
	private Logger logger = Logger.getLogger("ReboundMe");
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("Starting signin");
        Intent signin = new Intent(this, Signin.class);
        startActivityForResult(signin, 1);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			accessToken = data.getStringExtra("access_token");
			logger.info("Got access token " + accessToken);
			
			Intent home = new Intent(this, Home.class);
			startActivity(home);
		} else {
			String error = data.getStringExtra("error");
			Toast.makeText(this, error, Toast.LENGTH_SHORT);
			logger.log(Level.SEVERE, error);
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
}