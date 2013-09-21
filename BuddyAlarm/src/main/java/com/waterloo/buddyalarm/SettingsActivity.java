package com.waterloo.buddyalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    Activity mActivity;

    ActionBar m_ActionBar;
    EditText m_AlarmNameEdit;
    EditText m_AlarmPassEdit;

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
                //cancelChanges();
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
            builder.setMessage(R.string.alert_NameSaveChanges)
                    .setPositiveButton(R.string.alert_OptionsSaveChanges, new DialogInterface.OnClickListener() {
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
        String m_AlarmPassChange;

        if(m_AlarmNameEdit.getText().toString().isEmpty() || m_AlarmNameEdit == null){
            showSaveChangesDialog();
        }
        else{
            m_AlarmNameChange = m_AlarmNameEdit.getText().toString();
        }

        Database db = new Database(mActivity);
        db.open();
        db.addOrUpdateAlarmInDatabase(id, m_AlarmNameChange, 0, "abs", "sss");
        db.close();
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
    }
}
