package com.tathva.tathva_live.activity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.tathva.tathva_live.ui.IconMarker;
import com.tathva.tathva_live.ui.Marker;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.jwethere.augmented_reality/databases/";

	private static String DB_NAME = "tathva.db";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		if (android.os.Build.VERSION.SDK_INT >= 4.2) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		 SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(myContext);
		int firsttime= preferences.getInt("firsttime",0);
		SharedPreferences.Editor editor=preferences.edit();
		
		
		if(firsttime==0)
		{
			editor.putInt("firsttime",1);
			editor.putInt("newsFeedUpdateNum",0);
			editor.commit();
		}
		
		

		if (dbExist&&(firsttime!=0)) {
			// do nothing - database already exist
			
			
			
		} else {

			editor.putInt("firsttime",1);
			editor.commit();
			
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	public void openWritableDatabase() throws SQLException {
		String mypath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(mypath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor getEvents(String type) {
		String[] n = { "rowid _id", "name" };
		String[] a = { type };

		return myDataBase.rawQuery("SELECT rowid _id,name FROM events WHERE type=? ORDER BY name COLLATE NOCASE",a);

	}

	public Cursor getEventsByGenre(String genre) {
		String[] n = { "rowid _id", "name" };
		String[] a = { genre };

		
		return myDataBase
				.rawQuery(
						"SELECT rowid _id,name FROM events WHERE genre=? ORDER BY name COLLATE NOCASE",
						a);

	}

	public Cursor getGenres(String type) {
		String[] n = { "rowid _id", "genre" };
		String[] a = { type };

		return myDataBase
				.rawQuery(
						"SELECT DISTINCT rowid _id,genre FROM events WHERE type=? GROUP BY genre ORDER BY genre COLLATE NOCASE",
						a);

	}

	public Cursor getEventDetails(String name) {
		String[] a = { name };
		String[] n = { "code", "name","type", "description", "day1", "day2", "day3",
				"building", "room", "contactname", "contactnumber", "status","results" };

		return myDataBase.query("events", n, "name=?", a, null, null, null);

	}
	
	
	
	public Cursor getResult(String code) {
		String[] a = { code };
		String[] n = { "results" };

		return myDataBase.query("events", n, "code=?", a, null, null, null);

	}

	public int updateEvent(JSONObject json) throws JSONException {
		ContentValues newValues = new ContentValues();
		newValues.put("day1", json.getString("day1"));
		newValues.put("day2", json.getString("day2"));
		newValues.put("day3", json.getString("day3"));
		newValues.put("building", json.getString("building"));
		newValues.put("room", json.getString("room"));
		newValues.put("status", json.getInt("status"));
		newValues.put("results", json.getString("results"));
		newValues.put("name", json.getString("name"));
		
		String[] args = new String[] { json.getString("code") };
		return myDataBase.update("events", newValues, "code=?", args);

	}

	

	public long updateNewsFeed(JSONObject json) throws JSONException {
		ContentValues newValues = new ContentValues();
		
		newValues.put("news", json.getString("news"));
		
		newValues.put("dateandtime", json.getString("dateandtime"));
	//	newValues.put("number", json.getInt("number"));
	
 
		return  myDataBase.insertOrThrow("newsfeed",null,newValues);

	}
	
	public Cursor getAllBuildings()
	{
		String[] n = { "rowid _id", "building" };
	

		return myDataBase
				.rawQuery(
						"SELECT DISTINCT rowid _id,building FROM location ORDER BY building COLLATE NOCASE",
						null);
	}
	
	
	
	public Cursor getNewsFeed()
	{
		

		return myDataBase.rawQuery("SELECT rowid _id,news,dateandtime FROM newsfeed  ORDER BY number DESC",null);
			}
	
	
	public Cursor getMarkerDB(String building) {
		String[] n = { "latitude", "longitude" };
		String[] a = { building };

		Cursor cursor = myDataBase.query("location", n, "building=?", a, null,
				null, null);
		return cursor;

	}

	
	public Cursor fetchAllTypes(){
		return myDataBase
				.rawQuery(
						"SELECT DISTINCT rowid _id,type FROM location GROUP BY type ORDER BY type COLLATE NOCASE",
						null);
		
	}
	
	public Cursor fetchBuildingsForType(String type)
	{
		String[] a={type};
		return myDataBase
				.rawQuery(
						"SELECT rowid _id,building FROM location WHERE type=? ORDER BY building COLLATE NOCASE",
						a);
	}

	
	public int getBuildingNo(String building)
	{
		String[] n={"rowid _id","building"};
		String[] a={building};
		
		Cursor cursor = myDataBase.query("location", n, "building=?", a, null,
				null, null);
		cursor.moveToFirst();
		return cursor.getInt(cursor.getColumnIndex("_id"));
	}
	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

}