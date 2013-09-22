package com.waterloo.buddyalarm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends Activity {

    Activity mActivity;

    ActionBar m_ActionBar;
    EditText m_AlarmNameEdit;
    EditText m_AlarmPassEdit;
    EditText m_AlarmDescEdit;
    TimePicker m_TimePicker;

    private int id;
    private String name;
    private String description;
    private int time;
    private String passcode;

    private void init() {
        id = -1;
        name = "";
        description = "";
        time = 0;
        passcode = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_settings);
        mActivity = this;
        init();

        m_AlarmNameEdit   = (EditText) findViewById(R.id.et_AlarmName);
        m_AlarmPassEdit   = (EditText) findViewById(R.id.et_AlarmPass);
        m_AlarmDescEdit   = (EditText) findViewById(R.id.et_AlarmDescription);
        m_TimePicker = (TimePicker) findViewById((R.id.tp_AlarmTime));

        m_ActionBar = getActionBar();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            populateFields(extras.getString("NAME"));
        }
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
            case R.id.action_save:
                saveChanges();
                return true;
            case R.id.action_cancel:
                super.onBackPressed();
                return true;
            case R.id.action_delete:
                if (id == -1) {
                    super.onBackPressed();
                    return true;
                }
                Database db = new Database(mActivity);
                db.open();
                db.deleteAlarm(name);
                db.close();
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SaveChangesDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.alert_SaveChanges)
                    .setPositiveButton(R.string.alert_OptionsOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    public void saveChanges()
    {
        String m_AlarmNameChange = "";
        String m_AlarmPassChange = "";
        String m_AlarmDescChange = "";
        String m_TimePickerString = "";
        int time;

        if((m_AlarmNameEdit != null && !m_AlarmNameEdit.getText().toString().isEmpty())
          || (m_AlarmPassEdit != null && !m_AlarmPassEdit.getText().toString().isEmpty())
          || (m_AlarmDescEdit != null && !m_AlarmDescEdit.getText().toString().isEmpty())) {

                m_AlarmNameChange = m_AlarmNameEdit.getText().toString();
                m_AlarmPassChange = m_AlarmPassEdit.getText().toString();
                m_AlarmDescChange = m_AlarmDescEdit.getText().toString();
                time = (m_TimePicker.getCurrentMinute()*60 + m_TimePicker.getCurrentHour()*60*60)*1000;

//                m_TimePickerString = String.format("%02d:%02d",
//                        TimeUnit.MILLISECONDS.toHours(time),
//                        TimeUnit.MILLISECONDS.toMinutes(time) -
//                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)));

                //Log.i("Time:", m_TimePickerString);

                Database db = new Database(mActivity);
                db.open();

                db.addOrUpdateAlarmInDatabase(id, m_AlarmNameChange, time, m_AlarmPassChange, m_AlarmDescChange, "true");

                db.close();

                setResult(RESULT_OK);
                finish();
        }
        else
        {
            showSaveChangesDialog();
        }
    }

    private void showSaveChangesDialog() {
        DialogFragment newFragment = new SaveChangesDialog();
        newFragment.show(getFragmentManager(), "test");
    }

    private void populateFields(String alarmName) {
        Database db = new Database(mActivity);
        db.open();
        ArrayList<String> details = db.getAlarmDetails(alarmName);

        id = Integer.valueOf(details.get(0));
        name = details.get(1);
        time = Integer.valueOf(details.get(2));
        passcode = details.get(3);
        description = details.get(4);

        m_AlarmNameEdit.setText(name);
        m_AlarmPassEdit.setText(passcode);
        m_AlarmDescEdit.setText(description);

        int tempTimeInSeconds = time/1000;
        int hour = tempTimeInSeconds/3600;
        int minute = (tempTimeInSeconds % 3600) / 60;
        m_TimePicker.setCurrentHour(hour);
        m_TimePicker.setCurrentMinute(minute);

    }
}
