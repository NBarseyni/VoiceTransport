package com.bartholome.voicetransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SEARCHS = "searchs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_FINISH = "finish";
    public static final String COLUMN_TIME = "duration";

    private static final String DATABASE_NAME = "searchs.db";
    private static final int DATABASE_VERSION = 1;

    // Commande SQL pour la création
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SEARCHS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_START
            + " text not null, " + COLUMN_FINISH
            + " text not null, " + COLUMN_TIME
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHS);
        onCreate(db);
    }
}
