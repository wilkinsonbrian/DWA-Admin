package com.parse.starter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by brianwilkinson on 1/5/16.
 */
public class AddNewActivity extends Activity implements OnClickListener {

    private EditText startDate;
    private EditText endDate;
    private EditText startTime;
    private EditText endTime;

    private EditText aTitle;
    private EditText aDescription;
    private EditText maxParticipants;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.add_new_activity);
        dateFormatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm aa", Locale.US);


        findViewsById();
        setDateTimeFields();

    }

    private void findViewsById() {
        startDate = (EditText) findViewById(R.id.start_date_field);
        endDate = (EditText) findViewById(R.id.end_date_field);
        startTime = (EditText) findViewById(R.id.start_time_field);
        endTime = (EditText) findViewById(R.id.end_time_field);
        aTitle = (EditText) findViewById(R.id.title_field);
        aDescription = (EditText) findViewById(R.id.description_field);
        maxParticipants = (EditText) findViewById(R.id.max_participants_field);

    }

    private void setDateTimeFields() {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                startTime.setText(timeFormatter.format(newTime.getTime()));
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                endTime.setText(timeFormatter.format(newTime.getTime()));
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);
    }



    public void saveNewActivity(View view) {
        // TODO Save the activity to Parse
        // Convert maxParticipants into an Integer
        String max = maxParticipants.getText().toString();
        Integer finalMax = Integer.parseInt(max);


        // Create the date and time strings to match iOS version
        StringBuilder startDateAndTime = new StringBuilder();
        StringBuilder endDateAndTime = new StringBuilder();
        startDateAndTime.append(startDate.getText().toString());
        startDateAndTime.append(" at ");
        startDateAndTime.append(startTime.getText().toString());
        endDateAndTime.append(endDate.getText().toString());
        endDateAndTime.append(" at ");
        endDateAndTime.append(endTime.getText().toString());

        // Create the Parse Object and create key, value pairs for each field.
        ParseObject activity = new ParseObject("newActivty");


        /**
         * Create the access control list for the activity
         * this allows students to be added to the participant signed up
         * and participant attended lists.
         */
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        activity.setACL(acl);

        activity.put("title", aTitle.getText().toString());
        activity.put("activityDescription", aDescription.getText().toString());
        activity.put("maxParticipants", finalMax);
        activity.put("participantsSignedUp", Collections.<String>emptyList());
        activity.put("participantsAttended", Collections.<String>emptyList());
        activity.put("startTime", startDateAndTime.toString());
        activity.put("endTime", endDateAndTime.toString());

        activity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Show a simple Toast message upon successful registration
                    Toast.makeText(getApplicationContext(),
                            "Activity Saved!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "There was an error saving this activity", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        /**
         * Determines which view (Entry text field) has been tapped
         * and shows the appropriate picker.
         */
        if(view == startDate) {
            startDatePickerDialog.show();
        } else if(view == endDate) {
            endDatePickerDialog.show();
        } else if (view == startTime) {
            startTimePickerDialog.show();
        } else if (view == endTime) {
            endTimePickerDialog.show();
        }
    }

}
