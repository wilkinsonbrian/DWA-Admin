package com.parse.starter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;


public class TakeActivityAttendance extends ListActivity {
    // Declare Variables
    TextView txtname;
    CheckedTextView chkView;
    String activityID;
    ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> studentsSignedUp;
    ArrayList<String> studentsAttended;
    Integer itemCount;
    ParseObject currentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.attendence_view);

        // Retrieve data from ViewActivityList class on item click event
        //Bundle b = this.getIntent().getExtras();
        Intent i = getIntent();
        studentsSignedUp = i.getStringArrayListExtra("signedUp");
        studentsAttended = i.getStringArrayListExtra("attended");
        String activityTitle = i.getStringExtra("activityTitle");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("newActivty");

        // Retrieve the object by the title which was passed from the last activity
        query.whereEqualTo("title", activityTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject activity, ParseException e) {
                if (e == null) {
                    currentActivity = activity;
                }
            }
        });

        addNamesToLayout();
        checkIfAlreadyTakenAttendance();
    }

    private void addNamesToLayout() {

        listview = getListView();

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, studentsSignedUp));

        // Allows multiple selections to be made
        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
        itemCount = listview.getAdapter().getCount();

    }

            public void onListItemClick(ListView parent, View v, int position, long id) {
        /*
        Already have the students attended array.  Need to add a student to the array when tapped,
        then update in Parse.  If a student is already checked, remove from array, then update.
        If they are not already checked, add to the array, then update.

        Since the item toggles being checked as soon as it is clicked, must see what the state was
        before it was tapped.  So the if statement is being evaluated AFTER the item is tapped.
         */
                ArrayList<String> currentStudent = new ArrayList<String>();
                currentStudent.add(studentsSignedUp.get(position));
                if (listview.isItemChecked(position)) {

                    // Attendence is being taken and the student will be added to the list
                    // of participants attended.
                    currentActivity.addAll("participantsAttended", currentStudent);
                    currentActivity.saveInBackground();
                } else {
                    // Student has attended the event, but needs to be removed for some reason.
                    // Maybe they decided not to go at the last minute.
                    currentActivity.removeAll("participantsAttended", currentStudent);
                    currentActivity.saveInBackground();
                }

            }

            private void checkIfAlreadyTakenAttendance() {
                listview.post(new Runnable() {
                    public void run() {
                        for (int i = 0; i <= listview.getLastVisiblePosition() - listview.getFirstVisiblePosition(); i++) {
                            CheckedTextView item = (CheckedTextView) listview.getChildAt(i);
                            String name = listview.getItemAtPosition(i).toString();
                            if (name != null) {
                                if (studentsAttended.contains(name)) {
                                    listview.setItemChecked(i, true);
                                    //item.setChecked(true);
                                }
                            }
                        }
                    }
                });

            }

        }
