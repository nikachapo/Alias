package com.example.nick.wordsgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "words_database.db";
    private static final int DB_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE words(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "word TEXT NOT NULL," +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "words");
        onCreate(db);
    }

    public void insertData(String word){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("word",word);
        database.insert("words",null, contentValues);

        database.close();

    }

    public ArrayList<Integer> getAllID(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from words GROUP BY word",null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getInt(cursor.getColumnIndex("_id")));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<String> getAllWords() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from words GROUP BY word", null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            array_list.add(cursor.getString(cursor.getColumnIndex("word")));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return array_list;
    }

    public ArrayList<String> getSearchedWords(String searchedChars){
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from words " +
                "WHERE word LIKE '"+searchedChars+"%'" +
                " GROUP BY word", null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            array_list.add(cursor.getString(cursor.getColumnIndex("word")));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return array_list;
    }
    public void deleteWord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("words",
                "_id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getNumberOfWords(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "words");
        return numRows;
    }
}
