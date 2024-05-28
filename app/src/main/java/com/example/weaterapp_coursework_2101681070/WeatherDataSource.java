package com.example.weaterapp_coursework_2101681070;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDataSource {
    private SQLiteDatabase database;
    private WeatherDbHelper dbHelper;

    public WeatherDataSource(Context context) {
        dbHelper = new WeatherDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getAllSearchHistory() {
        return database.query(WeatherDbHelper.SEARCH_HISTORY_TABLE_NAME, null, null, null, null, null, null);
    }

    public long insertSearchHistory(String city, double temperature) {
        ContentValues values = new ContentValues();
        values.put(WeatherDbHelper.COLUMN_CITY, city);
        values.put(WeatherDbHelper.COLUMN_TEMPERATURE, temperature);
        return database.insert(WeatherDbHelper.SEARCH_HISTORY_TABLE_NAME, null, values);
    }
    public boolean deleteSearchHistory(long id) {
        return database.delete(WeatherDbHelper.SEARCH_HISTORY_TABLE_NAME,
                WeatherDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;
    }
}

