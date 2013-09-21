package com.waterloo.buddyalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TimePicker;

import java.util.ArrayList;

/**
 * Created by supreet on 9/21/13.
 */

public class Database {

    private static final String DATABASE_NAME = "BuddyAlarm.db";
    private static final int DATABASE_VERSION = 1;

    public static final String ALARMS_TABLE = "Alarms_Table";
    public static final String ALARMS_TABLE_ID = "id";
    public static final String ALARMS_TABLE_NAME = "name";
    public static final String ALARMS_TABLE_PASSCODE = "passcode";
    public static final String ALARMS_TABLE_TIME = "time";
    public static final String ALARMS_TABLE_DESCRIPTION = "description";

    private DbHelper mHelper;
    private final Context mContext;
    private static SQLiteDatabase mDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + ALARMS_TABLE + " ("
                    + ALARMS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ALARMS_TABLE_PASSCODE + " TEXT NOT NULL, "
                    + ALARMS_TABLE_TIME + " INTEGER NOT NULL, "
                    + ALARMS_TABLE_DESCRIPTION + " TEXT NOT NULL, "
                    + ALARMS_TABLE_NAME + " TEXT NOT NULL); ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE);
        }

    }

    public Database(Context c) {
        mContext = c;
    }

    public Database open() throws SQLException {
        mHelper = new DbHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mHelper.close();
    }

    public boolean addOrUpdateAlarmInDatabase(int id, String name, String time, String passcode, String description) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ALARMS_TABLE_NAME, name);
        contentValue.put(ALARMS_TABLE_TIME, time);
        contentValue.put(ALARMS_TABLE_PASSCODE, passcode);
        contentValue.put(ALARMS_TABLE_DESCRIPTION, description);
        if (alarmExistsInDatabase(id)) {
            int numberOfRowsAffected = mDatabase.update(ALARMS_TABLE, contentValue, ALARMS_TABLE_ID + "=" + id, null);
            if (0 ==  numberOfRowsAffected)
                return false;
            else
                return true;
        }
        long i = mDatabase.insert(ALARMS_TABLE, null, contentValue);
        if (i == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> getAlarmDetails(String name) {
        String columns[] = new String[]{ALARMS_TABLE_ID, ALARMS_TABLE_NAME, ALARMS_TABLE_TIME, ALARMS_TABLE_PASSCODE, ALARMS_TABLE_DESCRIPTION};
        Cursor cursor = mDatabase.query(ALARMS_TABLE, columns, null, null,
                null, null, null);
        int nameColumnIndex = cursor.getColumnIndex(ALARMS_TABLE_NAME);
        ArrayList<String> returnList = new ArrayList<String>(5);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (name.equals(cursor.getString(nameColumnIndex))) {
                Log.i("Found Alarm:", "Yes");
                returnList.add(cursor.getString(cursor.getColumnIndex(ALARMS_TABLE_ID)));
                returnList.add(cursor.getString(cursor.getColumnIndex(ALARMS_TABLE_NAME)));
                returnList.add(cursor.getString(cursor.getColumnIndex(ALARMS_TABLE_TIME)));
                returnList.add(cursor.getString(cursor.getColumnIndex(ALARMS_TABLE_PASSCODE)));
                returnList.add(cursor.getString(cursor.getColumnIndex(ALARMS_TABLE_DESCRIPTION)));
                return returnList;
            }
        }
        return null;
    }

    public Boolean alarmExistsInDatabase(int id) {
        String columns[] = new String[]{ALARMS_TABLE_ID};
        Cursor cursor = mDatabase.query(ALARMS_TABLE, columns, null, null,
                null, null, null);
        int idColumnIndex = cursor.getColumnIndex(ALARMS_TABLE_ID);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (id == cursor.getInt(idColumnIndex)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getAlarmNames() {
        String columns[] = new String[]{ALARMS_TABLE_NAME};
        Cursor cursor = mDatabase.query(ALARMS_TABLE, columns, null, null,
                null, null, null);
        ArrayList<String> groups = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            groups.add(cursor.getString(cursor.getColumnIndex(ALARMS_TABLE_NAME)));
        }
        return groups;
    }

    public boolean deleteAlarm(String alarmName) {
        int numberOfRowsAffected = mDatabase.delete(ALARMS_TABLE, ALARMS_TABLE_NAME + "=?", new String[] { alarmName });
        if (1 == numberOfRowsAffected) {
            return  true;
        }
        else
        {
            return false;
        }
    }
}
