package me.wingspan.rebound.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Home extends ListActivity {
	private String[] exercises = { "Shoulder", "Leg" };
	private ArrayAdapter<String> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.home);
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exercises);
		setListAdapter(listAdapter);
		
		
		/*
		Hgraph.addActivity("Shoulder", 5);
		Hgraph.addActivity("Shoulder", 10);
		ReboundmeActivity.info("Added activities");
		*/
	}

	/** Handles clicks on exercises */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (position == 0) {
			Intent exercise = new Intent(this, ExerciseInstructions.class);
			startActivity(exercise);
		}
	}
}