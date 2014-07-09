package com.tathva.tathva_live.activity;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tathva.tathva_live.R;
import com.tathva.tathva_live.data.ARData;
import com.tathva.tathva_live.data.LocalDataSource;
import com.tathva.tathva_live.data.NetworkDataSource;
import com.tathva.tathva_live.ui.Marker;
import com.tathva.tathva_live.widget.VerticalTextView;

/**
 * This class extends the AugmentedReality and is designed to be an example on
 * how to extends the AugmentedReality class to show multiple data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */

public class Demo extends AugmentedReality {

	private static final String TAG = "Demo";
	private static final String locale = Locale.getDefault().getLanguage();
	private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
			1);
	private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(
			1, 1, 20, TimeUnit.SECONDS, queue);
	private static final Map<String, NetworkDataSource> sources = new ConcurrentHashMap<String, NetworkDataSource>();

	private static Toast myToast = null;
	private static VerticalTextView text = null;
	private static AlertDialog alertDialog;
	private static AlertDialog.Builder alertDialogBuilder;
	
	private static String building;
	private static String room;
	private static ProgressDialog pdialog;
	private static AnimationDrawable progressAnimation;
	private static Dialog dialog;
	private static View connectingDialog;
	private static View youAreNotAtNitc;
	private static View youHaveReached;
	static LayoutParams augLayout;
	static TextView reachedMessage;
	static TextView notAtMessage;
	static boolean dialogShown=false;
	static ImageView buildingImage;

	private static AnimationDrawable gpsAnimation;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create toast
		myToast = new Toast(getApplicationContext());
		myToast.setGravity(Gravity.CENTER, 0, 0);
		// Creating our custom text view, and setting text/rotation
		text = new VerticalTextView(getApplicationContext());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		text.setLayoutParams(params);
		text.setBackgroundResource(android.R.drawable.toast_frame);
		text.setTextAppearance(getApplicationContext(),
				android.R.style.TextAppearance_Small);
		text.setShadowLayer(2.75f, 0f, 0f, Color.parseColor("#BB000000"));
		myToast.setView(text);
		// Setting duration and displaying the toast
		myToast.setDuration(Toast.LENGTH_SHORT);

		// Local
		LocalDataSource localData = new LocalDataSource(this.getResources(),
				getApplicationContext(),"agam");

		Intent intent = getIntent();
		building = intent.getStringExtra(Event.BUILDING);
		System.out.println("INTENT DONE ");
		Bitmap up = BitmapFactory.decodeResource(getResources(), R.drawable.up);
		Bitmap down = BitmapFactory.decodeResource(getResources(),
				R.drawable.down);
		Bitmap right = BitmapFactory.decodeResource(getResources(),
				R.drawable.right);
		Bitmap left = BitmapFactory.decodeResource(getResources(),
				R.drawable.left);

		ARData.setBitmap(up, down, right, left);
		ARData.addMarkers(localData.getMarkerDB(building));
		
		int buildingNo=localData.getBuildingNo(building);

		connectingDialog = getLayoutInflater()
				.inflate(R.layout.alertview, null);
		youAreNotAtNitc = getLayoutInflater().inflate(R.layout.notatnit2, null);
		youHaveReached = getLayoutInflater()
				.inflate(R.layout.gpsreached2, null);
		augLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		
		
		addContentView(connectingDialog, augLayout);
		addContentView(youAreNotAtNitc, augLayout);
		addContentView(youHaveReached,augLayout);
		youAreNotAtNitc.setVisibility(View.INVISIBLE);
		connectingDialog.setVisibility(View.INVISIBLE);
		youHaveReached.setVisibility(View.INVISIBLE);
		ImageView gpsImage = (ImageView) findViewById(R.id.gpsImage);
		gpsImage.setBackgroundResource(R.drawable.gpsanimation);

		reachedMessage=(TextView) findViewById(R.id.reachedmessage);
		//notAtMessage=(TextView) findViewById(R.id.notatmessage);
		buildingImage=(ImageView) findViewById(R.id.reachedimage);
		String generatedString="building_";
		generatedString=generatedString+Integer.toString(buildingNo);
		
		System.out.println("Image Name= "+generatedString);
		Resources res = getResources();
		int resourceId = res.getIdentifier(
		   generatedString, "drawable", getPackageName() );
		buildingImage.setImageResource( resourceId );

		
		gpsAnimation = (AnimationDrawable) gpsImage.getBackground();

		pdialog = new ProgressDialog(this);

		pdialog.setCancelable(true);
		pdialog.setTitle("GPS Accuracy Low");
		pdialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				finish();
				
			}
		});
	

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart() {

		super.onStart();

		Location last = ARData.getCurrentLocation();
		updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
	}

	public static void showAlert(float accuracy) {
		// show it

		if (accuracy == 0) {

			if ((ARData.getCurrentLocation() == ARData.hardFix)) 
			{
				pdialog.hide();
				dialogShown=false;
				connectingDialog.setVisibility(View.VISIBLE);
				youAreNotAtNitc.setVisibility(View.INVISIBLE);
				youHaveReached.setVisibility(View.INVISIBLE);
			}

		} else if (accuracy == -20) {

			youHaveReached.setVisibility(View.VISIBLE);
			//reachedMessage.setText("You have Reached "+building+" ");
			dialogShown=false;
			pdialog.hide();
			connectingDialog.setVisibility(View.INVISIBLE);
			youAreNotAtNitc.setVisibility(View.INVISIBLE);

		} else {

			youAreNotAtNitc.setVisibility(View.INVISIBLE);
			youHaveReached.setVisibility(View.INVISIBLE);
			connectingDialog.setVisibility(View.INVISIBLE);
			pdialog.setMessage("GPS Accuracy is low " + accuracy + " m");
			dialogShown=true;
			pdialog.show();

		}
		// alertDialog.show();
		// pdialog.show();

	}

	public static void notAtNit(float distance)

	{
		if ((ARData.getCurrentLocation() == ARData.hardFix))
		{
			pdialog.hide();
			dialogShown=false;
			connectingDialog.setVisibility(View.VISIBLE);
			youAreNotAtNitc.setVisibility(View.INVISIBLE);
			youHaveReached.setVisibility(View.INVISIBLE);
		}
		else
		{
		pdialog.hide();
		dialogShown=false;
		connectingDialog.setVisibility(View.INVISIBLE);
		int d = (int) distance / 1000;
		youAreNotAtNitc.setVisibility(View.VISIBLE);
		youHaveReached.setVisibility(View.INVISIBLE);
	
		//notAtMessage.setText("You are "+Integer.toString(d)+" Km Away from NITC !");
		
		}

	}

	public static void hideAlert() {
		connectingDialog.setVisibility(View.INVISIBLE);
		pdialog.hide();
		dialogShown=false;
		youAreNotAtNitc.setVisibility(View.INVISIBLE);
		youHaveReached.setVisibility(View.INVISIBLE);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected() item=" + item);
		switch (item.getItemId()) {
		case R.id.exit:
			finish();
			break;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);

		updateData(location.getLatitude(), location.getLongitude(),
				location.getAltitude());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void markerTouched(Marker marker) {
		text.setText(marker.getName());
		myToast.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateDataOnZoom() {
		super.updateDataOnZoom();
		Location last = ARData.getCurrentLocation();
		updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
	}

	private void updateData(final double lat, final double lon, final double alt) {
		try {
			exeService.execute(new Runnable() {

				@Override
				public void run() {
					for (NetworkDataSource source : sources.values())
						download(source, lat, lon, alt);
				}
			});
		} catch (RejectedExecutionException rej) {
			Log.w(TAG, "Not running new download Runnable, queue is full.");
		} catch (Exception e) {
			Log.e(TAG, "Exception running download Runnable.", e);
		}
	}

	private static boolean download(NetworkDataSource source, double lat,
			double lon, double alt) {
		if (source == null)
			return false;

		String url = null;
		try {
			url = source.createRequestURL(lat, lon, alt, ARData.getRadius(),
					locale);
		} catch (NullPointerException e) {
			return false;
		}

		List<Marker> markers = null;
		try {
			markers = source.parse(url);
		} catch (NullPointerException e) {
			return false;
		}

		ARData.addMarkers(markers);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		super.onBackPressed();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus)
			gpsAnimation.start();
	}
}