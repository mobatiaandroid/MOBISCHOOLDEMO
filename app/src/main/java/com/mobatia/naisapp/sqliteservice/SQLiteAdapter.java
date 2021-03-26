package com.mobatia.naisapp.sqliteservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteAdapter {

	private Context context;
	private String DB_NAME;
	private static final int DB_VERSION = 1;
	private static final String TAG = null;
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	public static int MAX_INSERT_RECORDS;
	public static int CURRENT_RECORD;

	public SQLiteAdapter(Context c, String dbName) {
		this.context = c;
		this.DB_NAME = dbName;
	}

	public SQLiteAdapter openToRead() throws SQLException {
		sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public SQLiteAdapter openToWrite() throws SQLException {
		sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void executeDump() {

	}
	
	public int deleteItem(String DB_TABLE, String condition) {

		return sqLiteDatabase.delete(DB_TABLE, condition, null);
	}

	public void excuteRawQuery(String query) throws SQLException {
		sqLiteDatabase.execSQL(query);

	}

	public void close() {
		sqLiteHelper.close();
	}

	public long getCount(String DB_TABLE, String[][] constraints) {
		String constraint = "";
		SQLiteStatement s;
		for (int j = 0; j < constraints.length && constraints.length > 0; j++) {
			if (j == constraints.length - 1 || constraints.length == 1) {
				constraint += constraints[j][0] + " = " + constraints[j][1];
			} else {
				constraint += constraints[j][0] + " = " + constraints[j][1]
						+ " AND ";
			}
		}
		if (constraint != "") {
			s = sqLiteDatabase.compileStatement("SELECT COUNT(*) FROM "
					+ DB_TABLE + " WHERE " + constraint);
		} else {
			s = sqLiteDatabase.compileStatement("SELECT COUNT(*) FROM "
					+ DB_TABLE);
		}

		long count = s.simpleQueryForLong();
		return count;
	}

	public void update(String[][] content, String DB_TABLE,
			String[][] constraints) {

		ContentValues contentValues = new ContentValues();
		String constraint = "";
		List<String> columns = getAllColumns(DB_TABLE);
		int i = 0;
		for (String COLUMN : columns) {
			for (i = 0; i < content.length; i++) {
				if (content[i][0].equalsIgnoreCase(COLUMN)
						&& (!content[i][1].equalsIgnoreCase("") || content[i][1] == null)) {
					contentValues.put(COLUMN, content[i][1]);
					Log.v("column" + (i + 1), COLUMN + " " + content[i][1]);
				}
			}
		}

		for (int j = 0; j < constraints.length && constraints.length > 0; j++) {
			if (j == constraints.length - 1 || constraints.length == 1) {
				constraint += constraints[j][0] + " = " + constraints[j][1];
			} else {
				constraint += constraints[j][0] + " = " + constraints[j][1]
						+ " && ";
			}
		}
		try {
			sqLiteDatabase.beginTransaction();
			sqLiteDatabase.update(DB_TABLE, contentValues, constraint, null);
			sqLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("Error:", "Error: " + e.toString());
		} finally {
			sqLiteDatabase.endTransaction();
		}
	}

	public Boolean insert(String[][] content, String DB_TABLE) {
		ContentValues contentValues = new ContentValues();
		List<String> columns = getAllColumns(DB_TABLE);
		int i = 0;
		for (String COLUMN : columns) {
			for (i = 0; i < content.length; i++) {
				if (content[i][0].equalsIgnoreCase(COLUMN)
						&& (!content[i][1].equalsIgnoreCase("") || content[i][1] == null)) {
					contentValues.put(COLUMN, content[i][1]);
					Log.v("column" + (i + 1), COLUMN + " " + content[i][1]);
				}
			}
		}
		try {
			sqLiteDatabase.beginTransaction();
			if (sqLiteDatabase.insert(DB_TABLE, null, contentValues) >= 0) {
				sqLiteDatabase.setTransactionSuccessful();
				sqLiteDatabase.endTransaction();
				return true;
			} else {
				sqLiteDatabase.setTransactionSuccessful();
				sqLiteDatabase.endTransaction();
				return false;
			}
		} catch (Exception e) {
			Log.v("Error:", "Error: " + e.toString());
		}
		return false;
	}

	public int makeEmpty(String DB_TABLE) {
		return sqLiteDatabase.delete(DB_TABLE, null, null);
	}

	public int makeEmpty(String DB_TABLE, String cons) {
		return sqLiteDatabase.delete(DB_TABLE, cons, null);
	}

	public List<String> getAllColumns(String DB_TABLE) {
		String[] columns;
		List<String> list = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query(DB_TABLE, null, null, null, null,
				null, null);
		columns = cursor.getColumnNames();

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		for (int i = 0; i < columns.length; i++) {
			list.add(columns[i]);
		}
		return list;
	}

	public Cursor queueAll(String DB_TABLE, String[] Columns, String order,
			String condition) {
		Cursor cursor = sqLiteDatabase.query(DB_TABLE, Columns, condition,
				null, null, null, order);

		return cursor;
	}

	// select last_insert_rowid();
	public int getLatestRowId() {
		int latestId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid()",
				null);

		if (cursor != null && cursor.moveToFirst()) {
			latestId = cursor.getInt(0);// The 0 is the column index, we only
										// have 1
										// column, so the index is 0
			System.out.println("LatestId " + latestId);
			cursor.close();
		}
		return latestId;
	}

	private static class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}
}
