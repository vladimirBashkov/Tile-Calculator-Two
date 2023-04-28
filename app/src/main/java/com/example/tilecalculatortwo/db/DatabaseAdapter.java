package com.example.tilecalculatortwo.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DatabaseAdapter {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ARTICLE, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_VOLUME, DatabaseHelper.COLUMN_PIECES_IN_PACK};
        return  database.query(DatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public ArrayList<Box> getBoxes(){
        ArrayList<Box> boxes = new ArrayList<>();
        Cursor cursor = getAllEntries();
        while (cursor.moveToNext()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ARTICLE));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            @SuppressLint("Range") double volume = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_VOLUME));
            @SuppressLint("Range") int piecesInPack = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PIECES_IN_PACK));
            boxes.add(new Box(id, name, volume, piecesInPack));
        }
        cursor.close();
        return  boxes;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    public Box getBox(int article){
        Box box = null;
        String query = String.format("SELECT * FROM %s WHERE %s = ? ", DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ARTICLE);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(article)});
        if(cursor.moveToFirst()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            @SuppressLint("Range") double volume = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_VOLUME));
            @SuppressLint("Range") int piecesInPack = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PIECES_IN_PACK));
            box = new Box(article, name, volume, piecesInPack);
        }
        cursor.close();
        return box;
    }

    public boolean boxIsExists(int article){
        String query = String.format("SELECT * FROM %s WHERE %s = ? ", DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ARTICLE);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(article)});
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ArrayList<Box> getBox(String text){
        ArrayList<Box> boxArray = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s LIKE ? OR %s LIKE ? OR %s LIKE ?",
                DatabaseHelper.TABLE, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_VOLUME, DatabaseHelper.COLUMN_PIECES_IN_PACK);
        Cursor cursor = database.rawQuery(query, new String[]{ ("%" + text + "%"),
                ("%" + text + "%"), ("%" + text + "%")});
        if(cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int article = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ARTICLE));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                @SuppressLint("Range") double volume = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_VOLUME));
                @SuppressLint("Range") int piecesInPack = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PIECES_IN_PACK));
                Box box = new Box(article, name, volume, piecesInPack);
                boxArray.add(box);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return boxArray;
    }

    public long insert(Box box){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ARTICLE, box.getArticle());
        cv.put(DatabaseHelper.COLUMN_NAME, box.getName());
        cv.put(DatabaseHelper.COLUMN_VOLUME, box.getVolume());
        cv.put(DatabaseHelper.COLUMN_PIECES_IN_PACK, box.getPiecesInPack());
        return  database.insert(DatabaseHelper.TABLE, null, cv);
    }

    public long delete(int userId){
        String whereClause = "article = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Box box){
        String whereClause = DatabaseHelper.COLUMN_ARTICLE + "=" + box.getArticle();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, box.getName());
        cv.put(DatabaseHelper.COLUMN_VOLUME, box.getVolume());
        cv.put(DatabaseHelper.COLUMN_PIECES_IN_PACK, box.getPiecesInPack());
        return database.update(DatabaseHelper.TABLE, cv, whereClause, null);
    }
}
