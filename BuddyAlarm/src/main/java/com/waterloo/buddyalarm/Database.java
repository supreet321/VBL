package com.waterloo.buddyalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                    + ALARMS_TABLE_PASSCODE + " TEXT, "
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

    public boolean addAlarmToDatabase(String name, int time, String passcode, String description) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ALARMS_TABLE_NAME, name);
        contentValue.put(ALARMS_TABLE_TIME, time);
        contentValue.put(ALARMS_TABLE_PASSCODE, passcode);
        contentValue.put(ALARMS_TABLE_DESCRIPTION, description);
        long i = mDatabase.insert(ALARMS_TABLE, null, contentValue);
        if (i == -1)
            return false;
        else
            return true;
    }

    /*
    public boolean addPasscodeToAlarm(String alarmName, String passcode) {
        ContentValues cv = new ContentValues();
        cv.put(ALARMS_TABLE_NAME, alarmName);
        cv.put(ALARMS_TABLE_PASSCODE, passcode);

        int numberOfRowsAffected = mDatabase.update(ALARMS_TABLE, cv, ALARMS_TABLE_NAME + "=" + alarmName, null);
        if (1 == numberOfRowsAffected) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addDescriptionToAlarm(String alarmName, String description) {
        ContentValues cv = new ContentValues();
        cv.put(ALARMS_TABLE_NAME, alarmName);
        cv.put(ALARMS_TABLE_DESCRIPTION, description);
        mDatabase.

        int numberOfRowsAffected = mDatabase.update(ALARMS_TABLE, cv, ALARMS_TABLE_NAME + "=" + alarmName, null);
        if (1 == numberOfRowsAffected) {
            return true;
        }
        else
        {
            return false;
        }
    }*/

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
        int numberOfRowsAffected = mDatabase.delete(ALARMS_TABLE, ALARMS_TABLE_NAME + "=" + alarmName, null);
        if (1 == numberOfRowsAffected) {
            return  true;
        }
        else
        {
            return false;
        }
    }
}
