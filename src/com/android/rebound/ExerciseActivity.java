package com.android.rebound;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ExerciseActivity extends Activity implements SensorEventListener, TextToSpeech.OnInitListener {
	SensorManager sensorManager = null;
	/*	private float[] initialRotationM = new float[9];
	private float[] currentRotationM = new float[9];
	private float[] tempOrientation = new float[3];
	private float[] tempMagnetic = new float[3];*/

	float zangle=0;
	float yangle=0;
	float xangle=0;
	
	int rangecounter=0;
	
	private static final String[] numbers = {
	      "One",
	      "Two",
	      "Three",
	      "Four",
	      "Five",
	      "Six",
	      "Seven",
	      "Eight",
	      "Nine",
	      "Ten"};
	
	
    private Sensor mRotationVectorSensor;



	private float[] angle = new float[3];
	private int button_pressed;

	//private Vibrator v;

	private static final String TAG = "TextToSpeechDemo";

	private TextToSpeech mTts;
	private Button mAgainButton;

	int count = 0;
	Button button;

	// for accelerometer values
	TextView outputX;
	TextView outputY;
	TextView outputZ;

	// for orientation values
	TextView outputX2;
	TextView outputY2;
	TextView outputZ2;

	// for angle delta values
	TextView outputX3;
	TextView outputY3;
	TextView outputZ3;
	
	TextView output;

	private final float[] mRotationMatrix = new float[16];
	float movavgnum=100;
	float movavg=0;
	float movavgmax=0;
	float movavgmin=0;

	float tempsum=0;
	float upthreshmax = (float) 0.006;
	float upthreshmin = (float) 0.003;
	float downthreshmax = (float) -0.006;
	float downthreshmin = (float) -0.003;

	boolean updetect=false;
	boolean downdetect=false;
	boolean reset=true;
	boolean end=false;
	
	ArrayList<Float> xHist = new ArrayList<Float>(); 
	ArrayList<Float> yHist = new ArrayList<Float>();
	ArrayList<Float> zHist = new ArrayList<Float>();

	ArrayList<Float> xVel = new ArrayList<Float>(); 
	ArrayList<Float> yVel = new ArrayList<Float>();
	ArrayList<Float> zVel = new ArrayList<Float>();

	@SuppressWarnings("unused")
	private void trackMovement(double ang) {
		if((ang >= 165 && ang < 180) || (ang >= -180 && ang < -165))  {
			//v.vibrate(100);
		}
	}

	void CalcAngVel() {
		if (zHist.size()>2) {
			xVel.add(xHist.get(xHist.size()-1) - xHist.get(xHist.size()-2));
			yVel.add(yHist.get(yHist.size()-1) - yHist.get(yHist.size()-2));
			zVel.add(zHist.get(zHist.size()-1) - zHist.get(zHist.size()-2));
		}
	}

	private float CalcMovAvgAngVel() {
		tempsum=0;
        if (zVel.size()>movavgnum)
        for (int i = 1;i<movavgnum;i++) {
                tempsum = tempsum+zVel.get(zVel.size()-i);
        }
        return tempsum/movavgnum;
  	}

	
	private float CalcAvgAngVel(int range) {
        tempsum=0;
        if (zVel.size()>range)
        for (int i = 1;i<range;i++) {
                tempsum = tempsum+zVel.get(zVel.size()-i);
        }
        return tempsum/range;
	}
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.main);

		mRotationMatrix[ 0] = 1;
		mRotationMatrix[ 4] = 1;
		mRotationMatrix[ 8] = 1;
		mRotationMatrix[12] = 1;

		mTts = new TextToSpeech(this,
				this ); // TextToSpeech.OnInitListener


				//v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		button = (Button) findViewById(R.id.button1);
		button_pressed = 0;
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/*if (tempMagnetic[0] != -1 && tempMagnetic[1] != -1
						&& tempMagnetic[2] != -1 && tempOrientation[0] != -1
						&& tempOrientation[1] != -1 && tempOrientation[2] != -1) {
					SensorManager.getRotationMatrix(initialRotationM, null,
							tempOrientation, tempMagnetic);
					button_pressed = 1;
				}*/
			}
		});

		//		tempMagnetic[0] = tempMagnetic[1] = tempMagnetic[2] = -1;
		//		tempOrientation[0] = tempOrientation[1] = tempOrientation[2] = -1;

		// just some textviews, for data output
		/*outputX = (TextView) findViewById(R.id.textView1);
		outputY = (TextView) findViewById(R.id.textView2);
		outputZ = (TextView) findViewById(R.id.textView3);

		outputX2 = (TextView) findViewById(R.id.textView4);
		outputY2 = (TextView) findViewById(R.id.textView5);
		outputZ2 = (TextView) findViewById(R.id.textView6);

		outputX3 = (TextView) findViewById(R.id.textView7);
		outputY3 = (TextView) findViewById(R.id.textView8);
		outputZ3 = (TextView) findViewById(R.id.textView9);*/
		
		output = (TextView) findViewById(R.id.counter);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));	
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				sensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				sensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
				1000);///update once a milisecond
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				outputX.setText("x:" + Float.toString(event.values[0]));
				outputY.setText("y:" + Float.toString(event.values[1]));
				outputZ.setText("z:" + Float.toString(event.values[2]));
				break;
			case Sensor.TYPE_ORIENTATION:
				//	System.arraycopy(event.values, 0, tempOrientation, 0, 3);
				//	outputX2.setText("x:" + Float.toString(event.values[0]));
				//	outputY2.setText("y:" + Float.toString(event.values[1]));
				//		outputZ2.setText("z:" + Float.toString(event.values[2]));
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				//		System.arraycopy(event.values, 0, tempMagnetic, 0, 3);
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
				SensorManager.getRotationMatrixFromVector(mRotationMatrix , event.values);
				/*
                roll (rotation around z) : atan2(xy, xx)
                pitch (rotation around y) : -arcsin(xz)
                yaw (rotation around x) : atan2(yz,zz)
                [
                 xx0, yx1, zx2, px3;
                 xy4, yy5, zy6, py7;
                 xz8, yz9, zz10, pz11;
                 0, 0, 0, 115
                 ]
				 */
				xangle=(float) Math.atan2(mRotationMatrix[4],mRotationMatrix[0]);
				yangle=(float) -Math.asin(mRotationMatrix[8]);
				zangle=(float) Math.atan2(mRotationMatrix[9],mRotationMatrix[10]);
				outputX2.setText("x:" + Float.toString(xangle));
				outputY2.setText("y:" + Float.toString(yangle));
				outputZ2.setText("z:" + Float.toString(zangle));

				break;
			}

			xHist.add(xangle);
			yHist.add(yangle); 
			zHist.add(zangle);

			ExerciseDetect();
			
			if (movavg>movavgmax){
				movavgmax=movavg;
			}
			if (movavg<movavgmin){
				movavgmin=movavg;
			}				
		}
	}
	

	void ExerciseDetect ()
	{

	rangecounter=rangecounter+1;
	CalcAngVel();
	movavg = CalcMovAvgAngVel();
	
	//Log.d("moving average",Float.toString(movavg));
/*
	if (movavg<upthreshmax && movavg>upthreshmin && updetect==false)
		{
		updetect=true;
		}
	else if (movavg < upthreshmin && updetect==true){
		//v.vibrate(300);
		updetect=false;
		sayHello();
		//outputZ3.setText("Angle Z:" + Float.toString(movavg));
	    }
	else if (movavg>downthreshmax && movavg<downthreshmin && downdetect==false)
		{
		downdetect=true;
		}
	else if (movavg > downthreshmin && downdetect==true){
		//v.vibrate(300);
		sayHello();
		downdetect=false;
		//outputY3.setText("Angle Y:" + Float.toString(movavg));
	    }*/	
	
	if (zangle > .9 && downdetect==true && reset==false && end==false)
		{
		updetect = true;
		downdetect = false;
		if (movavg > upthreshmin && movavg < upthreshmax)	{	
			mTts.speak("And",TextToSpeech.QUEUE_ADD, null);
			}
		else if (movavg < upthreshmin){
			//mTts.speak("Faster",TextToSpeech.QUEUE_ADD, null);
			mTts.speak("And",TextToSpeech.QUEUE_ADD, null);
			}
		else if (movavg > upthreshmax){
			//mTts.speak("Slower",TextToSpeech.QUEUE_ADD, null);
			mTts.speak("And",TextToSpeech.QUEUE_ADD, null);
			}
		}

	else if (zangle < .3 && zangle > -.3 && updetect==true && reset==false && end==false)
		{
		updetect = false;
		downdetect = true;		
		if (movavg < downthreshmin && movavg > downthreshmax)	{	
			mTts.speak(numbers[count],TextToSpeech.QUEUE_ADD, null);
			output.setText(numbers[count]);
			count=count+1;
			}
		else if (movavg > downthreshmin){
			//mTts.speak("Faster",TextToSpeech.QUEUE_ADD, null);
			mTts.speak(numbers[count],TextToSpeech.QUEUE_ADD, null);
			output.setText(numbers[count]);
			count=count+1;
			}
		else if (movavg < downthreshmax){
			//mTts.speak("Slower",TextToSpeech.QUEUE_ADD, null);
			mTts.speak(numbers[count],TextToSpeech.QUEUE_ADD, null);
			output.setText(numbers[count]);
			count=count+1;
			}
		}
	
	else if (zangle < .3 && zangle > -.3 && reset==true && end==false)
		{
		updetect = false;
		downdetect = true;
		if (movavg < upthreshmin && movavg > downthreshmin){
			mTts.speak("Ready",TextToSpeech.QUEUE_ADD, null);
			reset = false;
			}
		}	
	
	
	if (count>4){
		end = true;
		SystemClock.sleep(500);
		mTts.speak("Well Done",TextToSpeech.QUEUE_ADD, null);
		output.setText("Well Done");
		
		SystemClock.sleep(500);
		///////////
		Intent intent = new Intent(this, Graph.class);
        startActivity(intent); 
		///////////
		
		}
	}
	
	
	@Override
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
            //    mAgainButton.setEnabled(true);
                // Greet the user.
           //     sayHello();
            }
        } else {
            // Initialization failed.
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }
	
	
	private static final Random RANDOM = new Random();
	private static final String[] HELLOS = {
		"Hello",
		"Salutations",
		"Greetings",
		"Howdy",
		"What's crack-a-lackin?",
		"That explains the stench!"
	};
}





