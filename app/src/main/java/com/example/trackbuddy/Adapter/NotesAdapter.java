package com.example.trackbuddy.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trackbuddy.R;
import com.example.trackbuddy.notes_data.NotesItem;

import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<NotesItem> {

    public NotesAdapter(@NonNull Context context, @NonNull ArrayList<NotesItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // we are creating a new view and storing it in by overriding the default, one textview containing, convertview
        View listView = convertView;
        // we inflate new layout
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.notes_list_items,parent,false);
        }

        // this returns the item which will be present at a particular position
        NotesItem item = getItem(position);

        // initializing hook for the text view present in list item
        TextView title_text_view = listView.findViewById(R.id.notes_item_title);
        TextView description_text_view = listView.findViewById(R.id.notes_item_description);

        //setting value in text view
        title_text_view.setText(item.getNotesItemTitle());
        description_text_view.setText(item.getNotesItemDescription());

        return listView;
    }


}
