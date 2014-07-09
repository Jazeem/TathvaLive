package com.tathva.tathva_live.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.tathva.tathva_live.R;
import com.tathva.tathva_live.activity.DataBaseHelper;
import com.tathva.tathva_live.ui.IconMarker;
import com.tathva.tathva_live.ui.Marker;

/**
 * This class should be used as a example local data source. It is an example of
 * how to add data programatically. You can add data either programatically,
 * SQLite or through any other source.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class LocalDataSource extends DataSource {

	
	Context context;
	DataBaseHelper dbHelper ;
	
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;
    private static String imageName;
    
    public LocalDataSource(Resources res,Context context,String imageName) {
    	
    	this.context=context;
    	this.imageName=imageName;
    	dbHelper =new DataBaseHelper(this.context);
        if (res == null) throw new NullPointerException();

        createIcon(res);
        
    }

    
    
    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();
        int b = res.getIdentifier(imageName, "drawable",
       		 context.getPackageName());

        icon = BitmapFactory.decodeResource(res,b);
    }

    public List<Marker> getMarkers() {
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
        /*Marker atl = new IconMarker("ATL ICON", 39.931228, -75.051262, 0, Color.DKGRAY, icon);
        cachedMarkers.add(atl);

        Marker home = new Marker("ATL CIRCLE", 11.264728, 75.821317, 0, Color.YELLOW);
        cachedMarkers.add(home);*/
    	
        /*Marker oat = new IconMarker("OAT", 11.322039, 75.933423, 0, Color.DKGRAY, icon);
        cachedMarkers.add(oat);
        Marker elhc = new IconMarker("ELHC", 11.322449, 75.93394, 0, Color.DKGRAY, icon);
        cachedMarkers.add(elhc);
        Marker nsl = new IconMarker("NSL", 11.322739, 75.934337, 0, Color.DKGRAY, icon);
        cachedMarkers.add(nsl);
        Marker mb = new IconMarker("MB", 11.3215, 75.934123, 0, Color.DKGRAY, icon);
        cachedMarkers.add(mb);
        Marker lib = new IconMarker("LIBRARY", 11.321976, 75.934836, 0, Color.DKGRAY, icon);
        cachedMarkers.add(lib);
        Marker db = new IconMarker("DB", 11.32227, 75.934871, 0, Color.DKGRAY, icon);
        cachedMarkers.add(db);
        Marker audi = new IconMarker("AUDI", 11.322439, 75.935751, 0, Color.DKGRAY, icon);
        cachedMarkers.add(audi);
        Marker ah = new IconMarker("A HOSTEL", 11.320787, 75.934941, 0, Color.DKGRAY, icon);
        cachedMarkers.add(ah);
        Marker bh = new IconMarker("B HOSTEL", 11.320398, 75.935909, 0, Color.DKGRAY, icon);
        cachedMarkers.add(bh);
        Marker ch = new IconMarker("C HOSTEL", 11.320114, 75.936813, 0, Color.DKGRAY, icon);
        cachedMarkers.add(ch);
        Marker dh = new IconMarker("D HOSTEL", 11.320143, 75.93769, 0, Color.DKGRAY, icon);
        cachedMarkers.add(dh);
        Marker eh = new IconMarker("E HOSTEL", 11.319688, 75.938508, 0, Color.DKGRAY, icon);
        cachedMarkers.add(eh);
        Marker fh = new IconMarker("F HOSTEL", 11.320619, 75.937545, 0, Color.DKGRAY, icon);
        cachedMarkers.add(fh);
        Marker gh = new IconMarker("G HOSTEL", 11.321232, 75.936808, 0, Color.DKGRAY, icon);
        cachedMarkers.add(gh);
        Marker ab = new IconMarker("AB", 11.320553, 75.932873, 0, Color.DKGRAY, icon);
        cachedMarkers.add(ab);*/
        /*
         * Marker lon = new IconMarker(
         * "I am a really really long string which should wrap a number of times on the screen."
         * , 39.95335, -74.9223445, 0, Color.MAGENTA, icon);
         * cachedMarkers.add(lon); Marker lon2 = new IconMarker(
         * "2: I am a really really long string which should wrap a number of times on the screen."
         * , 39.95334, -74.9223446, 0, Color.MAGENTA, icon);
         * cachedMarkers.add(lon2);
         */

        /*
         * float max = 10; for (float i=0; i<max; i++) { Marker marker = null;
         * float decimal = i/max; if (i%2==0) marker = new Marker("Test-"+i,
         * 39.99, -75.33+decimal, 0, Color.LTGRAY); marker = new
         * IconMarker("Test-"+i, 39.99+decimal, -75.33, 0, Color.LTGRAY, icon);
         * cachedMarkers.add(marker); }
         */
    	
    	
    	
    	

        return cachedMarkers;
    }

	public List<Marker> getMarkerDB(String building){
     
		cachedMarkers=new ArrayList<Marker>();
		dbHelper.openDataBase();
		Cursor cursor=dbHelper.getMarkerDB(building);
		cursor.moveToFirst();
		Marker marker=new IconMarker(building,cursor.getFloat(cursor.getColumnIndex("latitude")),
				cursor.getFloat(cursor.getColumnIndex("longitude")),0,Color.DKGRAY,icon);
		cachedMarkers.add(marker);
		return cachedMarkers;
	}
	
	public int getBuildingNo(String building){
		
		dbHelper.openDataBase();
		return dbHelper.getBuildingNo(building);
		
	}
}
