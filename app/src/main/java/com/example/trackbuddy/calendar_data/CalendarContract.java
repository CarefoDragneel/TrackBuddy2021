package com.example.trackbuddy.calendar_data;

import android.net.Uri;
import android.provider.BaseColumns;

public class CalendarContract {


//    creating an empty default constructor as no objects needed to be created for this class
    private CalendarContract(){}

//    creating an inner class for creating a table in the data base
    public static final class CalendarEntry implements BaseColumns{

        //        we specify the table name
        public final static String TABLE_NAME = "events";

        //        here, store the id of the pets
        public final static String COLUMN_EVENTS_ID = BaseColumns._ID;

        //        here, store the name of Name column
        public final static String  COLUMN_EVENTS_DESCRIPTION = "description";

        //        here, store the date and time in the millisecond format
        public final static String COLUMN_EVENTS_OCCUR = "occur";

    }
}
