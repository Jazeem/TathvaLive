package com.tathva.tathva_live.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tathva.tathva_live.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class Event extends Activity {
	protected static final String BUILDING = null;
	SharedPreferences preferences;
	DataBaseHelper dbHelper;
	TextView header;
	String name;

	TextView description;
	Button results;
	TextView time;
	TextView location;
	Button update;

	Button findthisevent;
	String building;

	HttpClient httpClient;
	HttpPost httpPost;
	ArrayList nameValuePairs;
	int isRegistered;
	HttpResponse response;
	Button call;
	String result;
	String phn;
	String contactName;
	ImageView image;
	TextView tabText1;
	TextView tabText2;
	TextView tabText3;
	TabHost tabHost;
	String day1s;
	String day2s;
	String day3s;
	TextView statusView;
	Integer status;
	TextView lastUpdated;
	Boolean updating = false;
	Boolean isDone = false;
	TextView fullDescription;
	Button day1;
	Button day2;
	Button day3;
	TextView tabText;
	String type;
	String code;
	int focus;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		name = intent.getStringExtra(EventsList.NAME);
		setContentView(R.layout.event);

		header = (TextView) findViewById(R.id.header);
		header.setText(name);
		dbHelper = new DataBaseHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHelper.openDataBase();

		Cursor cursor = dbHelper.getEventDetails(name);

		System.out.println(name);

		cursor.moveToFirst();
		if (cursor.getCount() <= 0) {
			System.out.println("CURSOR EMPTY");
		}

		day1s = cursor.getString(cursor.getColumnIndex("day1"));
		if (day1s == null)
			day1s = "null";
		day2s = cursor.getString(cursor.getColumnIndex("day2"));
		if (day2s == null)
			day2s = "null";

		day3s = cursor.getString(cursor.getColumnIndex("day3"));
		if (day3s == null)
			day3s = "null";

		status = cursor.getInt(cursor.getColumnIndex("status"));

		// description = (TextView) findViewById(R.id.description);

		// description.setText(cursor.getString(cursor
		// .getColumnIndex("description")));

		// fullDescription=(TextView)findViewById(R.id.fulldescription);
		// fullDescription.setText(cursor.getString(cursor.getColumnIndex("fulldescription")));

		type = cursor.getString(cursor.getColumnIndex("type"));
		
		
		 code = cursor.getString(cursor.getColumnIndex("code"));
		String	codeLower = code.toLowerCase();

		results = (Button) findViewById(R.id.results);
		results.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Event.this);

				// set title

				dbHelper.openDataBase();
				Cursor cur = dbHelper.getResult(code);
				cur.moveToFirst();
				String dbResult = cur.getString(cur.getColumnIndex("results"));

				if (dbResult == null)
					dbResult = "null";

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity

								dialog.cancel();
							}
						});

				if (type.equals("COMPETITIONS")) {

					if (dbResult.equals("null")) {
						alertDialogBuilder.setTitle(" No Results");
						alertDialogBuilder
								.setMessage("Results will be updated when the event is over");

					} else {
						alertDialogBuilder.setTitle("Results");
						alertDialogBuilder.setMessage(dbResult);

					}
				}

				else{
					alertDialogBuilder.setTitle("No Results");
					alertDialogBuilder.setMessage("This event is not a competition !");
					
				}
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}
		});

		// image = (ImageView) findViewById(R.id.image);
		// String imageName = cursor.getString(cursor.getColumnIndex("image"));
		// imageName = removeExtension(imageName);
		

		// image.setImageResource(b);

		WebView webView = (WebView) findViewById(R.id.webview);
		String summary = "";
		// webView.loadData(summary, "text/html", null);
		webView.loadUrl("file:///android_asset/"+codeLower+"/"+ codeLower+".html");

		location = (TextView) findViewById(R.id.location);
		location.setTextColor(Color.RED);

		building = cursor.getString(cursor.getColumnIndex("building"));
		if (building == null)
			building = "null";

		String room = cursor.getString(cursor.getColumnIndex("room"));
		if (room == null)
			room = "null";

		if (room.equals("null"))

			location.setText(building);

		else
			location.setText(building + " , " + room);

		if ((room.equals("null")) && (building.equals("null")))
			location.setText("Not Available ");

		tabText1 = (TextView) findViewById(R.id.tabtext1);
		tabText2 = (TextView) findViewById(R.id.tabtext2);
		tabText3 = (TextView) findViewById(R.id.tabtext3);
		day1 = (Button) findViewById(R.id.day1);
		day2 = (Button) findViewById(R.id.day2);
		day3 = (Button) findViewById(R.id.day3);

		try {
			timeManager();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		day1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setFocus(1);

			}
		});

		day2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setFocus(2);

			}
		});

		day3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setFocus(3);

			}
		});

		phn = cursor.getString(cursor.getColumnIndex("contactnumber"));
		phn = "+91" + phn;
		contactName = cursor.getString(cursor.getColumnIndex("contactname"));

		update = (Button) findViewById(R.id.update);
		System.out.println("BEFORE");

		update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("Calling FUNCTION");

				if (!networkCheckIn()) {
					Toast.makeText(getApplicationContext(),
							"No Internet Access, Review network settings !",
							Toast.LENGTH_LONG).show();
				}

				else {
					UpdateTimeAndLocation task = new UpdateTimeAndLocation();
					task.execute();

					update.setClickable(false);
					update.setText("Updating..");
					findthisevent.setClickable(false);
					findthisevent.setText("Updating location ... ");

				}

			}
		});

		lastUpdated = (TextView) findViewById(R.id.lastupdated);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String ls = preferences.getString("lastUpdated", "not updated");
		
		
		
		if (ls.equals("not updated"))
			lastUpdated.setText("Not updated yet ");
		else
		{
		
		
			lastUpdated.setText("Last Updated at " + ls);// .substring(0,ls.indexOf("G")-1));
		}
		findthisevent = (Button) findViewById(R.id.findthisevent);
		findthisevent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				if (!locationMgr
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					buildAlertMessageNoGps();
				} else {

					if (building.equals("null")) {
						buildAlertMessageNoLocation();

					}

					else {
						Intent nextScreen = new Intent(getApplicationContext(),
								Demo.class);

						nextScreen.putExtra(BUILDING, building);

						startActivity(nextScreen);
					}
				}
			}
		});

		call = (Button) findViewById(R.id.call);
		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callEventManager(phn, contactName);

			}
		});

		try {
			timeManager();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setFocus(int f) {
		if (f == 1) {
			day1.setBackgroundColor(Color.BLACK);
			day1.setTextColor(Color.WHITE);
			day2.setTextColor(Color.BLACK);
			day3.setTextColor(Color.BLACK);
			day2.setBackgroundResource(R.drawable.border);
			day3.setBackgroundResource(R.drawable.border);

			tabText1.setVisibility(View.VISIBLE);
			tabText2.setVisibility(View.INVISIBLE);
			tabText3.setVisibility(View.INVISIBLE);
			tabText1.setTextColor(Color.WHITE);
			tabText2.setTextColor(Color.BLACK);
			tabText3.setTextColor(Color.BLACK);
			tabText1.setBackgroundColor(Color.BLACK);
			tabText2.setBackgroundColor(Color.WHITE);
			tabText3.setBackgroundColor(Color.WHITE);

		} else if (f == 2) {
			day2.setBackgroundColor(Color.BLACK);
			day2.setTextColor(Color.WHITE);
			day1.setTextColor(Color.BLACK);
			day3.setTextColor(Color.BLACK);
			day1.setBackgroundResource(R.drawable.border);
			day3.setBackgroundResource(R.drawable.border);
			tabText2.setVisibility(View.VISIBLE);
			tabText1.setVisibility(View.INVISIBLE);
			tabText3.setVisibility(View.INVISIBLE);

			tabText2.setTextColor(Color.WHITE);
			tabText1.setTextColor(Color.BLACK);
			tabText3.setTextColor(Color.BLACK);
			tabText2.setBackgroundColor(Color.BLACK);
			tabText1.setBackgroundColor(Color.WHITE);
			tabText3.setBackgroundColor(Color.WHITE);
		}

		else if (f == 3) {
			day3.setBackgroundColor(Color.BLACK);
			day3.setTextColor(Color.WHITE);
			day2.setTextColor(Color.BLACK);
			day1.setTextColor(Color.BLACK);
			day2.setBackgroundResource(R.drawable.border);
			day1.setBackgroundResource(R.drawable.border);
			tabText3.setVisibility(View.VISIBLE);
			tabText2.setVisibility(View.INVISIBLE);
			tabText1.setVisibility(View.INVISIBLE);

			tabText3.setTextColor(Color.WHITE);
			tabText1.setTextColor(Color.BLACK);
			tabText2.setTextColor(Color.BLACK);
			tabText3.setBackgroundColor(Color.BLACK);
			tabText1.setBackgroundColor(Color.WHITE);
			tabText2.setBackgroundColor(Color.WHITE);
		}
	}

	private void timeManager() throws ParseException {

		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date; // your date
		Calendar cal = Calendar.getInstance();
		date = (Date) cal.getTime();

		int day = date.getDate();
		int year = cal.get(Calendar.YEAR);
		int month = date.getMonth();

		System.out.println("today=" + day + "/" + month + "/" + year);

		if ((day == 18) && (year == 2013) && (month == 9))
			setFocus(1);
		else if ((day == 19) && (year == 2013) && (month == 9)) {

			setFocus(2);
		} else if ((day == 20) && (year == 2013) && (month == 9))
			setFocus(3);
		else
			setFocus(1);

		String text = null;
		Date d1 = null;
		Date d2 = null;
		Date d3 = null;

		int count = 0;
		boolean one = false;
		boolean two = false;
		boolean three = false;

		if (!day1s.equals("null")) {
			one = true;
			count++;

			d1 = formatter.parse(day1s);

			tabText1.setText(createTabText(d1));

		}

		if (!day2s.equals("null")) {
			two = true;
			count++;

			d2 = formatter.parse(day2s);
			tabText2.setText(createTabText(d2));
		}

		if (!day3s.equals("null")) {
			three = true;
			count++;
			d3 = formatter.parse(day3s);

			tabText3.setText(createTabText(d3));
		}

		statusView = (TextView) findViewById(R.id.statusview);
		statusView.setTextColor(Color.RED);

		if (count == 0) {

			tabText1.setText("Schedule is not finalised ");
			tabText2.setText("Schedule is not finalised ");
			tabText3.setText("Schedule is not finalised ");

		}

		else {

			text = "This event is only on ";
			if (count == 1) {
				if (one)
					text = text + "Day 1";
				else if (two)
					text = text + "Day 2";
				else
					text = text + "Day 3";

			} else if (count == 2) {

				if (one && two)
					text = text + "Day 1 and Day 2";
				else if (one && three)
					text = text + "Day 1 and Day 3";
				else
					text = text + "Day 2 and Day 3";

			}

			if (!one)
				tabText1.setText(text);
			if (!two)
				tabText2.setText(text);
			if (!three)
				tabText3.setText(text);

		}

		if (status == 0)
			statusView.setText("Event is yet to Start ");
		else if (status == 1)
			statusView.setText("Event is Currently Progressing");
		else if (status == 2) {
			String t;
			if (!two)
				t = "Day 3";
			else if (date.before(d2))
				t = "Day 2";
			else
				t = "Day 3";

			statusView.setText("Event will Resume on " + t + " ");
		} else if (status == 3)
			statusView.setText("This event is Over !");
		else if (status == 4)
			statusView.setText("This event is Cancelled ");

		if (count == 0)
			statusView.setText("Not Available");
	}
	
	

	private void buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private void buildAlertMessageNoLocation() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Location Not Available!");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Location for this event is not available  , Try updating ")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity

						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public void callEventManager(final String phn, String contactName) {

		System.out.println(contactName + "-" + phn);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Call Event Manager");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Are You sure you want to call " + contactName + " ? ")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity

								Intent callIntent = new Intent(
										Intent.ACTION_CALL);
								callIntent.setData(Uri.parse("tel:" + phn));
								startActivity(callIntent);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private String createTabText(Date x) {
		int hours = x.getHours();
		int minutes = x.getMinutes();
		String t1;
		hours = hours % 12;
		if (hours == 0)
			hours = 12;
		String mins;
		mins = Integer.toString(minutes);

		if (minutes < 10)
			mins = "0" + mins;

		t1 = "Scheduled  Time " + hours + ":" + mins;

		if (x.getHours() >= 12)
			t1 = t1 + " PM";
		else
			t1 = t1 + " AM";
		return t1;
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

	public static String removeExtension(String s) {

		String separator = System.getProperty("file.separator");
		String filename;

		// Remove the path upto the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

	private class UpdateTimeAndLocation extends AsyncTask<Void, Void, String[]> {

		@SuppressWarnings("null")
		@Override
		protected String[] doInBackground(Void... arg0) {

			updating = true;
			isDone = false;
			// TODO Auto-generated method stub

			String[] ret = null;

			HttpClient httpClient;
			httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(
					"http://app.tathva.org/updates/index.php");
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			int updateNumber = preferences.getInt("updateNum", 0);

			nameValuePairs.add(new BasicNameValuePair("tag", "update"));
			nameValuePairs.add(new BasicNameValuePair("index", Integer
					.toString(updateNumber)));
			;

			dbHelper = new DataBaseHelper(getApplicationContext());

			System.out.println("GOING FOR HTTP REQUEST");

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = httpClient.execute(httpPost);
				isDone = true;
				SharedPreferences.Editor editor = null;
				Calendar c = Calendar.getInstance();
				Date ld = c.getTime();
				editor = preferences.edit();
				editor.putString("lastUpdated", ld.toLocaleString());
				editor.commit();

				System.out.println("GOT RESPONSE");

				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				final String response = httpClient.execute(httpPost,
						responseHandler);

				System.out.println(response);
				System.out.println(updateNumber);
				int l;
				JSONArray jsonArray = null;
				if (response.equals("null"))
					l = 0;
				else {
					jsonArray = new JSONArray(response);
					l = jsonArray.length();
				}

				int i = 0;
				JSONObject json = null;
				boolean done = false;
				boolean thisDone = false;
				int max = updateNumber;
				int temp = 0;
				done = true;

				if (l != 0) {

					dbHelper.openWritableDatabase();

				}

				while (i != l) {
					json = (JSONObject) jsonArray.get(i);
					dbHelper.updateEvent(json);

					if (json.getString("code").equals(code)) {

						thisDone = true;
						ret = new String[6];
						ret[0] = json.getString("day1");
						ret[1] = json.getString("day2");
						ret[2] = json.getString("day3");
						ret[3] = json.getString("building");
						ret[4] = json.getString("room");

						ret[5] = Integer.toString(json.getInt("status"));

					}

					temp = json.getInt("number");

					if (temp > max)

						max = temp;

					i++;

				}

				if (done) {
					editor.putInt("updateNum", max);

				}
				editor.commit();
				if (!thisDone)
					ret = null;
			}

			catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;

		}

		@Override
		protected void onPostExecute(String[] updateResult) {

			if (updateResult != null) {

				building = updateResult[3];

				if ((updateResult[4].equals("null"))
						&& (updateResult[3].equals("null")))
					location.setText("Not Available");

				if (updateResult[4].equals("null"))
					location.setText(updateResult[3]);

				else
					location.setText(updateResult[3] + " , " + updateResult[4]);

				day1s = updateResult[0];
				day2s = updateResult[1];
				day3s = updateResult[2];

				status = Integer.parseInt(updateResult[5]);

				try {
					timeManager();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Toast.makeText(getApplicationContext(), "Done  :)",
						Toast.LENGTH_LONG).show();

			} else {
				if (isDone) {
					Toast.makeText(getApplicationContext(),
							"No Updates for this Event", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Network error, Try again !", Toast.LENGTH_LONG)
							.show();
				}

			}

			if (isDone)
				lastUpdated.setText("Last updated at : Just now ");
			updating = false;
			update.setText("Update Now");
			update.setClickable(true);
			findthisevent.setClickable(true);
			findthisevent.setText("Find this Event");

		}
	}
	
	

}