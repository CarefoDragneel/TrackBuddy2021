package com.example.trackbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackbuddy.Adapter.NotesAdapter;
import com.example.trackbuddy.notes_data.NotesContract.NotesEntry;
import com.example.trackbuddy.notes_data.NotesItem;
import com.example.trackbuddy.notes_data.NotesOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private final String TAG = NotesActivity.class.getSimpleName();

    // creating variables
    private ListView mNotesList;

    //creating a global variable for the adapter
    NotesAdapter mAdapter;

    // declaring database variables
    private NotesOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // attaching hooks
        mNotesList = (ListView) findViewById(R.id.notes_list);

        // creating database variables
        mDbHelper = new NotesOpenHelper(this);

        displayInfo();

        // creating listener on each list item
        mNotesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //creating an alert dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                // setting message and title
                builder.setTitle(R.string.dialogbox_title).setMessage(R.string.dialogbox_message);
                //setting options
                builder.setPositiveButton(R.string.dialogbox_pos_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // creating a writable database variable
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();

                        // obtaining the item which is selected
                        NotesItem item = (NotesItem) parent.getItemAtPosition(position);

                        // creating selection for the delete statement
                        String selection = NotesEntry._ID + " LIKE ?";
                        String selectionArgs[] = {String.valueOf(item.getNotesItemId())};

                        // delete command
                        int deletedRows =  db.delete(NotesEntry.TABLE_NAME,selection,selectionArgs);

                        displayInfo();
                    }
                });
                builder.setNegativeButton(R.string.dialogbox_neg_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // creating the dialog
                AlertDialog dialog = builder.create();
                // showing the dialog
                dialog.show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayInfo();
    }

    private void displayInfo(){

        // making a readable database object
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // this is projection which specifies which columns to take out from the database
        String[] projections = {
                NotesEntry._ID,
                NotesEntry.COLUMN_NOTE_TITLE,
                NotesEntry.COLUMN_NOTE_DESCRIPTION
        };
        // creating cursor
        Cursor cursor = db.query( NotesEntry.TABLE_NAME, projections, null, null, null,
                null, null);

        // creating arraylist which will store the retrieved data
        ArrayList<NotesItem> list = new ArrayList<>();
        // creating object of the array adapter
        NotesAdapter adapter = new NotesAdapter(this,list);

        // adding data to arraylist
        while(cursor.moveToNext()) {
            //setting the column indices
            int idColIndx = cursor.getColumnIndex(NotesEntry._ID);
            int titleColIndx = cursor.getColumnIndex(NotesEntry.COLUMN_NOTE_TITLE);
            int descriptionColIndx = cursor.getColumnIndex(NotesEntry.COLUMN_NOTE_DESCRIPTION);

            Log.e(TAG, cursor.getString(titleColIndx)+" "+cursor.getString(descriptionColIndx));
            // retrieving data
            int id = cursor.getInt(idColIndx);
            String title = cursor.getString(titleColIndx);
            String description = cursor.getString(descriptionColIndx);
            adapter.add(new NotesItem(id, title, description));
        }

        // setting adapter to the listview
        mNotesList.setAdapter(adapter);

        cursor.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_new_note_option:
                Intent intent = new Intent(this,AddNewNotes.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}