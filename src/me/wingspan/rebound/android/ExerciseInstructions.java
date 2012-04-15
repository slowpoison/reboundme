package me.wingspan.rebound.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExerciseInstructions extends Activity {

		private Button button;
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.instructions);
            button = (Button) findViewById(R.id.button2);
            button.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Intent intent = new Intent(ExerciseInstructions.this, ExerciseActivity.class);
    	            startActivity(intent);      
    	            finish();
    			}
            });
            
           TextView tv = (TextView) findViewById(R.id.textViewIns); 
           
           tv.setText("Instructions: \n1. Lift Arm Level \n2. Raise Arm \n3. Lower Arm  \n4. Repeat");
        
           Button but = (Button) findViewById(R.id.buttonImage);
           
          
           
           but.setOnClickListener(new View.OnClickListener() {
   			public void onClick(View v) {
   			 Uri video = Uri.parse("http://www.youtube.com/watch?v=PCDueAmYd1w&feature=g-upl&context=G24f7702AUAAAAAAAAAA");
             video = Uri.parse("vnd.youtube:"  + video.getQueryParameter("v"));
   				Intent intent = new Intent(Intent.ACTION_VIEW, video);
   				startActivity(intent);
   				}
   			});
        }
}