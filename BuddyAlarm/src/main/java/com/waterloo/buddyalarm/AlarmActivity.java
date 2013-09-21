package com.waterloo.buddyalarm;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Andersen on 21/09/13.
 */
public class AlarmActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.alarm_page);
    }
}