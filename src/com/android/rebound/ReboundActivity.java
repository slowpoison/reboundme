package com.android.rebound;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReboundActivity extends Activity implements SensorEventListener {
	SensorManager sensorManager = null;
	private float[] initialRotationM = new float[9];
	private float[] currentRotationM = new float[9];
	private float[] tempOrientation = new float[3];
	private float[] tempMagnetic = new float[3];

	private float[] angle = new float[3];
	private int button_pressed;
	
	private Vibrator v;
	
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
	
	
	float movavgnum=100;
	float movavg=0;
	float tempsum=0;
	float upthreshmax = (float) 0.05;
	float upthreshmin = (float) 0.01;
	float downthreshmax = (float) -0.05;
	float downthreshmin = (float) -0.01;

	boolean updetect=true;
	boolean downdetect=false;


	ArrayList<Float> xHist = new ArrayList<Float>(); 
	ArrayList<Float> yHist = new ArrayList<Float>();
	ArrayList<Float> zHist = new ArrayList<Float>();

	ArrayList<Float> xVel = new ArrayList<Float>(); 
	ArrayList<Float> yVel = new ArrayList<Float>();
	ArrayList<Float> zVel = new ArrayList<Float>();

	
	
	@SuppressWarnings("unused")
	private void trackMovement(double ang) {
		if((ang >= 165 && ang < 180) || (ang >= -180 && ang < -165))  {
			v.vibrate(100);
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
        if (xHist.size()>movavgnum)
        for (int i = 1;i<movavgnum;i++) {
                tempsum = tempsum+zVel.get(zVel.size()-i);
        }
        return tempsum/movavgnum;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.main);
		
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		button = (Button) findViewById(R.id.button1);
		button_pressed = 0;
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tempMagnetic[0] != -1 && tempMagnetic[1] != -1
						&& tempMagnetic[2] != -1 && tempOrientation[0] != -1
						&& tempOrientation[1] != -1 && tempOrientation[2] != -1) {
					SensorManager.getRotationMatrix(initialRotationM, null,
							tempOrientation, tempMagnetic);
					button_pressed = 1;
				}
			}
		});

		tempMagnetic[0] = tempMagnetic[1] = tempMagnetic[2] = -1;
		tempOrientation[0] = tempOrientation[1] = tempOrientation[2] = -1;

		// just some textviews, for data output
		outputX = (TextView) findViewById(R.id.textView1);
		outputY = (TextView) findViewById(R.id.textView2);
		outputZ = (TextView) findViewById(R.id.textView3);

		outputX2 = (TextView) findViewById(R.id.textView4);
		outputY2 = (TextView) findViewById(R.id.textView5);
		outputZ2 = (TextView) findViewById(R.id.textView6);

		outputX3 = (TextView) findViewById(R.id.textView7);
		outputY3 = (TextView) findViewById(R.id.textView8);
		outputZ3 = (TextView) findViewById(R.id.textView9);
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
				System.arraycopy(event.values, 0, tempOrientation, 0, 3);
				outputX2.setText("x:" + Float.toString(event.values[0]));
				outputY2.setText("y:" + Float.toString(event.values[1]));
				outputZ2.setText("z:" + Float.toString(event.values[2]));
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				System.arraycopy(event.values, 0, tempMagnetic, 0, 3);
				break;
			}

			if (tempMagnetic[0] != -1 && tempMagnetic[1] != -1
					&& tempMagnetic[2] != -1 && tempOrientation[0] != -1
					&& tempOrientation[1] != -1 && tempOrientation[2] != -1 && button_pressed == 1) {
				

				SensorManager.getRotationMatrix(currentRotationM, null,
						tempOrientation, tempMagnetic);

				SensorManager.getAngleChange(angle, initialRotationM,
						currentRotationM);
				
				//trackMovement((angle[2] * 180) / Math.PI);
				
				xHist.add(angle[0]);
				yHist.add(angle[1]); 
				zHist.add(angle[2]);

				outputX3.setText("Angle X:" + Float.toString((float) ((angle[0] * 180) / Math.PI)));
				outputY3.setText("Angle Y:" + Float.toString((float) ((angle[1] * 180) / Math.PI)));
				outputZ3.setText("Angle Z:" + Float.toString((float) ((angle[2] * 180) / Math.PI)));
			
			
				ExerciseDetect();

			}

			
			
		}
	}

	void ExerciseDetect ()
	{

	CalcAngVel();
	movavg = CalcMovAvgAngVel();
	Log.d("moving average",Float.toString(movavg));

	if (movavg<upthreshmax && movavg>upthreshmin)
	{
	updetect=true;
	}
	else if (movavg < upthreshmin && updetect==true){
	v.vibrate(300);
	updetect=false;
	    }

	if (movavg>downthreshmax && movavg<downthreshmin)
	{
	downdetect=true;
	}
	else if (movavg > downthreshmin && updetect==true){
	v.vibrate(300);
	downdetect=false;
	    }


	}

}