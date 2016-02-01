/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class MainActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Determine whether the current user is an anonymous user
    if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
      // If user is anonymous, send the user to LoginSignupActivity.class
      Intent intent = new Intent(MainActivity.this,
              LoginSignupActivity.class);
      startActivity(intent);
      finish();
    } else {
      // If current user is NOT anonymous user
      // Get current user data from Parse.com
      ParseUser currentUser = ParseUser.getCurrentUser();
      if (currentUser != null) {
        // Send logged in users to Welcome.class
        Intent intent = new Intent(MainActivity.this, Welcome.class);
        startActivity(intent);
        finish();
      } else {
        // Send user to LoginSignupActivity.class
        Intent intent = new Intent(MainActivity.this,
                LoginSignupActivity.class);
        startActivity(intent);
        finish();
      }
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
