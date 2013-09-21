package com.waterloo.buddyalarm;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends Activity {

    ActionBar m_ActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        m_ActionBar = getActionBar();
        m_ActionBar.setTitle("Settings");
        m_ActionBar.setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_save:
                //saveChanges();
                return true;
            case R.id.action_cancel:
                //cancelChanges();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
