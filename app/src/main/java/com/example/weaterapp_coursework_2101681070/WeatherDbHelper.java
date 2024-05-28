package com.example.weaterapp_coursework_2101681070;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "weather";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";

    public static final String SEARCH_HISTORY_TABLE_NAME = "search_history";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CITY
            + " text not null, " + COLUMN_TEMPERATURE
            + " real not null);";

    private static final String SEARCH_HISTORY_TABLE_CREATE = "create table "
            + SEARCH_HISTORY_TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CITY
            + " text not null, " + COLUMN_TEMPERATURE
            + " real not null);";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(SEARCH_HISTORY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_HISTORY_TABLE_NAME);
        onCreate(db);
    }
}
