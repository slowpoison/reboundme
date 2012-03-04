package com.android.rebound;

import java.util.LinkedList;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

    
public class Graph extends Activity {
    

	LinkedList<Number> series1Numbers;
	LinkedList<Number> series2Numbers;
	
    private XYPlot mySimpleXYPlot;
    private SimpleXYSeries series1;
    private SimpleXYSeries series2;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
          
            
   //     TextToSpeech tts = new TextToSpeech(this, this);
   //     tts.setLanguage(Locale.US);
   //     tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);
    }

    
    
    
    private void GraphValues()
    {
 
    series1Numbers=new LinkedList<Number>();
    series2Numbers=new LinkedList<Number>();
    	
        // Turn the above arrays into XYSeries:
    series1 = new SimpleXYSeries(series1Numbers,          // SimpleXYSeries takes a List so turn our array into a List
               SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Angle");                             // Set the display title of the series
        // Same as above, for series2
//        series2 = new SimpleXYSeries(series2Numbers, 
//        		SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, 
//                "Series2");
 
    mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
    mySimpleXYPlot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);

        
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // line color
                Color.rgb(0, 100, 0),                // point color
                Color.rgb(100, 190, 100));              // fill color (optional)
 
        // Add series1 to the xyplot:
        mySimpleXYPlot.addSeries(series1, series1Format);
 
        mySimpleXYPlot.setRangeBoundaries(0, 180, BoundaryMode.FIXED);
        
        // Same as above, with series2:
//        mySimpleXYPlot.addSeries(series2, new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100),
//                Color.rgb(150, 150, 190)));
 
 
        // Reduce the number of range labels
        mySimpleXYPlot.setTicksPerRangeLabel(10);
 
        // By default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        mySimpleXYPlot.disableAllMarkup();
    }

    /*
    void UpdateGraphValues()
    {
    	//adjust angle
    	
    	series1Numbers.addLast(newangle);
    	//series2Numbers.addLast(Math.round(Math.random()*5));

    	if (series1Numbers.size() > NUM_GRAPH)
    		{
    		series1Numbers.removeFirst();
    		//series2Numbers.removeFirst();
    		}
    	
    	series1.setModel(series1Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    	//series2.setModel(series2Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    	
    	mySimpleXYPlot.redraw();
   */ 	
    
}    
    
    



