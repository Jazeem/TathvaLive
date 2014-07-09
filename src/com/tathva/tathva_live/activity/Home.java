package com.tathva.tathva_live.activity;

import java.io.IOException;

import com.tathva.tathva_live.R;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Home extends ListActivity implements OnClickListener,
		SensorEventListener {
	protected static final String TYPE = null;
	private static final String NAME = null;
	Context context = this;
	private SensorManager sensorManager;
	Button competitions;
	Button exhibitions;
	Button lectures;
	Button nites;
	Button workshops;
	int focus = 1;

	ImageView image;
	View homeLayout;
	DataBaseHelper dbHelper;
	SimpleCursorAdapter mAdapter;
	boolean isCompetitionSelected = false;
	boolean scroll = true;
	TextView heading;
	ImageView comp;
	ImageView exhi;
	ImageView lect;
	ImageView nite;
	ImageView work;
	LinearLayout images;
	AnimationDrawable compAnimation;
	AnimationDrawable workAnimation;
	AnimationDrawable lectAnimation;
	AnimationDrawable exhiAnimation;
	AnimationDrawable niteAnimation;
	int focused = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int scrWidth = getWindowManager().getDefaultDisplay().getWidth();
		int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
		int dp = (int) (scrHeight / Resources.getSystem().getDisplayMetrics().density);
		setContentView(R.layout.homelayout);
		competitions = (Button) findViewById(R.id.button1);
		workshops = (Button) findViewById(R.id.button2);
		exhibitions = (Button) findViewById(R.id.button3);
		lectures = (Button) findViewById(R.id.button4);
		nites = (Button) findViewById(R.id.button5);
		comp = (ImageView) findViewById(R.id.competitions);
		work = (ImageView) findViewById(R.id.workshops);
		exhi = (ImageView) findViewById(R.id.exhibitions);
		lect = (ImageView) findViewById(R.id.lectures);
		nite = (ImageView) findViewById(R.id.nites);

		images = (LinearLayout) findViewById(R.id.images);

		LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
				(int) ((scrHeight * 7) / 50), (int) ((scrHeight * 7) / 50));
		parms.setMargins((int) (scrWidth / 40), (int) ((scrHeight * 1.5) / 50),
				(int) (scrWidth / 40), (int) ((scrHeight * 1.5) / 50));
		competitions.setLayoutParams(parms);
		exhibitions.setLayoutParams(parms);
		workshops.setLayoutParams(parms);
		lectures.setLayoutParams(parms);
		nites.setLayoutParams(parms);

		int padding = (int) scrWidth / 20 + (int) ((scrHeight * 7) / 50);
		LinearLayout.LayoutParams parms1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(int) ((scrHeight * 7) / 50));
		parms1.setMargins(padding, (int) ((scrHeight * 1.5) / 50), 0,
				(int) ((scrHeight * 1.5) / 50));

		comp.setBackgroundResource(R.anim.companimation);
		compAnimation = (AnimationDrawable) comp.getBackground();

		work.setBackgroundResource(R.anim.workanimation);
		workAnimation = (AnimationDrawable) work.getBackground();
		exhi.setBackgroundResource(R.anim.exhianimation);
		exhiAnimation = (AnimationDrawable) exhi.getBackground();
		lect.setBackgroundResource(R.anim.lectanimation);
		lectAnimation = (AnimationDrawable) lect.getBackground();
		nite.setBackgroundResource(R.anim.niteanimation);
		niteAnimation = (AnimationDrawable) nite.getBackground();

		comp.setLayoutParams(parms1);
		exhi.setLayoutParams(parms1);
		work.setLayoutParams(parms1);
		lect.setLayoutParams(parms1);
		nite.setLayoutParams(parms1);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		homeLayout = (LinearLayout) findViewById(R.id.homelayout);

		competitions.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Do stuff here

				if (!scroll && focus == 1) {

					scrollable();
					compAnimation.start();
				} else {
					changeToNormal();
					clearText();
					homeLayout.setBackgroundColor(Color.parseColor("#00AEF0"));
					competitions
							.setBackgroundResource(R.drawable.button_competition_selected);
					scroll = false;
					focus = 1;

					buttonClicked(competitions.getTag().toString());

				}
			}
		});
		workshops.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Do stuff here
				if (!scroll && focus == 2) {

					scrollable();
					workAnimation.start();
				} else {
					changeToNormal();
					clearText();
					homeLayout.setBackgroundColor(Color.parseColor("#A864A9"));
					workshops
							.setBackgroundResource(R.drawable.button_workshop_selected);
					scroll = false;
					focus = 2;

					buttonClicked(workshops.getTag().toString());
				}
			}
		});
		exhibitions.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Do stuff here
				if (!scroll && focus == 3) {

					scrollable();
					exhiAnimation.start();
				} else {
					changeToNormal();
					clearText();
					homeLayout.setBackgroundColor(Color.parseColor("#F36C4F"));
					exhibitions
							.setBackgroundResource(R.drawable.button_exhibition_selected);
					scroll = false;
					focus = 3;

					buttonClicked(exhibitions.getTag().toString());
				}
			}
		});
		lectures.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Do stuff here
				if (!scroll && focus == 4) {

					scrollable();
					lectAnimation.start();
				} else {
					changeToNormal();
					clearText();
					homeLayout.setBackgroundColor(Color.parseColor("#FFF568"));
					lectures.setBackgroundResource(R.drawable.button_lecture_selected);
					scroll = false;
					focus = 4;

					buttonClicked(lectures.getTag().toString());
				}
			}
		});
		nites.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Do stuff here
				if (!scroll && focus == 5) {

					scrollable();
					niteAnimation.start();
				} else {
					changeToNormal();
					clearText();
					homeLayout.setBackgroundColor(Color.parseColor("#3CB878"));
					nites.setBackgroundResource(R.drawable.button_nite_selected);
					scroll = false;
					focus = 5;

					buttonClicked(nites.getTag().toString());
				}
			}
		});

		heading = (TextView) findViewById(R.id.heading);
	}

	private void buttonClicked(String type) {

		LinearLayout lv = (LinearLayout) findViewById(R.id.listview);
		lv.setVisibility(View.VISIBLE);

		heading.setText(type);

		int[] uiBindTo = { android.R.id.text1 };

		dbHelper = new DataBaseHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHelper.openDataBase();
		Cursor cursor;
		if (type.equals("COMPETITIONS")) {
			isCompetitionSelected = true;
			String[] uiBindFrom = { "genre" };
			cursor = dbHelper.getGenres(type);
			mAdapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_1, cursor, uiBindFrom,
					uiBindTo, 0);
			setListAdapter(mAdapter);
		} else {
			isCompetitionSelected = false;
			String[] uiBindFrom = { "name" };
			cursor = dbHelper.getEvents(type);

			/* Empty adapter that is used to display the loaded data */
			mAdapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_1, cursor, uiBindFrom,
					uiBindTo, 0);
			setListAdapter(mAdapter);
			System.out.println("SET LIST ADAPTER DONE");

		}

	}

	private void genreClicked(String genre) {
		LinearLayout lv = (LinearLayout) findViewById(R.id.listview);
		lv.setVisibility(View.VISIBLE);

		isCompetitionSelected = false;

		String[] uiBindFrom = { "name" };
		heading.setText(genre);

		int[] uiBindTo = { android.R.id.text1 };

		dbHelper = new DataBaseHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHelper.openDataBase();
		Cursor cursor = dbHelper.getEventsByGenre(genre);
		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, uiBindFrom,
				uiBindTo, 0);
		setListAdapter(mAdapter);

	}

	private void scrollable() {
		scroll = true;
		homeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

		LinearLayout lv = (LinearLayout) findViewById(R.id.listview);
		lv.setVisibility(View.INVISIBLE);
		// image.setVisibility(View.VISIBLE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			try {
				getAccelerometer(event);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getAccelerometer(SensorEvent event)
			throws InterruptedException {
		float[] values = event.values;
		// Movement
		float y = values[1];
		setFocus(y);
	}

	private void setFocus(float y) throws InterruptedException {
		y *= -1;
		// System.out.println("orientation: "+y);
		if (scroll) {
			if (y > 0 && y <= 5 || y < 0) {

				if (focused != 1) {
					changeToNormal();
					clearText();
					// comp.setVisibility(View.VISIBLE);
					compAnimation.start();
					focused = 1;
					competitions
							.setBackgroundResource(R.drawable.button_competition_selected);
				}
			}

			// requestFocus();

			// homeLayout.setBackgroundColor(Color.parseColor("#00AEF0"));

			else if (y > 5 && y <= 15) {

				if (focused != 2) {
					changeToNormal();
					clearText();
					// work.setVisibility(View.VISIBLE);
					workAnimation.start();
					focused = 2;
					workshops
							.setBackgroundResource(R.drawable.button_workshop_selected);
				}
				// homeLayout.setBackgroundColor(Color.parseColor("#A864A9"));
			} else if (y > 15 && y <= 25) {

				if (focused != 3) {
					changeToNormal();
					clearText();
					// exhi.setVisibility(View.VISIBLE);
					exhiAnimation.start();
					focused = 3;
					exhibitions
							.setBackgroundResource(R.drawable.button_exhibition_selected);
				}
				// homeLayout.setBackgroundColor(Color.parseColor("#F36C4F"));
			} else if (y > 25 && y <= 35) {
				if (focused != 4) {
					changeToNormal();
					clearText();
					// lect.setVisibility(View.VISIBLE);
					lectAnimation.start();
					focused = 4;
					lectures.setBackgroundResource(R.drawable.button_lecture_selected);
				}
				// homeLayout.setBackgroundColor(Color.parseColor("#FFF568"));
			} else if (y > 35) {
				if (focused != 5) {
					changeToNormal();
					clearText();
					// nite.setVisibility(View.VISIBLE);
					niteAnimation.start();
					focused = 5;
					nites.setBackgroundResource(R.drawable.button_nite_selected);
				}
				// homeLayout.setBackgroundColor(Color.parseColor("#3CB878"));
			}
		}
	}

	private void clearText() {

		compAnimation.stop();
		compAnimation.selectDrawable(0);

		workAnimation.stop();
		workAnimation.selectDrawable(0);
		lectAnimation.stop();
		lectAnimation.selectDrawable(0);
		exhiAnimation.stop();
		exhiAnimation.selectDrawable(0);
		niteAnimation.stop();
		niteAnimation.selectDrawable(0);
		/*
		 * comp.setVisibility(View.INVISIBLE);
		 * exhi.setVisibility(View.INVISIBLE);
		 * lect.setVisibility(View.INVISIBLE);
		 * work.setVisibility(View.INVISIBLE);
		 * nite.setVisibility(View.INVISIBLE);
		 */

	}

	private void changeToNormal() {

		competitions
				.setBackgroundResource(R.drawable.button_competition_normal);
		workshops.setBackgroundResource(R.drawable.button_workshop_normal);
		exhibitions.setBackgroundResource(R.drawable.button_exhibition_normal);
		lectures.setBackgroundResource(R.drawable.button_lecture_normal);
		nites.setBackgroundResource(R.drawable.button_nite_normal);
	}

	private void changeFocus(int prev, int cur) {
		if (prev < cur)
			while (prev != cur) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				makeFocus(prev + 1);
				prev++;
			}
		else if (prev > cur)
			while (prev != cur) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				makeFocus(prev - 1);
				prev--;
			}
		focus = cur;
	}

	private void makeFocus(int f) {
		switch (f) {
		case 1:
			competitions.requestFocus();
			break;
		case 2:
			workshops.requestFocus();
			break;
		case 3:
			exhibitions.requestFocus();
			break;
		case 4:
			lectures.requestFocus();
			break;
		case 5:
			nites.requestFocus();
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent nextScreen = new Intent(Home.this, Event.class);
		String selection = ((TextView) v).getText().toString();
		if (isCompetitionSelected)
			genreClicked(selection);
		else {
			nextScreen.putExtra(NAME, selection);
			// starting new activity
			startActivity(nextScreen);
		}
	}

}