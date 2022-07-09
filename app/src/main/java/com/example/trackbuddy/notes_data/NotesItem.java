package com.example.trackbuddy.notes_data;

// this class is to provide a custom class to store the value of each item
public class NotesItem {

    // this stores the id of the item
    public int mNotesItemId;

    // this stores the title of the item
    public String mNotesItemTitle;

    // this stores the description of the item
    public String mNotesItemDescription;

    public NotesItem(){}

    public NotesItem(int id,String title,String description){
        mNotesItemId = id;
        mNotesItemTitle = title;
        mNotesItemDescription = description;
    }

    public int getNotesItemId() {
        return mNotesItemId;
    }

    public String getNotesItemTitle() {
        return mNotesItemTitle;
    }

    public String getNotesItemDescription() {
        return mNotesItemDescription;
    }

}
