package com.example.trackbuddy.notes_data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NotesContract  {

    private NotesContract(){}

    // this shows the entries of the table which contains the notes
    public static class NotesEntry implements BaseColumns {

        // this contains the name of the table
        public final static String TABLE_NAME = "notes";

        // this contains the id of the table
        public final static String _ID = BaseColumns._ID;

        // this contains the title of the notes
        public final static String COLUMN_NOTE_TITLE ="title";

        // this contains the description in the notes
        public final static String COLUMN_NOTE_DESCRIPTION = "description";

    }
}
