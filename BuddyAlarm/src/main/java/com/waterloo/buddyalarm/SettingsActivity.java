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

    private int id;
    private String name;
    private String description;
    private String time;
    private String passcode;

    private void init() {
        id = -1;
        name = "";
        description = "";
        time = "";
        passcode = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.
        setContentView(R.layout.activity_settings);
        mActivity = this;
        init();

        m_AlarmNameEdit   = (EditText) findViewById(R.id.et_AlarmName);
        m_AlarmPassEdit   = (EditText) findViewById(R.id.et_AlarmPass);

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
        String m_TimePickerString = "";
        TimePicker m_TimePicker;
        long time;

        m_TimePicker = (TimePicker) findViewById((R.id.tp_AlarmTime));
        m_AlarmNameEdit   = (EditText) findViewById(R.id.et_AlarmName);
        m_AlarmPassEdit   = (EditText) findViewById(R.id.et_AlarmPass);

        if(m_AlarmNameEdit != null && !m_AlarmNameEdit.getText().toString().isEmpty()){
            if(m_AlarmPassEdit != null && !m_AlarmPassEdit.getText().toString().isEmpty()){
                m_AlarmNameChange = m_AlarmNameEdit.getText().toString();
                m_AlarmPassChange = m_AlarmPassEdit.getText().toString();

                time = (m_TimePicker.getCurrentMinute()*60 + m_TimePicker.getCurrentHour()*60*60)*1000;

                //Log.i("Current Hour", String.valueOf(m_TimePicker.getCurrentHour()));
                //Log.i("Current Minute", String.valueOf(m_TimePicker.getCurrentMinute()));
                //Log.i("Milliseconds", String.valueOf(time));

                m_TimePickerString = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(time),
                        TimeUnit.MILLISECONDS.toMinutes(time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)));

                //Log.i("Time:", m_TimePickerString);

                Database db = new Database(mActivity);
                db.open();
                db.addOrUpdateAlarmInDatabase(id, m_AlarmNameChange, m_TimePickerString, m_AlarmPassChange, "description");
                db.close();

                Intent intent = new Intent(mActivity, MainActivity.class);
                Intent currentIntent = new Intent(mActivity, SettingsActivity.class);
                startActivity(intent);
                stopService(currentIntent);
            }
            else
            {
                showSaveChangesDialog();
            }
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
        time = String.valueOf(details.get(2));
        passcode = details.get(3);
        description = details.get(4);

        m_AlarmNameEdit.setText(name);
        m_AlarmPassEdit.setText(passcode);
    }
}
