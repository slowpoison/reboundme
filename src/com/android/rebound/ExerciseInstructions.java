package com.android.rebound;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

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
        
            VideoView videoView = (VideoView) findViewById(R.id.videoView1);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse("rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp");
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
        }
}