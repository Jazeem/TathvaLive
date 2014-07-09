package com.tathva.tathva_live.activity;

import java.io.IOException;

import com.tathva.tathva_live.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class SpotsAtNitc extends ListActivity
        implements OnItemClickListener{
    private static final String BUILDING = null;
	ListView listView;
    DataBaseHelper dbHelper;
    View selectedView=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.spotsatnitc);
        
        
        listView = getListView();
        dbHelper = new DataBaseHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHelper.openDataBase();
		Cursor cursor = dbHelper.getAllBuildings();
 
        //Own row layout
        listView.setAdapter(new ItemAdapter(this,cursor));
        listView.setOnItemClickListener(this);
       
       
    
    }
        
  @Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if(selectedView!=null)
	{
		 selectedView.setBackgroundColor(Color.BLACK);
		 TextView lm=(TextView) selectedView.findViewById(R.id.locationname);
	    	lm.setTextColor(Color.WHITE);
	    	
		 
	}
}
 
    @Override
    public void onItemClick(AdapterView<?> adapter, View view,
                int position, long id) {
    	TextView l=(TextView) view.findViewById(R.id.locationname);
    	String b;
    	b=l.getText().toString();
    	System.out.println(b);
    	System.out.println("CLICK");
    	selectedView =view;
    	view.setBackgroundColor(Color.WHITE);
    	l.setTextColor(Color.BLACK);
    	
    	
		LocationManager locationMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if (!locationMgr
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		} else {
			
			if(b.equals("null"))
			{
				
				
			}
			
			else{
			Intent nextScreen = new Intent(this,
					Demo.class);
			
			String building;
			nextScreen.putExtra(BUILDING,b);
			
			startActivity(nextScreen);
		
			
		}}
	}

    //	String selection = ((TextView) view).getText().toString();
   
    
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
						if(selectedView!=null)
						{
							 selectedView.setBackgroundResource(R.drawable.border);
							 TextView lm=(TextView) selectedView.findViewById(R.id.locationname);
						    	lm.setTextColor(Color.BLACK);
						    	
							 
						}
					}
				});
		final AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}