package org.globant.emilglober.diy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static int version = 1; 	//11;	// 9	
	
	private static String name = "DIYDB";
	private static CursorFactory factory = null;

	// "Um, boss... instead of building a table JUST FOR THIS, why not using a JSON file?"
	// "We will, but first we have to get something workable up and FAST!"

	// TODO Implement a JSON file to read the user info! 
	
	private String T_USERDATA = "USERDATA",
			C_ID = "_id",
			C_NAME = "diy_name",
			C_USER_EMAIL = "diy_user_email",
			C_RECIPIENT_EMAIL = "diy_rec_email",
			C_USES_METRIC = "diy_uses_metric";

	private String D_USERDATA = "CREATE TABLE " + T_USERDATA + "("
			+ C_ID + " " + "INTEGER PRIMARY KEY" + ", "
			+ C_NAME + " " + "TEXT NOT NULL" + ", "
			+ C_USER_EMAIL + " " + "TEXT NOT NULL" + ", "
			+ C_RECIPIENT_EMAIL + " " + "TEXT NOT NULL" + ", "
			+ C_USES_METRIC + " " + "INTEGER DEFAULT 1 "
			+ ")";
	
	private String D_USER_INDEX = "CREATE UNIQUE INDEX " + C_NAME 
			+ " ON " + T_USERDATA
			+ "(" + C_NAME + " ASC)"; 

	private String T_MEASUREMENTS = "MEASUREMENTS",
			C_DATE = "diy_date",
			C_GRAMS = "diy_grams",
			C_POUNDS = "diy_pounds";

	private String D_MEASUREMENTS = "CREATE TABLE " + T_MEASUREMENTS + "(" 
			+ C_ID + " " + "INTEGER PRIMARY KEY" + ", "
			+ C_DATE + " " + "DATETIME NOT NULL" + ", "
			+ C_GRAMS + " " + "INTEGER NOT NULL" + ", "
			+ C_POUNDS + " " + "REAL NOT NULL "
			+ ")";
	
	private String D_MEAS_INDEX = "CREATE UNIQUE INDEX " + C_DATE 
			+ " ON " + T_MEASUREMENTS
			+ "(" + C_DATE + " ASC)"; 
	
	public DBHelper(Context context)
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		Log.i(this.getClass().toString(), "Building DB");
		
		db.execSQL(D_MEASUREMENTS);
		db.execSQL(D_MEAS_INDEX);
		
		db.execSQL(D_USERDATA);
		db.execSQL(D_USER_INDEX);
		
		Log.i(this.getClass().toString(), "Database created");
		
//		upgradeToVersion2(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
//		if (oldVersion < 2)
//		{
//			try
//			{
//				upgradeToVersion2(db);
//			}
//			catch(SQLException e)
//			{
//				Log.i(this.getClass().toString(), e.getMessage());
//			}
//		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) 
	{
	    super.onOpen(db);
	    
	    if (!db.isReadOnly()) 
	    {
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	@SuppressWarnings("unused")
	private void upgradeToVersion2(SQLiteDatabase db) 
	{
//		db.execSQL("ALTER TABLE " + T_PROPERTIES + " ADD " + C_CONFIRMED + " INTEGER NOT NULL DEFAULT 0");
//		
//		Log.i(this.getClass().toString(), "Update to version 2 complete");
	}
}
