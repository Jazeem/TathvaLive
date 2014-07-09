package com.tathva.tathva_live.activity;

import java.io.IOException;

import com.tathva.tathva_live.R;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

public class SpotsList extends ExpandableListActivity {

	private DataBaseHelper mDbHelper; // your db adapter
	private Cursor mGroupsCursor; 
	private Cursor buildingsCursor;
	// cursor for list of groups (list top nodes)
	private int mGroupIdColumnIndex;
	private ExpandableListAdapter mAdapter;
	int lastExpandedGroupPosition=-1;
	ExpandableListView listView;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDbHelper = new DataBaseHelper(this);
		try {
			mDbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDbHelper.openDataBase();

		fillData();
		listView=getExpandableListView();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}

	
	@Override
	 public void onGroupExpand(int groupPosition){
	        //collapse the old expanded group, if not the same
	        //as new group to expand
	        if(groupPosition != lastExpandedGroupPosition){
	            listView.collapseGroup(lastExpandedGroupPosition);
	        }

	        super.onGroupExpand(groupPosition);           
	        lastExpandedGroupPosition = groupPosition;
	    }
	
	
	 
	 
	private void fillData() {
		mGroupsCursor = mDbHelper.fetchAllTypes(); // fills cursor with list of
													// your top nodes - groups
	

		// Cache the ID column index
		// mGroupIdColumnIndex =
		// mGroupsCursor.getColumnIndexOrThrow("rowid _id");

		// Set up our adapter
		mAdapter = new MyExpandableListAdapter(mGroupsCursor, this,

		android.R.layout.simple_expandable_list_item_1,
				android.R.layout.simple_list_item_1,

				new String[] { "type" }, // group title for group layouts
				new int[] { android.R.id.text1 },

				new String[] { "building" }, // exercise title for child layouts
				new int[] { android.R.id.text1 });

		setListAdapter(mAdapter);
	}

	// extending SimpleCursorTreeAdapter
	public class MyExpandableListAdapter extends SimpleCursorTreeAdapter {

		protected final String BUILDING = null;

		public MyExpandableListAdapter(Cursor cursor, Context context,
				int groupLayout, int childLayout, String[] groupFrom,
				int[] groupTo, String[] childrenFrom, int[] childrenTo) {
			super(context, cursor, groupLayout, groupFrom, groupTo,
					childLayout, childrenFrom, childrenTo);
		}

		// returns cursor with subitems for given group cursor
		@Override
		protected Cursor getChildrenCursor(Cursor typeCursor) {
			buildingsCursor = mDbHelper.fetchBuildingsForType(typeCursor
					.getString(typeCursor.getColumnIndex("type")));
			
			return buildingsCursor;
		}

		// I needed to process click on click of the button on child item
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			final View rowView = super.getChildView(groupPosition,
					childPosition, isLastChild, convertView, parent);
			
			TextView text = (TextView) rowView
					.findViewById(android.R.id.text1);
			text.setBackgroundColor(Color.GRAY);
			text.setPadding(80,0,0,0);
			text.setTextSize(16);
			
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					TextView text = (TextView) rowView
							.findViewById(android.R.id.text1);
					

					String b = text.getText().toString();

					LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					if (!locationMgr
							.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						buildAlertMessageNoGps();
					} else {

						if (b.equals("null")) {

						}

						else {
							Intent nextScreen = new Intent(SpotsList.this, Demo.class);

							nextScreen.putExtra(BUILDING, b);

							startActivity(nextScreen);

						}
					}

				}
			});

			return rowView;
		}
		
		

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
}
