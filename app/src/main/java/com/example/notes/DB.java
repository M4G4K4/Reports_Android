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
    private static final String ID = "_id";
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
        String query = "CREATE TABLE " + dbTable + "(" + ID + " INTEGER PRIMARY KEY," +
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
        return id;
    }

    // Get 1 note
    public Notes getNote(long id2){
        // Constructor requirement :
        // String title , String description, String time , String date

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(dbTable,new String[]{ID,title,description,time,date}, ID +"=?"
                ,new String[]{String.valueOf(id2)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        assert cursor != null;
        return new Notes(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
    }

    // Get all notes
    public List<Notes> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Notes> notesList = new ArrayList<>();

        String query = "Select * from " + dbTable;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                Notes notes = new Notes();

                notes.setId(cursor.getLong(0));
                notes.setTitle(cursor.getString(1));
                notes.setDescription(cursor.getString(2));
                notes.setTime(cursor.getString(3));
                notes.setDate(cursor.getString(4));

                System.out.println("DB getNotes ID : " + notes.getId());

                notesList.add(notes);

            }while (cursor.moveToNext());
        }

        return notesList;
    }

    // Edit note
    public int editNote(Notes note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put(title,note.getTitle());
        c.put(description,note.getDescription());
        c.put(date,note.getDate());
        c.put(time,note.getTime());

        return db.update(dbTable,c, ID +"=?",new String[]{String.valueOf(note.getId())});
    }


    // Delete note
    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(dbTable,id+" = ?",new String[]{String.valueOf(id)});
        db.execSQL("DELETE FROM "+dbTable+" WHERE "+ID+"='"+id+"'");
        db.close();
    }



}
