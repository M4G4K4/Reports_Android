package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final int dbVersion = 2;
    private static final String dbName = "noteDB";
    private static final String dbTable = "notes";

    // Columns
    private static final String id = "id";
    private static final String title = "title";
    private static final String description = "description";
    private static final String date = "date";
    private static final String time = "time";


    DB(Context context){
        super(context,dbName,null,dbVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Query to create table
        String query = "CREATE TABLE " + dbTable + "(" + id + " INT PRIMARY KEY," +
                title + " TEXT,"+
                description + " TEXT," +
                date + " TEXT," +
                time + " TEXT" + ")";

        // Excecute table
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {
        if(i >= il){
            return;
        }

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbName);
        onCreate(sqLiteDatabase);
    }

    // Add new note to DB
    public long addNote(Notes notes){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put(title,notes.getTitle());
        c.put(description,notes.getDescription());
        c.put(time,notes.getTime());
        c.put(date,notes.getDate());

        long id = db.insert(dbTable,null,c);
        System.out.println("ID : " + id);

        return id;
    }

    // Get 1 note
    private Notes getNote(long id2){
        // Constructor requirement :
        // String title , String description, String time , String date

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(dbTable,new String[]{id,title,description,time,date},id+"=?"
                ,new String[]{String.valueOf(id2)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return new Notes(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
    }

    // Get all notes
    private List<Notes> getNotes(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Notes> notesList = new ArrayList<>();

        String query = "Select * from " + dbTable;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor != null){
            do {
                Notes notes = new Notes();

                notes.setId(cursor.getLong(0));
                notes.setTitle(cursor.getString(1));
                notes.setDescription(cursor.getString(2));
                notes.setTime(cursor.getString(3));
                notes.setDate(cursor.getString(4));

                notesList.add(notes);

            }while (cursor.moveToNext());
        }



        return notesList;
    }


}
