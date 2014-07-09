package com.tathva.tathva_live.activity;

import com.tathva.tathva_live.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {

	Button events;
	Button clueless;
	Button game;
	Button spotsatnitc;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlayout);
		events = (Button) findViewById(R.id.button1);
		events.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(Main.this, Home.class));

			}
		});

		spotsatnitc = (Button) findViewById(R.id.button4);

		spotsatnitc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(Main.this, SpotsList.class));
			}
		});

		game = (Button) findViewById(R.id.button3);

		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(Main.this, NewsFeed.class));
			}
		});

		clueless = (Button) findViewById(R.id.button2);

		clueless.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!networkCheckIn()) {
					Toast.makeText(getApplicationContext(),
							"No Internet Access, Review network settings !",
							Toast.LENGTH_LONG).show();
				} else {
					startActivity(new Intent(Main.this, Clueless.class));
				}
			}
		});

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.develepors:
			startActivity(new Intent(Main.this, Develepors.class));
			break;
		
		}
		return true;
	}
	public boolean networkCheckIn() {
		try {

			ConnectivityManager networkInfo = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			networkInfo.getActiveNetworkInfo().isConnectedOrConnecting();

			Log.d("1", "Net avail:"
					+ networkInfo.getActiveNetworkInfo()
							.isConnectedOrConnecting());

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				// Log.d("2", "Network available:true");
				return true;

			} else {
				// Log.d("3", "Network available:false");
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}
}