package com.bartholome.voicetransport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SearchDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
        MySQLiteHelper.COLUMN_ID,
        MySQLiteHelper.COLUMN_START,
        MySQLiteHelper.COLUMN_FINISH,
        MySQLiteHelper.COLUMN_TIME };

    public SearchDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Search createSearch(String start, String end, String duration) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_START, start);
        values.put(MySQLiteHelper.COLUMN_FINISH, end);
        values.put(MySQLiteHelper.COLUMN_TIME, duration);

        long insertId = database.insert(MySQLiteHelper.TABLE_SEARCHS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SEARCHS,
                allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Search newSearch = cursorToSearch(cursor);
        cursor.close();
        return newSearch;
    }

    public void deleteSearch(Search search) {
        long id = search.getId();
        database.delete(MySQLiteHelper.TABLE_SEARCHS,
                MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Search> getAllSearchs() {
        List<Search> searchs = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SEARCHS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Search search = cursorToSearch(cursor);
            searchs.add(search);
            cursor.moveToNext();
        }

        cursor.close();
        return searchs;
    }

    private Search cursorToSearch(Cursor cursor) {
        Search search = new Search();
        search.setId(cursor.getLong(0));
        search.setStart(cursor.getString(1));
        search.setFinish(cursor.getString(2));
        search.setDuration(cursor.getString(3));
        return search;
    }
}
