package com.tathva.tathva_live.activity;
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


public class ItemAdapter extends CursorAdapter {
    protected static final String BUILDING = null;
	private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int scrWidth ;
	private int scrHeight ;

    private LinearLayout.LayoutParams parms ;
    private LinearLayout.LayoutParams parms1 ;
    public ItemAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        scrWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        scrHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
       parms= new LinearLayout.LayoutParams(
   			(int) scrWidth/3,(int)scrWidth/3);
       parms1= new LinearLayout.LayoutParams(
      			(int) (scrWidth*2)/3,(int)scrWidth/3);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.one_location, parent, false);
        return v;
    }

    /**
     * @author will
     * 
     * @param   v
     *          The view in which the elements we set up here will be displayed.
     * 
     * @param   context
     *          The running context where this ListView adapter will be active.
     * 
     * @param   c
     *          The Cursor containing the query results we will display.
     */

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String name = c.getString(c.getColumnIndex("building"));
       
        
        

        TextView buildingName = (TextView) v.findViewById(R.id.locationname);
        if (buildingName != null) {
            buildingName.setText(name);
        }

        buildingName.setLayoutParams(parms1);
		
   /*     ImageView locationImage = (ImageView) v.findViewById(R.id.locationimage);
        
		
		

        locationImage.setImageResource(R.drawable.building);
        locationImage.setLayoutParams(parms);
        
        */
      /*  
        Button findThisSpot=(Button) v.findViewById(R.id.findthisspot);
        findThisSpot.setTag(name);
        findThisSpot.setLayoutParams(parms);
        
    	findThisSpot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String b=(String) arg0.getTag();
				LocationManager locationMgr = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
				if (!locationMgr
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					buildAlertMessageNoGps();
				} else {
					
					if(b.equals("null"))
					{
						
						
					}
					
					else{
					Intent nextScreen = new Intent(mContext,
							Demo.class);
					
					String building;
					nextScreen.putExtra(BUILDING,b);
					mContext.startActivity(nextScreen);
				}}
			}
		});
    	
    	*/
      
    }
    
	private void buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		alertDialogBuilder
				.setMessage(
						"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								mContext.startActivity(new Intent(
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