package com.example.trackbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trackbuddy.calendar_data.CalendarContract.CalendarEntry;
import com.example.trackbuddy.calendar_data.CalenderDbHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/*
* this class is to provide functionality to the fragment which will help user to
* enter events data
 */
public class AddNewEvent extends BottomSheetDialogFragment {

    // class TAG
    public static final String TAG = "AddNewEvent";

//    creating factory method
    public static AddNewEvent newInstance() {
        return new AddNewEvent();
    }

//    global variables
    private EditText mDescription;
    private Button mSave;
    private TextView mSetDueDate;
    private TextView mSetDueTime;
    private Context context;
    private String setDate = "";
    private String setTime = "";
    private String displayTime = "";

//    database helper variable
    private CalenderDbHelper mDbHelper;

//    this overridden method is used to inflate layout file
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_event, container, false);
    }

//    this overridden method is used to change functionality to the different view components
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        different hooks
        mDescription = view.findViewById(R.id.addDescription);
        mSave = view.findViewById(R.id.event_save_button);
        mSetDueDate = view.findViewById(R.id.set_due_date);
        mSetDueTime = view.findViewById(R.id.set_due_time);

//        adding functionality to the helper variable
        mDbHelper = new CalenderDbHelper(view.getContext());

        // this class is used to change the "SAVE" button in accordance with the text entered
        mDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSave.setEnabled(false);
                    mSave.setBackgroundColor(Color.GRAY);
                }else{
                    mSave.setEnabled(true);
                    mSave.setBackgroundColor(getResources().getColor(R.color.skin_200));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//      adding date picker to pick date
        mSetDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        mSetDueDate.setText(dayOfMonth + "/" + month + "/" + year);
                        setDate = year+"-"+month+"-"+dayOfMonth;

                    }
                } , YEAR , MONTH , DAY);

                datePickerDialog.show();
            }
        });

//      adding time picker to pick time
        mSetDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MINUTE = calendar.get(Calendar.MINUTE);
                int HOUR = calendar.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // modifications to show time in the correct way
                        int hr = hourOfDay;
                        String meridian = new String();
                        if(hr==0){
                            hr=12;
                            meridian = "AM";
                        }
                        else if(hr>12) {
                            hr-=12;
                            meridian = "PM";
                        }
                        else{
                            meridian = "AM";
                        }
                        String min = new String();
                        if(minute>=0 && minute<=9){
                            min = "0"+minute;
                        }
                        else min = ""+minute;
                        mSetDueTime.setText(hr+":"+min+" "+meridian);
                        setTime = hourOfDay+":"+minute;
                        displayTime = hr+":"+min+" "+meridian;
                    }
                },HOUR,MINUTE,false);

                timePickerDialog.show();
            }
        });

        // adding functionality to the save button
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(v.getContext());
                dismiss();
            }
        });

    }

    private long getTimeInMillis(String date) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate;/*from w  ww  .  ja  va2s . c  om*/
        try {
            String format = "HH:mm";
            if (date.contains("-")) {
                format = "yyyy-MM-dd HH:mm";
            }
            SimpleDateFormat dateForamt = new SimpleDateFormat(format);
            currentDate = dateForamt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();

            currentDate = new Date();
        }
        calendar.setTime(currentDate);
        Log.e(TAG,String.valueOf(calendar.getTimeInMillis()));
        return calendar.getTimeInMillis();
    }

    private void insertData(Context context){
        // obtaining description
        String description = mDescription.getText().toString();
        // obtaining the time and data in millisecond format
        long occur = getTimeInMillis(setDate+" "+setTime);
        // string which is added
        String added = description+" "+displayTime;

        // obtaining the database in writeable format
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //creating content values
        ContentValues values = new ContentValues();
        values.put(CalendarEntry.COLUMN_EVENTS_DESCRIPTION,description);
        values.put(CalendarEntry.COLUMN_EVENTS_OCCUR,String.valueOf(occur));

        // inserting values in the database
        long inserted_row_id = db.insert(CalendarEntry.TABLE_NAME,null,values);

        if(inserted_row_id!=-1){
            Log.e(TAG,"Event added in the database:");
            Log.e(TAG,description+" "+occur);

        }
        else{
            Log.e(TAG,"Event not added in the database");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
