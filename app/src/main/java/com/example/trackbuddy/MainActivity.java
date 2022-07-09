package com.example.trackbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.trackbuddy.calendar_data.CalendarContract.CalendarEntry;
import com.example.trackbuddy.calendar_data.CalenderDbHelper;
import com.example.trackbuddy.notes_data.NotesContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nordan.dialog.NordanAlertDialog;
import com.skyhope.eventcalenderlibrary.CalenderEvent;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // initializing tag
    public final String TAG = MainActivity.class.getSimpleName();

    // context variable
    private Activity mActivity;

    //Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CalenderEvent mCalenderEvent;

//    global variables to retrieve the date which is selected in the calendar
    public static String selectedDate;

//    creating database helper object
    private CalenderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*----------------------hooks---------------------- */
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCalenderEvent = (CalenderEvent) findViewById(R.id.calender_event);

        /*-----------------------Tool Bar----------------------------*/
        setSupportActionBar(toolbar);

        /* -------------- Navigation Drawer Menu-----------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //For making navigation view item clickable
        navigationView.setNavigationItemSelectedListener(this);
        //For making navigation calender default selected
        navigationView.setCheckedItem(R.id.calender);

        /*--------- floating button functionality---------------- */
        FloatingActionButton addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                calling bottomSheetDialogFragment which clicking on the floating button
                AddNewEvent.newInstance().show(getSupportFragmentManager() , AddNewEvent.TAG);
            }
        });

        // getting context
        mActivity = this;

        // adding functionality to the db helper object
        mDbHelper = new CalenderDbHelper(this);

        // adding events to the calendar
        addEventsToCalendar();

        // adding click listener to the calendar
        mCalenderEvent.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(DayContainerModel dayContainerModel) {
                // checking if the event is present
                if(dayContainerModel.isHaveEvent()){
                    // taking out the event
                    Event currentEvent = dayContainerModel.getEvent();
                    // dialog box
                    new NordanAlertDialog.Builder(mActivity)
                            .setTitle("All events")
                            .setMessage(currentEvent.getEventText())
                            .setPositiveBtnText("Delete")
                            .onPositiveClicked(() -> {
                                mCalenderEvent.removeEvent(currentEvent);
                            })
                            .setNegativeBtnText("Cancel")
                            .onNegativeClicked(() -> {
                                //comes out
                            })
                            .build().show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"No event present",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addEventsToCalendar() {
        // creating a readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //projection
        String[] projections = {
                CalendarEntry.COLUMN_EVENTS_ID,
                CalendarEntry.COLUMN_EVENTS_DESCRIPTION,
                CalendarEntry.COLUMN_EVENTS_OCCUR
        };

        // creating a cursor
        Cursor cursor = db.query( CalendarEntry.TABLE_NAME, projections, null, null, null,
                null, null);

        // creating column index
        int desColIndx = cursor.getColumnIndex(CalendarEntry.COLUMN_EVENTS_DESCRIPTION);
        int occurColIndx = cursor.getColumnIndex(CalendarEntry.COLUMN_EVENTS_OCCUR);

        while(cursor.moveToNext()){
            // extracting values
            String description = cursor.getString(desColIndx);
            String occur = cursor.getString(occurColIndx);
            long time = Long.parseLong(occur);

            Log.e(TAG,description+" "+occur);

            // creating event
            Event event = new Event(time,description);
            // adding event
            mCalenderEvent.addEvent(event);
        }
    }

    // For making back button to close the only drawer menu

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.calender:
                break;
            case R.id.todo: {
                Intent i = new Intent(MainActivity.this, ToDoActivity.class);
                startActivity(i);
                break;
            }
            case R.id.notes:
            {
                Intent i = new Intent(MainActivity.this,NotesActivity.class);
                startActivity(i);
                break;
            }
            case R.id.Logout:
            {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}




