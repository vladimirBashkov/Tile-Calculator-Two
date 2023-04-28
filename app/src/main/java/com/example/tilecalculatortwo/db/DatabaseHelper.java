package com.example.tilecalculatortwo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "boxcalculator.db";
    private static final int SCHEMA = 1; // DB version
    static final String TABLE = "boxes";
    public static final String COLUMN_ARTICLE = "article";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VOLUME = "volume";
    public static final String COLUMN_PIECES_IN_PACK = "pieces_in_pack";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ARTICLE
                + " INTEGER PRIMARY KEY UNIQUE, " + COLUMN_NAME
                + " TEXT, " + COLUMN_VOLUME + " REAL, " + COLUMN_PIECES_IN_PACK + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
