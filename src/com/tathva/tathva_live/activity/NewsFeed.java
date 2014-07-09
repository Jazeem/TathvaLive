package com.tathva.tathva_live.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFeed extends ListActivity implements OnItemClickListener {
	private static final int THRESHOLD = 0;
	private static final boolean STATE_REFRESH_ENABLED = false;
	private static final boolean STATE_REFRESHING = false;
	ListView listView;
	DataBaseHelper dbHelper;
	boolean done = false;

	ProgressDialog updating;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newsfeed);

		listView = getListView();

		dbHelper = new DataBaseHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHelper.openDataBase();
		Cursor cursor = dbHelper.getNewsFeed();

		// Own row layout
		listView.setAdapter(new NewsFeedAdapter(this, cursor));

		updating = new ProgressDialog(this);

		updating.setCancelable(true);
		updating.setMessage("Updating..");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!networkCheckIn()) {
			Toast.makeText(getApplicationContext(),
					"No Internet Access, Review network settings !",
					Toast.LENGTH_LONG).show();
		}

		else {
			UpdateTimeAndLocation task = new UpdateTimeAndLocation();
			updating.show();
			task.execute();

		}
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

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		// String selection = ((TextView) view).getText().toString();

	}

	private class UpdateTimeAndLocation extends
			AsyncTask<Void, Void, JSONArray> {

		@SuppressWarnings("null")
		@Override
		protected JSONArray doInBackground(Void... arg0) {

			// TODO Auto-generated method stub

			done = false;
			JSONArray ret = null;

			HttpClient httpClient;
			httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(
					"http://app.tathva.org/updates/index.php");
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			int updateNumber = preferences.getInt("newsFeedUpdateNum", 0);

			nameValuePairs.add(new BasicNameValuePair("tag", "updateNewsFeed"));
			nameValuePairs.add(new BasicNameValuePair("index", Integer
					.toString(updateNumber)));
			;

			dbHelper = new DataBaseHelper(getApplicationContext());

			System.out.println("GOING FOR HTTP REQUEST");

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				SharedPreferences.Editor editor = null;
				editor = preferences.edit();

				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				final String response = httpClient.execute(httpPost,
						responseHandler);

				done = true;
				/*
				 * Calendar c = Calendar.getInstance(); Date ld = c.getTime();
				 * editor = preferences.edit(); editor.putString("lastUpdated",
				 * ld.toString()); editor.commit();
				 */

				System.out.println("GOT RESPONSE");

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
					ret = jsonArray;

				}

				while (i != l) {

					json = (JSONObject) jsonArray.get(i);

					dbHelper.updateNewsFeed(json);

					thisDone = true;

					temp = json.getInt("number");

					if (temp > max)

						max = temp;

					i++;

				}

				if (done) {
					editor.putInt("newsFeedUpdateNum", max);

				}
				System.out.println("DONE THIS");
				editor.commit();

				System.out.println("DONE THAT");

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
		protected void onPostExecute(JSONArray jsonArray) {
			int s = 0;

			JSONObject j;

			if (jsonArray != null) {
				s = jsonArray.length();

				if (s != 0) {

					int i = 0;
					j = null;
					while (s != i) {

						try {
							j = (JSONObject) jsonArray.get(i);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							String dateandtime = j.getString("dateandtime");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							String news = j.getString("news");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						i++;
					}

					Toast.makeText(getApplicationContext(), "New updates !",
							Toast.LENGTH_SHORT).show();
					Cursor c = dbHelper.getNewsFeed();

					// Own row layout
					listView.setAdapter(new NewsFeedAdapter(
							getApplicationContext(), c));

				}

			} else {
				if (done) {
					Toast.makeText(getApplicationContext(), "No Updates !",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Network error, Try again !", Toast.LENGTH_LONG)
							.show();
				}

			}

			updating.hide();
		}

	}

}
