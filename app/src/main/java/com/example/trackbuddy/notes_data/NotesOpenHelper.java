package com.example.trackbuddy.notes_data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesOpenHelper extends SQLiteOpenHelper {

    // this is the name of the database which can contain many tables
    private static final String DATABASE_NAME = "shelter.db";
    // this is the database version
    private static final int DATABASE_VERSION = 1;

    // SQL command to create the sql database
    private final static String create_db = "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME + " ("
            + NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NotesContract.NotesEntry.COLUMN_NOTE_TITLE + " TEXT NOT NULL, "
            + NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION+ " TEXT);";

    // SQL command to drop table
    private final static String delete_dp = "DROP TABLE "+NotesContract.NotesEntry.TABLE_NAME+";";

    // constructor
    public NotesOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(delete_dp);
        onCreate(db);
    }
}
