package com.example.trackbuddy.calendar_data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.trackbuddy.calendar_data.CalendarContract.CalendarEntry;

import androidx.annotation.Nullable;

public class CalenderDbHelper extends SQLiteOpenHelper {

    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_NAME = "calendar";

    public CalenderDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String DATABASE_CREATE_TABLE_COMMAND = "CREATE TABLE " + CalendarEntry.TABLE_NAME + "("
                + CalendarEntry.COLUMN_EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CalendarEntry.COLUMN_EVENTS_DESCRIPTION + " TEXT NOT NULL,"
                + CalendarEntry.COLUMN_EVENTS_OCCUR + " TEXT NOT NULL"
                + " );" ;

        db.execSQL(DATABASE_CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ CalendarEntry.TABLE_NAME);
        onCreate(db);
    }
}
