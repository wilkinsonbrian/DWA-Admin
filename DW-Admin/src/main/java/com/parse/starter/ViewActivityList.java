package com.parse.starter;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewActivityList extends Activity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.view_activity_list);
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewActivityList.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "newActivty");
            query.whereExists("title");
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(ViewActivityList.this,
                    R.layout.listview_item);
            // Retrieve object "name" from Parse.com database
            for (ParseObject activity : ob) {
                adapter.add((String) activity.get("title"));
            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to TakeActivityAttendance Class
                    Bundle b = new Bundle();
                    ArrayList<String> studentsSignedUp = new ArrayList<String>();
                    ArrayList<String> studentsAttended = new ArrayList<String>();

                    for (Object name: ob.get(position).getList("participantsSignedUp")) {
                        studentsSignedUp.add(name.toString());
                    }
                    for (Object name: ob.get(position).getList("participantsAttended")) {
                        studentsAttended.add(name.toString());
                    }

                    b.putStringArrayList("signedUp", studentsSignedUp);
                    b.putStringArrayList("attended", studentsAttended);
                    b.putString("activityTitle", ob.get(position).getString("title"));
                    //b.putString("activityID", activityName);
                    Intent i = new Intent(ViewActivityList.this,
                            TakeActivityAttendance.class);
                    // Pass data "name" followed by the position
                    i.putExtras(b);

                    //i.putStringArrayListExtra("names", studentsSignedUp);
                    // Open TakeActivityAttendance.java Activity
                    startActivity(i);
                }
            });
        }
    }
}
