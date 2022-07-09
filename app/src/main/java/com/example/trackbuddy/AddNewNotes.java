package com.example.trackbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.example.trackbuddy.notes_data.NotesContract.NotesEntry;
import com.example.trackbuddy.notes_data.NotesOpenHelper;

public class AddNewNotes extends AppCompatActivity {

    //creating a tag
    private final String TAG = AddNewNotes.class.getSimpleName();

    // creating variables
    private EditText mNotesTitle;
    private EditText mNotesDescription;
    private Button mSaveButton;

    //creating database variable
    NotesOpenHelper mDbHelper;
    SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_notes);

        // creating hooks
        mNotesTitle = (EditText) findViewById(R.id.notes_enter_title);
        mNotesDescription = (EditText) findViewById(R.id.notes_enter_description);
        mSaveButton = (Button) findViewById(R.id.notes_enter_save);

        // creating database helper class which will give the database object in which we can store value
        mDbHelper = new NotesOpenHelper(this);
        mDb = mDbHelper.getWritableDatabase();


        // adding functionality to the save button
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // strings taken from the edit text
                String title = mNotesTitle.getText().toString().trim();
                String description = mNotesDescription.getText().toString().trim();

                // creating content values by extracting data from edit views
                ContentValues values = new ContentValues();
                values.put(NotesEntry.COLUMN_NOTE_TITLE,title);
                values.put(NotesEntry.COLUMN_NOTE_DESCRIPTION,description);

                // inserting the new row in the database
                // returns the id of the new row which is added
                long inserted_row_id = mDb.insert(NotesEntry.TABLE_NAME,null,values);
                if(inserted_row_id!=-1){
                    Toast toast = Toast.makeText(getApplicationContext(),"Row entered successfully ",Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Row not entered successfully. Try Again ",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
}