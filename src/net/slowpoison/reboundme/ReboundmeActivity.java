package net.slowpoison.reboundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ReboundmeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent signin = new Intent(this, Signin.class);
        startActivityForResult(signin, 1);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}