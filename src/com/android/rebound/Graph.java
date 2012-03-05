package com.android.rebound;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;

import com.androidplot.Plot;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

public class Graph extends Activity
{
 
    private XYPlot mySimpleXYPlot;
    private XYPlot ExerXYPlot;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
 
        mySimpleXYPlot = (XYPlot) findViewById(R.id.myRangeOfMotionPlot);
        //Number[] numSightings = {50, 80, 90, 20, 50};
        Number[] expectedROM = {32, 30, 32, 35, 38,
        					40, 42, 45, 48, 51,
        					55, 60, 65, 70, 75,
        					80, 88, 96, 105, 115,
        					125, 135, 145, 153, 160,
        					165, 175, 180, 180, 180};
        
        Number[] ActualROM = {35, 30, 35, 33, 35,
        					37, 45, 50, 0, 0,
        					0, 0, 0, 0, 0,
        					0, 0, 0, 0, 0,
        					0, 0, 0, 0, 0,
        					0, 0, 0, 0, 0};
        
        Number startdate = 1325894400000L;        
        Number[] timestamp = {0, 0, 0, 0, 0,
        			0, 0, 0, 0, 0,
        			0, 0, 0, 0, 0,
        			0, 0, 0, 0, 0,
        			0, 0, 0, 0, 0,
        			0, 0, 0, 0, 0};
        for (int i = 0; i < expectedROM.length; i++)
        	{
        	timestamp[i] = startdate.longValue() + (86400000L * i);
        	}
        
        ExerXYPlot = (XYPlot) findViewById(R.id.myExercises);

        
       
        // create our series from our array of nums:
        XYSeries expected = new SimpleXYSeries(
                Arrays.asList(timestamp),
                Arrays.asList(expectedROM),
                "Expected");

        XYSeries Actual = new SimpleXYSeries(
                Arrays.asList(timestamp),
                Arrays.asList(ActualROM),
                "Actual");
        
        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1,1}, 1));
        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
 
        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        mySimpleXYPlot.getBorderPaint().setStrokeWidth(1);
        mySimpleXYPlot.getBorderPaint().setAntiAlias(false);
        mySimpleXYPlot.getBorderPaint().setColor(Color.WHITE);
 
        
        
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series2Format = new LineAndPointFormatter(
                Color.rgb(0, 100, 0),                   // line color
                Color.rgb(0, 100, 0),                   // point color
                Color.argb(255, 0, 0, 0));                // fill color
 
 
        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE, Color.GREEN, Shader.TileMode.MIRROR));
 
        LineAndPointFormatter formatter  = new LineAndPointFormatter(Color.rgb(0, 0,0), Color.GREEN, Color.BLACK);
        mySimpleXYPlot.getGraphWidget().setPaddingRight(2);
        mySimpleXYPlot.addSeries(expected, series2Format);

        formatter.setFillPaint(lineFill);
        mySimpleXYPlot.addSeries(Actual, formatter);
        
        
        // draw a domain tick for each year:
        mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, timestamp.length/5);
        mySimpleXYPlot.setRangeStep(XYStepMode.SUBDIVIDE, 11);
        
        mySimpleXYPlot.setRangeBoundaries(0, 200, BoundaryMode.FIXED);
        
        // customize our domain/range labels
        mySimpleXYPlot.setDomainLabel("Day");
        mySimpleXYPlot.setRangeLabel("Range of Motion");
        
        // get rid of decimal points in our range labels:
        mySimpleXYPlot.setRangeValueFormat(new DecimalFormat("0"));
 
        mySimpleXYPlot.setDomainValueFormat(new MyDateFormat());
 
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        mySimpleXYPlot.disableAllMarkup();
    //////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        Number[] series3Numbers = {1, 1, 1, 1, 1,
    			1, 1, 1, 0, 0,
    			0, 0, 0, 0, 0,
    			0, 0, 0, 0, 0,
    			0, 0, 0, 0, 0,
    			0, 0, 0, 0, 0};
        
        
        // create our series from our array of nums:
        XYSeries series3 = new SimpleXYSeries(
        		Arrays.asList(timestamp),
                Arrays.asList(series3Numbers),
                "Days Exercised");
 
        ExerXYPlot.setDomainValueFormat(new MyDateFormat());
        
        ExerXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        ExerXYPlot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
        ExerXYPlot.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1,1}, 1));
        ExerXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        ExerXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        ExerXYPlot.getGraphWidget().setMarginRight(5);
 
        ExerXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        ExerXYPlot.getBorderPaint().setStrokeWidth(1);
        ExerXYPlot.getBorderPaint().setAntiAlias(false);
        ExerXYPlot.getBorderPaint().setColor(Color.WHITE);
 
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series3Format = new LineAndPointFormatter(
                Color.rgb(0, 0, 100),                   // line color
                Color.rgb(0, 0, 0),                   // point color
                Color.argb(255, 255, 0, 0 ));                // fill color
 
 
        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill3 = new Paint();
        lineFill3.setAlpha(240);
        lineFill3.setShader(new LinearGradient(0, 0, 0, 250, Color.GREEN, Color.WHITE, Shader.TileMode.MIRROR));
 
        StepFormatter stepFormatter3  = new StepFormatter(Color.rgb(0, 0, 0), Color.RED);
        stepFormatter3.getLinePaint().setStrokeWidth(1);
 
        
        stepFormatter3.getLinePaint().setAntiAlias(false);
        stepFormatter3.setFillPaint(lineFill3);
        ExerXYPlot.addSeries(series3, stepFormatter3);
 
        // adjust the domain/range ticks to make more sense; label per tick for range and label per 5 ticks domain:
        ExerXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
        //ExerXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
        ExerXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, timestamp.length/5);
        
        //ExerXYPlot.setTicksPerRangeLabel(1);
        ExerXYPlot.setTicksPerDomainLabel(timestamp.length);
 
        // customize our domain/range labels
        //ExerXYPlot.setDomainLabel("Days");
        //ExerXYPlot.setRangeLabel("Checked!");
 
 
        // get rid of decimal points in our domain labels:
        //ExerXYPlot.setDomainValueFormat(new DecimalFormat("0"));
 
        // create a custom formatter to draw our state names as range tick labels:
        //ExerXYPlot.setRangeValueFormat(
 
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        ExerXYPlot.disableAllMarkup();
    
    
    
    
    
    }
 
    private class MyDateFormat extends Format {
 
            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.
            private SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd");
 
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                long timestamp = ((Number) obj).longValue();
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }
            
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
 
            }
    }
}