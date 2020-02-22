package com.example.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

}
