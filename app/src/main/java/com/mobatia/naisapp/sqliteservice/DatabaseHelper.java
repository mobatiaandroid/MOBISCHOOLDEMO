package com.mobatia.naisapp.sqliteservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static String DB_PATH = "/data/data/com.mobatia.naisapp/databases/";
	private String DB_NAME;
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DatabaseHelper(Context context, String dbName) {
		super(context, dbName, null, 1);
		this.myContext = context;
		this.DB_NAME = dbName;
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
        System.out.println("dbExist::"+dbExist);
		if (dbExist) {

		} else {
			Log.v("Need to create", "Need to create");
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			checkDB.close();

			Log.v("DB Exists", "DB Exists");
		} catch (SQLiteException e) {
			Log.v("Database Not Exist", "Database Not Exist");
		}

		/*if (checkDB != null) {
			checkDB.close();
		}*/

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {

		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(Payment.CREATE_TABLE);
//	}

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

//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS "+Payment.TABLE_NAME);
//	}
//	public  long  insertPayment(String payment)
//	{
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(Payment.COLUMN_ORDER_ID,payment);
//		values.put(Payment.COLUMN_STATUS,payment);
//		long id= db.insert(Payment.TABLE_NAME,null,values);
//		db.close();
//		return id;
//		}
//      public Payment getPayment(long id)
//	  {
//	  	SQLiteDatabase db= this.getReadableDatabase();
//		  Cursor cursor = db.query(Payment.TABLE_NAME,
//				  new String[]{Payment.COLUMN_ID,Payment.COLUMN_ORDER_ID,Payment.COLUMN_STATUS},
//				  Payment.COLUMN_ID+"=?",
//				  new String[]{String.valueOf(id)},null,null,null);
//		  if (cursor!=null)
//		  {
//		  	cursor.moveToFirst();
//		  }
//		  Payment payment= new Payment(
//		  		cursor.getInt(cursor.getColumnIndex(Payment.COLUMN_ID)),
//						cursor.getString(cursor.getColumnIndex(Payment.COLUMN_ORDER_ID)),
//				        cursor.getString(cursor.getColumnIndex(Payment.COLUMN_STATUS))
//		  );
//		  cursor.close();
//		  return payment;
//	  }
}
