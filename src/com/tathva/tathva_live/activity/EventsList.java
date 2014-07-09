package com.tathva.tathva_live.activity;



import java.io.IOException;

import com.tathva.tathva_live.R;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.widget.SimpleCursorAdapter; 

public class EventsList extends ListActivity   {

	
   protected static final String NAME = null;
SimpleCursorAdapter mAdapter; 		
   LoaderManager loadermanager;		
   CursorLoader cursorLoader;
   DataBaseHelper dbHelper;
   
   
   
   private static String TAG="CursorLoader";
   
   protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	
	setContentView(R.layout.simple_list_item1);
	String[] uiBindFrom = {"name"};		
	int[] uiBindTo = {android.R.id.text1};
	
	dbHelper=new DataBaseHelper(this);
	try {
		dbHelper.createDataBase();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	dbHelper.openDataBase();
	Intent intent = getIntent();
	String type = intent.getStringExtra(Home.TYPE);
	
	Cursor cursor=dbHelper.getEvents(type);
	
	
       /*Empty adapter that is used to display the loaded data*/
	mAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor, uiBindFrom, uiBindTo,0);  
       setListAdapter(mAdapter);
 
       
}
   @Override
   protected void onListItemClick(ListView l, View v, int position, long id){
	    
	   Intent nextScreen = new Intent(EventsList.this,Event.class);
	  String selection=((TextView)v).getText().toString();	   
	   System.out.println(selection);
       
	   
       nextScreen.putExtra(NAME,selection);
		// starting new activity
		startActivity(nextScreen);
   }
}