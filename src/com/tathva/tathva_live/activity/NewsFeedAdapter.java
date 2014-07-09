package com.tathva.tathva_live.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.tathva.tathva_live.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
//import android.support.v4.widget.SearchViewCompatIcs.MySearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsFeedAdapter extends CursorAdapter {
	protected static final String BUILDING = null;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private int scrWidth;
	private int scrHeight;
	SimpleDateFormat formatter;
	private LinearLayout.LayoutParams parms;

	public NewsFeedAdapter(Context context, Cursor c) {
		super(context, c);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);

		
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*
		 * 
		 * mLayoutInflater = LayoutInflater.from(context); scrWidth =
		 * ((Activity)
		 * mContext).getWindowManager().getDefaultDisplay().getWidth();
		 * scrHeight = ((Activity)
		 * mContext).getWindowManager().getDefaultDisplay().getHeight(); parms=
		 * new LinearLayout.LayoutParams( (int) scrWidth/3,(int)scrWidth/3);
		 */
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mLayoutInflater.inflate(R.layout.one_news, parent, false);
		return v;
	}

	/**
	 * @author will
	 * 
	 * @param v
	 *            The view in which the elements we set up here will be
	 *            displayed.
	 * 
	 * @param context
	 *            The running context where this ListView adapter will be
	 *            active.
	 * 
	 * @param c
	 *            The Cursor containing the query results we will display.
	 */

	@Override
	public void bindView(View v, Context context, Cursor c) {
		String text = c.getString(c.getColumnIndex("news"));

		//=new TextView(mContext);
		TextView newsText = (TextView) v.findViewById(R.id.news);
			
		if (newsText != null) {
			newsText.setText(Html.fromHtml(text));
			
			// newsText.setMovementMethod(LinkMovementMethod.getInstance());
		}
		

		
		TextView dateandtime = (TextView) v.findViewById(R.id.dateandtime);
		String d = c.getString(c.getColumnIndex("dateandtime"));
		if (d == null)
			d = "null";
		if (!d.equals("null")){
			
			
			try {
				Date date=formatter.parse(d);
				
				Calendar now = Calendar.getInstance();
				long nowMilli=now.getTimeInMillis();
				now.setTime(date);
				long timeMilli=now.getTimeInMillis();
				d=(String) DateUtils.getRelativeTimeSpanString(timeMilli, nowMilli,1000);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
			dateandtime.setText(d);
		}
		

	}

}